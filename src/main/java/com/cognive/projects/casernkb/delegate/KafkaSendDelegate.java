package com.cognive.projects.casernkb.delegate;

import com.cognive.projects.casernkb.service.KafkaService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class KafkaSendDelegate implements JavaDelegate {

    private final KafkaService kafkaService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        String message = (String)delegateExecution.getVariable("message");
        String topic = (String)delegateExecution.getVariable("topic");
        log.info("topic " + topic);
        String key = (String)delegateExecution.getVariable("key");
        kafkaService.sendSimpleMessage(topic, message, key);

    }
}
