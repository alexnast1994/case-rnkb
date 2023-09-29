package com.cognive.projects.casernkb.model.fes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    Long participantCodeId;
    Long participantStatusId;
    Long participantTypeId;
    Long participantResidentFeatureId;
    Long participantFeatureId;
    Long participantFrommuUuid;
    List<FesParticipantIndividualDto> fesParticipantIndividuals;
    List<FesParticipantLegalDto> fesParticipantLegals;
    List<FesParticipantForeignDto> fesParticipantForeigns;
    List<FesBeneficiaryDto> fesBeneficiaryList;
    List<FesEioDto> fesEios;
    List<FesAddressDto> fesAddresses;
}