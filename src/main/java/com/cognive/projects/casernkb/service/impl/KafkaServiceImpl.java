package com.cognive.projects.casernkb.service.impl;

import com.cognive.projects.casernkb.config.BindingMappingConfig;
import com.cognive.projects.casernkb.config.MessageMappingConfig;
import com.cognive.projects.casernkb.service.BPMProcessService;
import com.cognive.projects.casernkb.service.KafkaService;
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

import java.nio.charset.StandardCharsets;
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

    @Value("${server.process-name-header}")
    public String AML_HEADER;

    @Value("${server.csm-process-name-header}")
    public String CSM_HEADER;

    @Value("${server.csm-process-name}")
    public String CSM_PROCESS_NAME;

    @Value("${server.camunda-csm-process-name}")
    public String CAMUNDA_CSM_PROCESS;

    @Bean
    public Consumer<Message<String>> commonMessageInput(){
        return x-> {
            String key = x.getHeaders().get(KafkaHeaders.RECEIVED_MESSAGE_KEY, String.class);
            String topic = x.getHeaders().get(KafkaHeaders.RECEIVED_TOPIC, String.class);
            String amlHeader = getHeaderData(x.getHeaders().get(AML_HEADER));

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
        };
    }

    @Bean
    public Consumer<Message<String>> csmMessageInput() {
        return x-> {
            String key = x.getHeaders().get(KafkaHeaders.RECEIVED_MESSAGE_KEY, String.class);
            String topic = x.getHeaders().get(KafkaHeaders.RECEIVED_TOPIC, String.class);
            String operationHeader = getHeaderData(x.getHeaders().get(CSM_HEADER));

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
}
