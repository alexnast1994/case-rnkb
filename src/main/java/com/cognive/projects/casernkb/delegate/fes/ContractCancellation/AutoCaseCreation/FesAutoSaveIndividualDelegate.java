package com.cognive.projects.casernkb.delegate.fes.ContractCancellation.AutoCaseCreation;

import com.cognive.projects.casernkb.service.FesService;
import com.prime.db.rnkb.model.Client;
import com.prime.db.rnkb.model.fes.FesCategory;
import com.prime.db.rnkb.model.fes.FesParticipant;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FesAutoSaveIndividualDelegate implements JavaDelegate {

    private final FesService fesService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        FesCategory fesCategory = (FesCategory) execution.getVariable("fesCategory");
        String clientTypeCode = (String) execution.getVariable("clientTypeCode");
        var client = (Client) execution.getVariable("client");

        var participantType = clientTypeCode.equals("5") ?
                fesService.getBd("3", 322) :
                fesService.getBd("2", 322);

        FesParticipant fesParticipant = fesService.addParticipant(fesCategory, participantType, client.getIsResidentRus());
        fesService.addParticipantIndividual(fesCategory, fesParticipant, client);

        execution.setVariable("fesParticipant", fesParticipant);
    }
}
