package com.cognive.projects.casernkb.delegate;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DebugDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Object logParam = delegateExecution.getVariable("logParam");
        if(logParam != null) {
            log.info("-> {}", logParam);
        }
        delegateExecution.getVariables().forEach((x, y) -> {
            log.info("{} = {}", x, y);
        });
    }
}
