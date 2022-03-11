package com.cognive.projects.casernkb.delegate;

import com.cognive.projects.casernkb.service.KafkaService;
import lombok.AllArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class KafkaDelegate implements JavaDelegate {
    private final KafkaService kafkaService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String messageId = (String)delegateExecution.getVariable("messageId");
        String key = (String)delegateExecution.getVariable("key");
        String data = (String)delegateExecution.getVariable("payload");

        if(data == null) {
            data = "test_" + delegateExecution.getCurrentActivityId();
        }

        kafkaService.commonMessageOutput(messageId, key, data);
    }
}
