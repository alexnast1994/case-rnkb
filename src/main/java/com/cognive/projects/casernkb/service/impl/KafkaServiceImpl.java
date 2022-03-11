package com.cognive.projects.casernkb.service.impl;

import com.cognive.projects.casernkb.config.MessageMappingConfig;
import com.cognive.projects.casernkb.service.BPMProcessService;
import com.cognive.projects.casernkb.service.KafkaService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@AllArgsConstructor
@Slf4j
@Component
public class KafkaServiceImpl implements KafkaService {

    private final MessageMappingConfig topicMappingConfig;
    private final BPMProcessService bpmService;
    private final StreamBridge streamBridge;

    @Bean
    public Consumer<Message<String>> commonMessageInput(){
        return x-> {
            String key = x.getHeaders().get(KafkaHeaders.RECEIVED_MESSAGE_KEY, String.class);
            String topic = x.getHeaders().get(KafkaHeaders.RECEIVED_TOPIC, String.class);
            log.info("Kafka message key={}, from: {}", key, topic);

            String messageId = topicMappingConfig.getBinding(topic);
            if(messageId == null)
                throw new IllegalArgumentException("Unknown topic configuration");

            bpmService.message(messageId, x.getPayload());
        };
    }

    @Override
    public void commonMessageOutput(String messageId, String key, String data) {
        if(!topicMappingConfig.getBindings().containsKey(messageId))
            throw new IllegalArgumentException("Unknown binding configuration");

        Message<?> message = MessageBuilder.withPayload(data)
                .setHeader(KafkaHeaders.MESSAGE_KEY, key)
                .build();

        streamBridge.send(messageId, message);
    }
}
