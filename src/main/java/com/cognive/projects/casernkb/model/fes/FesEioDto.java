package com.cognive.projects.casernkb.model.fes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * DTO for {@link com.prime.db.rnkb.model.fes.FesEio}
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FesEioDto implements Serializable {
    long id;
    long participantId;
    Long eioTypeId;
    Long eioResidentFeatureId;
}