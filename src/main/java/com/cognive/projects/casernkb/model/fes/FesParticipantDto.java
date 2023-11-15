package com.cognive.projects.casernkb.model.fes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.prime.db.rnkb.model.BaseDictionary;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link com.prime.db.rnkb.model.fes.FesParticipant}
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FesParticipantDto implements Serializable {
    Long id;
    Long categoryId;
    BaseDictionary participantCode;
    BaseDictionary participantStatus;
    BaseDictionary participantType;
    BaseDictionary participantResidentFeature;
    BaseDictionary participantFeature;
    Long participantFrommuUuid;
    List<FesParticipantIndividualDto> fesParticipantIndividuals;
    List<FesParticipantLegalDto> fesParticipantLegals;
    List<FesParticipantForeignDto> fesParticipantForeigns;
    List<FesBeneficiaryDto> fesBeneficiaryList;
    List<FesEioDto> fesEios;
    List<FesAddressDto> fesAddresses;
}