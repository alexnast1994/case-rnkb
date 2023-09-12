package com.cognive.projects.casernkb.delegate.fes.ContractCancellation.AutoCaseCreation;

import com.cognive.projects.casernkb.service.FesService;
import com.prime.db.rnkb.model.BaseDictionary;
import com.prime.db.rnkb.model.Client;
import com.prime.db.rnkb.model.ClientRelation;
import com.prime.db.rnkb.model.fes.FesBeneficiary;
import com.prime.db.rnkb.model.fes.FesParticipant;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FesAutoSaveBeneficiaryDelegate implements JavaDelegate {
    private static final Integer DICTIONARY328 = 328;

    private final FesService fesService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        var client = (Client) execution.getVariable("client");
        var fesParticipant = (FesParticipant) execution.getVariable("fesParticipant");

        var relationType = fesService.getBd("1", 23);

        BaseDictionary beneficiaryType = checkBeneficiaryType(client, relationType);

        FesBeneficiary fesBeneficiary = fesService.addBeneficiary(fesParticipant, beneficiaryType);
        Client beneficiary = fesService.findRelation(client, relationType);

        if (beneficiary == null || beneficiary.equals(client)) {
            beneficiary = client;
        }
        fesService.addParticipantIndividualBeneficiary(fesBeneficiary, beneficiary);
    }

    private BaseDictionary checkBeneficiaryType(Client client, BaseDictionary relationType) {
        Optional<ClientRelation> clientRelation = client.getClientRelationToList().stream()
                .filter(p -> p.getRelationType().equals(relationType))
                .findFirst();
        BaseDictionary beneficiaryType = fesService.getBd("21", DICTIONARY328);
        if (clientRelation.isPresent()) {
            ClientRelation cr = clientRelation.get();
            if (cr.getFromClientId() != null) {
                if (!cr.getFromClientId().equals(cr.getToClientId())
                        && (client.getIseio() == null || !client.getIseio())) {
                    beneficiaryType = fesService.getBd("11", DICTIONARY328);
                } else if (!cr.getFromClientId().equals(cr.getToClientId())
                        && client.getIseio()) {
                    beneficiaryType = fesService.getBd("12", DICTIONARY328);
                } else {
                    beneficiaryType = fesService.getBd("23", DICTIONARY328);
                }
            }
        }
        return beneficiaryType;
    }
}
