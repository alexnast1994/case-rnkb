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
public class FesAutoSaveOpRPayeeDelegate implements JavaDelegate {

    private final FesService fesService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        FesCategory fesCategory = (FesCategory) execution.getVariable("fesCategory");
        Payment payment = (Payment) execution.getVariable("payment");
        var rejectTypeCode = (String) execution.getVariable("rejectType");
        var client = (Client) execution.getVariable("clientPayee");
        var isPayer = false;
        if (client != null) {
            var participantType = fesService.getParticipantType(client.getClientType());

            FesParticipant fesParticipant = fesService.addParticipant(fesCategory, participantType, client.getIsResidentRus(), rejectTypeCode);
            fesService.addFesCashMoneyTransfers(fesParticipant, payment, isPayer);

            fesService.addParticipantChild(client, fesParticipant);

            execution.setVariable("fesParticipant", fesParticipant);
        }
    }
}
