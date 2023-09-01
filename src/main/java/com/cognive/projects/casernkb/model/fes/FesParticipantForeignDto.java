package com.cognive.projects.casernkb.model.fes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Value;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link com.prime.db.rnkb.model.fes.FesParticipantForeign}
 */
@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class FesParticipantForeignDto implements Serializable {
    long id;
    long participantId;
    String participantForeignName;
    Long orgFormFeatureId;
    String founderLastname;
    String founderFirstname;
    String founderMiddlename;
    String founderFullName;
    List<FesParticipantForeignIdentifierDto> fesParticipantForeignIdentifiers;
}