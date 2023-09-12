package com.cognive.projects.casernkb.delegate.fes.ContractCancellation.AutoCaseCreation;

import com.cognive.projects.casernkb.service.FesService;
import com.prime.db.rnkb.model.Address;
import com.prime.db.rnkb.model.Client;
import com.prime.db.rnkb.model.fes.FesCategory;
import com.prime.db.rnkb.model.fes.FesParticipant;
import com.prime.db.rnkb.repository.BaseDictionaryRepository;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FesAutoSaveLegalDelegate implements JavaDelegate {

    public static final String FES_ADDRESS_OF_REG = "6";
    public static final String FES_ADDRESS_LOCATION = "7";
    public static final String ADDRESS_OF_REG = "5";
    public static final String ADDRESS_LOCATION = "3";

    private final FesService fesService;

    private final BaseDictionaryRepository baseDictionaryRepository;

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        var fesCategory = (FesCategory) execution.getVariable("fesCategory");
        var client = (Client) execution.getVariable("client");

        Address clientAddressOfReg = fesService.findMainLegalAddress(client.getAddressList(), ADDRESS_OF_REG);
        Address clientAddressLocation = fesService.findMainLegalAddress(client.getAddressList(), ADDRESS_LOCATION);

        var participantType = baseDictionaryRepository.getBaseDictionary("1", 322);
        var addressOfRegType = baseDictionaryRepository.getBaseDictionary(FES_ADDRESS_OF_REG, 331);
        var addressLocationType = baseDictionaryRepository.getBaseDictionary(FES_ADDRESS_LOCATION, 331);

        FesParticipant fesParticipant = fesService.addParticipant(fesCategory, participantType);

        fesService.addParticipantLegal(fesParticipant, null, client);

        fesService.addAddress(fesCategory, null, null, addressOfRegType, clientAddressOfReg);
        fesService.addAddress(fesCategory, null, null, addressLocationType, clientAddressLocation);
    }
}
