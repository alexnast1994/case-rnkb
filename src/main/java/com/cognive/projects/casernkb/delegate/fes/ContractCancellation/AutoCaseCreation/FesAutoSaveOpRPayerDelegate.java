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

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class FesAutoSaveOpRPayerDelegate implements JavaDelegate {

    private final FesService fesService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        FesCategory fesCategory = (FesCategory) execution.getVariable("fesCategory");
        Payment payment = (Payment) execution.getVariable("payment");
        var client = (Client) execution.getVariable("client");
        var isPayer = true;
        if (client != null) {
            var participantStatus = client.getClientMark() != null &&
                    Objects.equals(client.getClientMark().getCode(), "1") ? "1" : "2";

            FesParticipant fesParticipant = fesService.saveFesParticipantOp(fesCategory, client, participantStatus);
            fesService.addFesCashMoneyTransfers(fesParticipant, payment, isPayer);

            fesService.addParticipantChild(client, fesParticipant);

            execution.setVariable("fesParticipant", fesParticipant);
            execution.setVariable("client", client);
        }
    }
}
