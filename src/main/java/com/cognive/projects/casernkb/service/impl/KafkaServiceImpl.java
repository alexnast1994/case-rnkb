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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.ObjectValue;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaServiceImpl implements KafkaService {

    private final BindingMappingConfig topicMappingConfig;

    private final MessageMappingConfig messageMappingConfig;

    private final BPMProcessService bpmService;

    private final StreamBridge streamBridge;

    private final KafkaServerProperties properties;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Bean
    public Consumer<Message<String>> commonMessageInput() {
        return x -> {
            String key = x.getHeaders().get(KafkaHeaders.RECEIVED_MESSAGE_KEY, String.class);
            String topic = x.getHeaders().get(KafkaHeaders.RECEIVED_TOPIC, String.class);
            String amlHeader = getHeaderData(x.getHeaders().get(properties.getProcessNameHeader()));

            runWithError(amlHeader, key, () -> {
                if (amlHeader == null) {
                    log.warn("Input message with null processId header, key={}, topic={}. Skip message", key, topic);
                    return;
                }

                log.info("Kafka message key={}, from={}, processId={}", key, topic, amlHeader);

                Map<String, Object> variables = new HashMap<>();

                // Store value as json, prevent Camunda String limitation (4000 and 2000 for Oracle)
                ObjectValue jsonData = Variables.objectValue(x.getPayload()).serializationDataFormat("application/json").create();
                variables.put("payload", jsonData);

                String id = bpmService.startProcess(amlHeader, key, variables);
                log.debug("Process started: {}", id);
            });
        };
    }

    @Bean
    public Consumer<Message<String>> csmMessageInput() {
        return x -> {
            String key = x.getHeaders().get(KafkaHeaders.RECEIVED_MESSAGE_KEY, String.class);
            String topic = x.getHeaders().get(KafkaHeaders.RECEIVED_TOPIC, String.class);
            String operationHeader = getHeaderData(x.getHeaders().get(properties.getCsmProcessNameHeader()));

            runWithError(properties.getCsmProcessName(), key, () -> {
                if (operationHeader == null) {
                    log.warn("Input message with null operationHeader header, key={}, topic={}. Skip message", key, topic);
                    return;
                }

                log.info("Kafka csm message key={}, from={}, header={}", key, topic, operationHeader);
                if (!operationHeader.equals(properties.getCsmProcessName())) {
                    log.info("Skip operation: {}", operationHeader);
                    return;
                }

                Map<String, Object> variables = new HashMap<>();

                // Store value as json, prevent Camunda String limitation (4000 and 2000 for Oracle)
                ObjectValue jsonData = Variables.objectValue(x.getPayload()).serializationDataFormat("application/json").create();
                variables.put("payload", jsonData);

                String id = bpmService.startProcess(properties.getCamundaCsmProcessName(), key, variables);
                log.debug("Process started: {}", id);
            });
        };
    }


    @Bean
    public Consumer<Message<String>> csmKycClientMessageInput() {
        return x -> {
            final var key = x.getHeaders().get(KafkaHeaders.RECEIVED_MESSAGE_KEY, String.class);
            final var topic = x.getHeaders().get(KafkaHeaders.RECEIVED_TOPIC, String.class);
            final var onlineHeader = getHeaderData(x.getHeaders().get(properties.getCsmKycOnlineClientProcessName()));
            final var offlineHeader = getHeaderData(x.getHeaders().get(properties.getCsmKycOfflineClientProcessName()));
            final var processName = onlineHeader == null
                    ? properties.getCsmKycOfflineClientProcessName()
                    : properties.getCsmKycOnlineClientProcessName();

            runWithError(processName, key, () -> {
                if ((processName.equals(properties.getCsmKycOnlineClientProcessName()) && onlineHeader == null)
                        || (processName.equals(properties.getCsmKycOfflineClientProcessName()) && offlineHeader == null)) {
                    log.warn("Input message with null header, key={}, topic={}. Skip message", key, topic);
                    return;
                }

                log.info("Kafka csm message key={}, from={}, onlineClientHeader={}, offlineClientHeader={}",
                        key, topic, onlineHeader, offlineHeader);

                final var variables = new HashMap<String, Object>();

                // Store value as json, prevent Camunda String limitation (4000 and 2000 for Oracle)
                final var jsonData = Variables
                        .objectValue(x.getPayload())
                        .serializationDataFormat(MediaType.APPLICATION_JSON_VALUE)
                        .create();

                variables.put("payload", jsonData);
                variables.put("processName", processName); // Determine type of kyc client (online/offline)

                final var id = bpmService.startProcess(properties.getCamundaCsmProcessName(), key, variables);
                log.debug("Process started: {}", id);
            });
        };
    }


    @Bean
    public Consumer<Message<String>> pipelineMessageInput() {
        return x -> {
            String key = x.getHeaders().get(KafkaHeaders.RECEIVED_MESSAGE_KEY, String.class);
            String topic = x.getHeaders().get(KafkaHeaders.RECEIVED_TOPIC, String.class);

            runWithError(properties.getCamundaPipelineProcessName(), key, () -> {
                log.info("Kafka pipeline message key={}, from={}", key, topic);

                PipelineResponse pipelineResponse = getPipelineResponse(x.getPayload());
                log.info("Pipeline workflow id: {}, status: {}", pipelineResponse.getWorkflowId(), pipelineResponse.getStatus());

                if (!pipelineResponse.isWorkflowId(properties.getPipelineWorkflowId())) {
                    return;
                }

                if (!pipelineResponse.isDone()) {
                    throw new RuntimeException("Pipeline failed with error " + pipelineResponse.getErrorMessage());
                }

                Map<String, Object> variables = Collections.emptyMap();
                String id = bpmService.startProcess(properties.getCamundaPipelineProcessName(), key, variables);
                log.debug("Process started: {}", id);
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

    private String getHeaderData(Object value) {
        if (value == null)
            return null;

        if (String.class.isAssignableFrom(value.getClass()))
            return (String) value;

        if (byte[].class.isAssignableFrom(value.getClass()))
            return new String((byte[]) value, StandardCharsets.UTF_8);

        return null;
    }

    private void runWithError(String processName, String key, Runnable runnable) {
        try {
            runnable.run();
        } catch (Exception ex) {
            log.error("Failed run process: {}", processName, ex);
            if (properties.getErrorTopic() != null && !properties.getErrorTopic().isEmpty()) {
                sendError(processName, key, ex);
            }
        }
    }

    private void sendError(String processName, String key, Exception ex) {
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
}
