package com.cognive.projects.casernkb.delegate.fes.ContractCancellation.AutoCaseCreation;

import com.cognive.projects.casernkb.service.FesService;
import com.prime.db.rnkb.model.Client;
import com.prime.db.rnkb.model.fes.FesCategory;
import com.prime.db.rnkb.model.fes.FesParticipant;
import com.prime.db.rnkb.model.fes.FesParticipantForeign;
import com.prime.db.rnkb.model.fes.FesParticipantForeignIdentifier;
import com.prime.db.rnkb.repository.fes.FesParticipantForeignRepository;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FesAutoSaveForeignDelegate implements JavaDelegate {
    private static final String FES_ADDRESS_OF_REG = "10";
    private static final String FES_ADDRESS_LOCATION = "9";
    private static final String ADDRESS_OF_REG = "2";
    private static final String ADDRESS_LOCATION = "9";

    private final FesService fesService;
    private final FesParticipantForeignRepository fesParticipantForeignRepository;

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        var fesCategory = (FesCategory) execution.getVariable("fesCategory");
        var client = (Client) execution.getVariable("client");
        var participantType = fesService.getBd("5", 322);
        var clientLegal = client.getClientLegal();

        FesParticipant fesParticipant = fesService.addParticipant(fesCategory, participantType, client.getIsResidentRus());

        FesParticipantForeign fesParticipantForeign = new FesParticipantForeign();
        fesParticipantForeign.setParticipantId(fesParticipant);
        if (clientLegal != null) {
            fesParticipantForeign.setParticipantForeignName(clientLegal.getLegalname());
        }
        if (fesService.extractPartOfName(client.getFullName(), 0) != null) {
            fesParticipantForeign.setFounderLastname(fesService.extractPartOfName(client.getFullName(), 0));
            fesParticipantForeign.setFounderFirstname(fesService.extractPartOfName(client.getFullName(), 1));
            fesParticipantForeign.setFounderMiddlename(fesService.extractPartOfName(client.getFullName(), 2));
        } else {
            fesParticipantForeign.setFounderFullName(client.getFullName());
        }
        fesParticipantForeign = fesParticipantForeignRepository.save(fesParticipantForeign);

        FesParticipantForeignIdentifier fesParticipantForeignIdentifier = new FesParticipantForeignIdentifier();
        fesParticipantForeignIdentifier.setParticipantForeignId(fesParticipantForeign);
        if (clientLegal != null) {
            fesParticipantForeignIdentifier.setForeignNum(clientLegal.getForeignTaxInNum());
            fesParticipantForeignIdentifier.setForeignCode(clientLegal.getForeignTaxInCode());
        }

        fesService.findForeignAddressAndAdd(fesCategory, client, FES_ADDRESS_LOCATION, ADDRESS_LOCATION);
        fesService.findForeignAddressAndAdd(fesCategory, client, FES_ADDRESS_OF_REG, ADDRESS_OF_REG);
    }
}
