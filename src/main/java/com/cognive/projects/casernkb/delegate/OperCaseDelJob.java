package com.cognive.projects.casernkb.delegate;


import com.cognive.projects.casernkb.model.RuleModelJob;
import com.cognive.projects.casernkb.model.RuleResultCO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OperCaseDelJob implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = (String) delegateExecution.getVariable("operation");
        RuleModelJob operationModel = mapper.readValue(json,RuleModelJob.class);
        List<RuleResultCO> operation = operationModel.getOperation();
        delegateExecution.setVariable("sizeOfOper",operation.size()-1);
        delegateExecution.setVariable("operCase", operation);
    }
}
