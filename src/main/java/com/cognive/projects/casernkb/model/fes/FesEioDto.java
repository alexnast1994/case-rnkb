package com.cognive.projects.casernkb.model.fes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.prime.db.rnkb.model.BaseDictionary;
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
    BaseDictionary eioType;
    BaseDictionary eioResidentFeature;
    List<FesAddressDto> fesAddresses;
    List<FesParticipantIndividualDto> fesParticipantIndividuals;
    List<FesParticipantLegalDto> fesParticipantLegals;
}