package com.cognive.projects.casernkb.delegate.fes.ContractCancellation.AutoCaseCreation;

import com.cognive.projects.casernkb.service.FesService;
import com.prime.db.rnkb.model.Client;
import com.prime.db.rnkb.model.Payment;
import com.prime.db.rnkb.model.fes.FesCategory;
import com.prime.db.rnkb.model.fes.FesParticipant;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FesAutoSaveOpPayerParticipantsDelegate implements JavaDelegate {

    private final FesService fesService;
    @Override
    public void execute(DelegateExecution execution) throws Exception {

        FesCategory fesCategory = (FesCategory) execution.getVariable("fesCategory");
        Payment payment = (Payment) execution.getVariable("payment");

        Client client = payment.getPayerClientId();
        FesParticipant fesParticipant = null;
        if (client != null) {
            fesParticipant = fesService.saveFesParticipantOp(fesCategory, client, "3");
            fesService.addParticipantChild(client, fesParticipant);
        }
        execution.setVariable("fesParticipant", fesParticipant);
        execution.setVariable("client", client);
    }
}
