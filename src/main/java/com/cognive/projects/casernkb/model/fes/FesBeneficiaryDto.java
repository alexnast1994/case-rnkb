package com.cognive.projects.casernkb.model.fes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.prime.db.rnkb.model.BaseDictionary;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link com.prime.db.rnkb.model.fes.FesBeneficiary}
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FesBeneficiaryDto implements Serializable {
    Long id;
    Long participantId;
    BaseDictionary beneficiaryType;
    BaseDictionary beneficiaryResidentFeature;
    List<FesAddressDto> fesAddresses;
    List<FesParticipantIndividualDto> fesParticipantIndividuals;
}