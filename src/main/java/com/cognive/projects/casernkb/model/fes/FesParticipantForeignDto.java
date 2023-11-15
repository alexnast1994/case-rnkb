package com.cognive.projects.casernkb.model.fes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.prime.db.rnkb.model.BaseDictionary;
import lombok.Value;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link com.prime.db.rnkb.model.fes.FesParticipantForeign}
 */
@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class FesParticipantForeignDto implements Serializable {
    Long id;
    Long participantId;
    String participantForeignName;
    BaseDictionary orgFormFeature;
    String founderLastname;
    String founderFirstname;
    String founderMiddlename;
    String founderFullName;
    List<FesParticipantForeignIdentifierDto> fesParticipantForeignIdentifiers;
}