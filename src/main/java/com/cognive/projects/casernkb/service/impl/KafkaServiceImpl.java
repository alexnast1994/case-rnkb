package com.cognive.projects.casernkb.service.impl;

import com.cognive.projects.casernkb.config.BindingMappingConfig;
import com.cognive.projects.casernkb.config.MessageMappingConfig;
import com.cognive.projects.casernkb.model.ErrorDto;
import com.cognive.projects.casernkb.service.BPMProcessService;
import com.cognive.projects.casernkb.service.KafkaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import liquibase.pro.packaged.S;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.ObjectValue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Slf4j
@Component
public class KafkaServiceImpl implements KafkaService {
    private final BindingMappingConfig topicMappingConfig;
    private final MessageMappingConfig messageMappingConfig;
    private final BPMProcessService bpmService;
    private final StreamBridge streamBridge;

    public KafkaServiceImpl(BindingMappingConfig bindingMappingConfig,
                            MessageMappingConfig messageMappingConfig,
                            BPMProcessService bpmService,
                            StreamBridge streamBridge) {
        this.topicMappingConfig = bindingMappingConfig;
        this.messageMappingConfig = messageMappingConfig;
        this.bpmService = bpmService;
        this.streamBridge = streamBridge;
    }

    @Value("${server.kafka.process-name-header}")
    public String AML_HEADER;

    @Value("${server.kafka.csm-process-name-header}")
    public String CSM_HEADER;

    @Value("${server.kafka.csm-process-name}")
    public String CSM_PROCESS_NAME;

    @Value("${server.kafka.camunda-csm-process-name}")
    public String CAMUNDA_CSM_PROCESS;

    @Value("${server.kafka.error-topic}")
    public String ERROR_TOPIC;

    @Bean
    public Consumer<Message<String>> commonMessageInput(){
        return x-> {
            String key = x.getHeaders().get(KafkaHeaders.RECEIVED_MESSAGE_KEY, String.class);
            String topic = x.getHeaders().get(KafkaHeaders.RECEIVED_TOPIC, String.class);
            String amlHeader = getHeaderData(x.getHeaders().get(AML_HEADER));

            runWithError(amlHeader, key, () -> {
                if(amlHeader == null) {
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
        return x-> {
            String key = x.getHeaders().get(KafkaHeaders.RECEIVED_MESSAGE_KEY, String.class);
            String topic = x.getHeaders().get(KafkaHeaders.RECEIVED_TOPIC, String.class);
            String operationHeader = getHeaderData(x.getHeaders().get(CSM_HEADER));

            runWithError(CSM_PROCESS_NAME, key, () -> {
                if(operationHeader == null) {
                    log.warn("Input message with null operationHeader header, key={}, topic={}. Skip message", key, topic);
                    return;
                }

                log.info("Kafka csm message key={}, from={}, header={}", key, topic, operationHeader);
                if(!operationHeader.equals(CSM_PROCESS_NAME)) {
                    log.info("Skip operation: {}", operationHeader);
                    return;
                }

                Map<String, Object> variables = new HashMap<>();

                // Store value as json, prevent Camunda String limitation (4000 and 2000 for Oracle)
                ObjectValue jsonData = Variables.objectValue(x.getPayload()).serializationDataFormat("application/json").create();
                variables.put("payload", jsonData);

                String id = bpmService.startProcess(CAMUNDA_CSM_PROCESS, key, variables);
                log.debug("Process started: {}", id);
            });
        };
    }

    /**
     * Send message to topic
     * @param messageId - id for message
     * @param key - kafka key
     * @param data - kafka message
     */
    @Override
    public void commonMessageOutput(String messageId, String key, String data) {
        String topicName = messageMappingConfig.getTopic(messageId);
        if(topicName == null)
            throw new IllegalArgumentException("Unknown topic configuration");

        String binding = topicMappingConfig.getBinding(topicName);
        if(binding == null)
            throw new IllegalArgumentException("Unknown binding configuration");

        Message<?> message = MessageBuilder.withPayload(data)
                .setHeader(KafkaHeaders.MESSAGE_KEY, key)
                .build();

        streamBridge.send(binding, message);
    }

    private String getHeaderData(Object value) {
        if(value == null)
            return null;

        if(String.class.isAssignableFrom(value.getClass()))
            return (String)value;

        if(byte[].class.isAssignableFrom(value.getClass()))
            return new String((byte[])value, StandardCharsets.UTF_8);

        return null;
    }

    private void runWithError(String processName, String key, Runnable runnable) {
        try {
            runnable.run();
        } catch (Exception ex) {
            log.error("Failed run process: {}", processName, ex);
            if(ERROR_TOPIC != null && !ERROR_TOPIC.isEmpty()) {
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

            streamBridge.send(ERROR_TOPIC, message);
        } catch (Exception sendEx) {
            log.warn("Failed send error: {}", sendEx.getMessage(), sendEx);
        }
    }
}
