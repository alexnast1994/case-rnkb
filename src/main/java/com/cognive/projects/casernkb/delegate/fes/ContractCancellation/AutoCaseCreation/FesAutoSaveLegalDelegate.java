package com.cognive.projects.casernkb.delegate.fes.ContractCancellation.AutoCaseCreation;

import com.cognive.projects.casernkb.service.FesService;
import com.prime.db.rnkb.model.Address;
import com.prime.db.rnkb.model.Client;
import com.prime.db.rnkb.model.fes.FesCategory;
import com.prime.db.rnkb.model.fes.FesParticipant;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_322;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_331;

@Component
@RequiredArgsConstructor
public class FesAutoSaveLegalDelegate implements JavaDelegate {

    public static final String FES_ADDRESS_OF_REG = "6";
    public static final String FES_ADDRESS_LOCATION = "7";
    public static final String ADDRESS_OF_REG = "5";
    public static final String ADDRESS_LOCATION = "3";

    private final FesService fesService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        var fesCategory = (FesCategory) execution.getVariable("fesCategory");
        var rejectTypeCode = (String) execution.getVariable("rejectType");
        var client = (Client) execution.getVariable("client");

        Address clientAddressOfReg = fesService.findMainLegalAddress(client.getAddressList(), ADDRESS_OF_REG);
        Address clientAddressLocation = fesService.findMainLegalAddress(client.getAddressList(), ADDRESS_LOCATION);

        var participantType = fesService.getBd(DICTIONARY_322, "1");
        var addressOfRegType = fesService.getBd(DICTIONARY_331, FES_ADDRESS_OF_REG);
        var addressLocationType = fesService.getBd(DICTIONARY_331, FES_ADDRESS_LOCATION);

        FesParticipant fesParticipant = fesService.addParticipant(fesCategory, participantType, client.getIsResidentRus(), rejectTypeCode);

        fesService.addParticipantLegal(fesParticipant, null, client);

        fesService.addAddress(null, fesParticipant, null, null, addressOfRegType, clientAddressOfReg);
        fesService.addAddress(null, fesParticipant, null, null, addressLocationType, clientAddressLocation);

        execution.setVariable("fesParticipant", fesParticipant);
    }
}
