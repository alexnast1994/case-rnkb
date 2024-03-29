package com.cognive.projects.casernkb.delegate.fes.ContractCancellation.AutoCaseCreation;

import com.cognive.projects.casernkb.service.FesService;
import com.prime.db.rnkb.model.BaseDictionary;
import com.prime.db.rnkb.model.Client;
import com.prime.db.rnkb.model.fes.FesEio;
import com.prime.db.rnkb.model.fes.FesParticipant;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_23;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_346;

@Component
@RequiredArgsConstructor
public class FesAutoSaveEioDelegate implements JavaDelegate {

    private static final String FES_ADDRESS_OF_REG = "6";
    private static final String FES_ADDRESS_LOCATION = "7";
    private static final String ADDRESS_OF_REG = "5";
    private static final String ADDRESS_LOCATION = "3";

    private final FesService fesService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        var client = (Client) execution.getVariable("client");
        var fesParticipant = (FesParticipant) execution.getVariable("fesParticipant");

        if (client != null) {
            var eioRelationType = fesService.getBd(DICTIONARY_23, "4");

            Client eioClient = fesService.findRelation(client, eioRelationType);
            String eioClientType = null;
            if (eioClient != null) {
                eioClientType = fesService.checkClientType(eioClient);
            }
            BaseDictionary eioType = getEioType(eioClientType);
            FesEio fesEio = fesService.addEio(fesParticipant, eioType);

            // Добавляем анкету EIO, если известен тип клиента
            if (eioType != null) {
                if (eioType.getCode().equals("2")) {
                    fesService.addParticipantIndividualGeneric(null, null, fesEio, eioClient);
                } else if (eioType.getCode().equals("1")) {
                    fesService.addParticipantLegal(null, fesEio, eioClient);
                    fesService.findEioAddressAndAdd(fesEio, eioClient, ADDRESS_OF_REG, FES_ADDRESS_OF_REG);
                    fesService.findEioAddressAndAdd(fesEio, eioClient, ADDRESS_LOCATION, FES_ADDRESS_LOCATION);
                }
            }
        }
    }

    private BaseDictionary getEioType(String eioClientType) {
        if (eioClientType == null) {
            return null;
        }
        if (eioClientType.equals("Individual")) {
            return fesService.getBd(DICTIONARY_346, "2");
        } else if (eioClientType.equals("Legal")) {
            return fesService.getBd(DICTIONARY_346, "1");
        }
        return null;
    }
}
