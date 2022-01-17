package com.cognive.projects.casernkb.delegate;


import com.cognive.projects.casernkb.model.RuleResultCO;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OperCaseDel implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        List<RuleResultCO> operation = (List<RuleResultCO>) delegateExecution.getVariable("operation");
        delegateExecution.setVariable("sizeOfOper",operation.size()-1);
        delegateExecution.setVariable("operCase",operation);
    }
}
