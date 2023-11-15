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

import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_23;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_328;

@Component
@RequiredArgsConstructor
public class FesAutoSaveBeneficiaryDelegate implements JavaDelegate {

    private final FesService fesService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        var client = (Client) execution.getVariable("client");
        var fesParticipant = (FesParticipant) execution.getVariable("fesParticipant");

        var relationType = fesService.getBd(DICTIONARY_23,"1");

        BaseDictionary beneficiaryType = checkBeneficiaryType(client, relationType);

        FesBeneficiary fesBeneficiary = fesService.addBeneficiary(fesParticipant, beneficiaryType);
        Client beneficiary = fesService.findRelation(client, relationType);

        if (beneficiary == null || beneficiary.equals(client)) {
            beneficiary = client;
        }
        fesService.addParticipantIndividualGeneric(null, fesBeneficiary, null, beneficiary);
    }

    private BaseDictionary checkBeneficiaryType(Client client, BaseDictionary relationType) {
        Optional<ClientRelation> clientRelation = client.getClientRelationToList().stream()
                .filter(p -> p.getRelationType().equals(relationType))
                .findFirst();
        BaseDictionary beneficiaryType = fesService.getBd(DICTIONARY_328,"21");
        if (clientRelation.isPresent()) {
            ClientRelation cr = clientRelation.get();
            if (cr.getFromClientId() != null) {
                if (!cr.getFromClientId().equals(cr.getToClientId())
                        && (client.getIseio() == null || !client.getIseio())) {
                    beneficiaryType = fesService.getBd(DICTIONARY_328, "11");
                } else if (!cr.getFromClientId().equals(cr.getToClientId())
                        && client.getIseio()) {
                    beneficiaryType = fesService.getBd(DICTIONARY_328, "12");
                } else {
                    beneficiaryType = fesService.getBd(DICTIONARY_328, "23");
                }
            }
        }
        return beneficiaryType;
    }
}
