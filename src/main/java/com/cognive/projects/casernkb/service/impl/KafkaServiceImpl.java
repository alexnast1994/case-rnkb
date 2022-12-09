package com.cognive.projects.casernkb.service.impl;

import com.cognive.projects.casernkb.config.BindingMappingConfig;
import com.cognive.projects.casernkb.config.MessageMappingConfig;
import com.cognive.projects.casernkb.config.property.KafkaServerProperties;
import com.cognive.projects.casernkb.model.ErrorDto;
import com.cognive.projects.casernkb.model.PipelineResponse;
import com.cognive.projects.casernkb.service.BPMProcessService;
import com.cognive.projects.casernkb.service.KafkaService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.ObjectValue;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaServiceImpl implements KafkaService {

    private final BindingMappingConfig topicMappingConfig;

    private final MessageMappingConfig messageMappingConfig;

    private final BPMProcessService bpmService;

    private final StreamBridge streamBridge;

    private final KafkaServerProperties properties;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Bean
    public Consumer<Message<String>> commonMessageInput() {
        return x -> {
            runProcess(x, () -> {
                String amlHeader = getHeaderData(x.getHeaders().get(properties.getCamunda().getHeaderName()));

                log.info("Common message amlHeader={}", amlHeader);

                Map<String, Object> variables = new HashMap<>();

                // Store value as json, prevent Camunda String limitation (4000 and 2000 for Oracle)
                ObjectValue jsonData = Variables.objectValue(x.getPayload()).serializationDataFormat("application/json").create();
                variables.put("payload", jsonData);

                return new Process(amlHeader,
                        amlHeader == null,
                        variables);
                    });
        };
    }

    @Bean
    public Consumer<Message<String>> csmMessageInput() {
        return x -> {
            runProcess(x, () -> {
                String operationHeader = getHeaderData(x.getHeaders().get(properties.getCsm().getHeaderName()));

                log.info("CSM message operationHeader={}", operationHeader);

                Map<String, Object> variables = new HashMap<>();
                // Store value as json, prevent Camunda String limitation (4000 and 2000 for Oracle)
                ObjectValue jsonData = Variables.objectValue(x.getPayload()).serializationDataFormat("application/json").create();
                variables.put("payload", jsonData);
                variables.put("processName", operationHeader);

                return new Process(properties.getCsm().getProcessName(),
                        operationHeader == null,
                        variables);
            });
        };
    }

    @Bean
    public Consumer<Message<String>> kycCaseCreationInput() {
        return x -> {
            runProcess(x, () -> {
                Map<String, Object> variables = new HashMap<>();
                ObjectValue jsonData = Variables.objectValue(x.getPayload()).serializationDataFormat("application/json").create();
                variables.put("payload", jsonData);

                return new Process(properties.getKyc().getProcessName(),
                        false,
                        variables);
            });

        };
    }

    @Bean
    public Consumer<Message<String>> pipelineMessageInput() {
        return x -> {
            runProcess(x, () -> {
                PipelineResponse pipelineResponse = getPipelineResponse(x.getPayload());
                log.info("Pipeline workflow id: {}, mapping: {}, status: {}",
                        pipelineResponse.getWorkflowId(),
                        pipelineResponse.getMappingName(),
                        pipelineResponse.getStatus());

                Map<String, Object> variables = new HashMap<>();
                variables.put("processName", pipelineResponse.getMappingName());

                return new Process(properties.getPipeline().getProcessName(),
                        !pipelineResponse.isDone(),
                        variables);
            });
        };
    }

    /**
     * Send message to topic
     *
     * @param messageId - id for message
     * @param key       - kafka key
     * @param data      - kafka message
     */
    @Override
    public void commonMessageOutput(String messageId, String key, String data) {
        String topicName = messageMappingConfig.getTopic(messageId);
        if (topicName == null)
            throw new IllegalArgumentException("Unknown topic configuration");

        String binding = topicMappingConfig.getBinding(topicName);
        if (binding == null)
            throw new IllegalArgumentException("Unknown binding configuration");

        Message<?> message = MessageBuilder.withPayload(data)
                .setHeader(KafkaHeaders.MESSAGE_KEY, key)
                .build();

        streamBridge.send(binding, message);
    }

    @Bean
    public Consumer<Message<String>> saveRiskResponseMessageInput() {
        return x -> {
            runProcess(x, () -> {
                Map<String, Object> variables = new HashMap<>();
                ObjectValue jsonData = Variables.objectValue(x.getPayload()).serializationDataFormat("application/json").create();
                variables.put("payload", jsonData);

                return new Process("amlRiskSaveResult",
                        false,
                        variables);
            });
        };
    }

    @Bean
    public Consumer<Message<String>> savePipelineResponsePaymentMessageInput() {
        return x -> {
            runProcess(x, () -> {
                Map<String, Object> variables = new HashMap<>();
                ObjectValue jsonData = Variables.objectValue(x.getPayload()).serializationDataFormat("application/json").create();
                variables.put("payload", jsonData);

                return new Process("amlPaymentCasePostBatch",
                        false,
                        variables);
            });
        };
    }

    private String getHeaderData(Object value) {
        if (value == null)
            return null;

        if (String.class.isAssignableFrom(value.getClass()))
            return (String) value;

        if (byte[].class.isAssignableFrom(value.getClass()))
            return new String((byte[]) value, StandardCharsets.UTF_8);

        return null;
    }

    private void runProcess(Message<String> message, Supplier<Process> function) {
        try {
            String key = message.getHeaders().get(KafkaHeaders.RECEIVED_MESSAGE_KEY, String.class);
            String topic = message.getHeaders().get(KafkaHeaders.RECEIVED_TOPIC, String.class);

            log.info("Input message from: {}, key={}", topic, key);

            Process process = function.get();
            try {
                if(process.isSkip()) {
                    log.info("Skip run: {}", process.getName());
                    return;
                }

                log.info("Start process: {}", process.getName());
                String id = bpmService.startProcess(process.getName(), key, process.getVariables());
                log.info("Process success: {}, id={}", process.getName(), id);
            } catch (Exception ex) {
                log.error("Failed run process: {}", process.getName(), ex);
                sendError(process.getName(), key, ex);
            }
        } catch (Exception ex) {
            sendError("", "", ex);
        }
    }

    private void sendError(String processName, String key, Exception ex) {
        if (!(properties.getErrorTopic() != null && !properties.getErrorTopic().isEmpty())) {
            return;
        }

        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);

            ErrorDto errorDto = new ErrorDto();
            errorDto.setProcessName(processName);
            errorDto.setKey(key);
            errorDto.setDescription(ex.toString());
            errorDto.setStackTrace(sw.toString());

            String errorKey = LocalDateTime.now() + "_" + processName;

            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = ow.writeValueAsString(errorDto);
            Message<?> message = MessageBuilder.withPayload(json)
                    .setHeader(KafkaHeaders.MESSAGE_KEY, errorKey)
                    .build();

            streamBridge.send(properties.getErrorTopic(), message);
        } catch (Exception sendEx) {
            log.warn("Failed send error: {}", sendEx.getMessage(), sendEx);
        }
    }

    private PipelineResponse getPipelineResponse(String data) {

        PipelineResponse response = new PipelineResponse();

        try {
            JsonNode root = objectMapper.readTree(data);
            if (root.has("runEventResponse")) {
                JsonNode runEventResponse = root.get("runEventResponse");
                if (runEventResponse.has("workflowId")) {
                    response.setWorkflowId(runEventResponse.get("workflowId").textValue());
                    if(this.properties.getPipeline().getMapping() != null) {
                        this.properties.getPipeline().getMapping().entrySet()
                                .stream().filter(x -> x.getValue().equals(response.getWorkflowId())).findFirst().ifPresent(mapping -> {
                            response.setMappingName(mapping.getKey());
                        });
                    }
                }
                if (runEventResponse.has("status")) {
                    response.setStatus(runEventResponse.get("status").textValue());
                }
                if (runEventResponse.has("error")) {

                    JsonNode error = runEventResponse.get("error");
                    if (error.has("message")) {
                        response.setErrorMessage(error.get("message").textValue());
                    }
                }
            }
        } catch (JsonProcessingException ex) {
            throw new RuntimeException("Failed parse pipeline response", ex);
        }

        return response;
    }

    @Getter
    @AllArgsConstructor
    public static class Process {
        private String name;
        private boolean skip;
        private Map<String, Object> variables;
    }
}
