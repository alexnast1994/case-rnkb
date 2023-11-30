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

import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_322;

@Component
@RequiredArgsConstructor
public class FesAutoSaveIndividualDelegate implements JavaDelegate {

    private final FesService fesService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        FesCategory fesCategory = (FesCategory) execution.getVariable("fesCategory");
        Payment payment = (Payment) execution.getVariable("payment");
        var rejectTypeCode = (String) execution.getVariable("rejectType");
        String clientTypeCode = (String) execution.getVariable("clientTypeCode");
        var client = (Client) execution.getVariable("client");
        var isOperationRejection = (boolean) execution.getVariable("isOperationRejection");

        var participantType = clientTypeCode.equals("5") ?
                fesService.getBd(DICTIONARY_322, "3") :
                fesService.getBd(DICTIONARY_322, "2");

        FesParticipant fesParticipant = fesService.addParticipant(fesCategory, participantType, client.getIsResidentRus(), rejectTypeCode);
        if (isOperationRejection) {
            fesService.addFesCashMoneyTransfers(fesParticipant, payment);
        }
        fesService.addParticipantIndividualGeneric(fesParticipant, null, null, client);

        execution.setVariable("fesParticipant", fesParticipant);
    }
}
