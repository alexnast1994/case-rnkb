package com.cognive.projects.casernkb.model.fes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.prime.db.rnkb.model.fes.FesParticipantForeignIdentifier}
 */
@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class FesParticipantForeignIdentifierDto implements Serializable {
    long id;
    String foreignNum;
    String foreignCode;
}