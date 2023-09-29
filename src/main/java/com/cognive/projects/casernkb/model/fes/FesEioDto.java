package com.cognive.projects.casernkb.model.fes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link com.prime.db.rnkb.model.fes.FesEio}
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FesEioDto implements Serializable {
    Long id;
    Long participantId;
    Long eioTypeId;
    Long eioResidentFeatureId;
    List<FesAddressDto> fesAddresses;
    List<FesParticipantIndividualDto> fesParticipantIndividuals;
    List<FesParticipantLegalDto> fesParticipantLegals;
}