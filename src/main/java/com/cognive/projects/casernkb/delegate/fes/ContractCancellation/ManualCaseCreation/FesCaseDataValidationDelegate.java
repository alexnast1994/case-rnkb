package com.cognive.projects.casernkb.delegate.fes.ContractCancellation.ManualCaseCreation;

import com.cognive.projects.casernkb.model.fes.FesCaseSaveDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
public class FesCaseDataValidationDelegate implements JavaDelegate {

    private final ObjectMapper objectMapper;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        var jsonData = delegateExecution.getVariable("payload");

        FesCaseSaveDto fesCaseSaveDto = objectMapper.convertValue(jsonData, FesCaseSaveDto.class);
        boolean isCaseNew = fesCaseSaveDto.getFesCategory().getId() == null;
        if (!isCaseNew) {
            delegateExecution.setVariable("categoryId", fesCaseSaveDto.getFesCategory().getId());
        }

        delegateExecution.setVariable("isCaseNew", isCaseNew);
    }
}
