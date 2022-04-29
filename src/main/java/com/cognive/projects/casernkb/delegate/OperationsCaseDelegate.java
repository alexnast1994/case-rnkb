package com.cognive.projects.casernkb.delegate;


import com.cognive.projects.casernkb.model.RuleResultCO;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OperationsCaseDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        List<RuleResultCO> operation = (List<RuleResultCO>) delegateExecution.getVariable("operation");
        delegateExecution.setVariable("operationsCase",operation);
    }
}
