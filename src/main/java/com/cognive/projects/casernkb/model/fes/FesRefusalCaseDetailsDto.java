package com.cognive.projects.casernkb.model.fes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.prime.db.rnkb.model.fes.FesRefusalCaseDetails}
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FesRefusalCaseDetailsDto implements Serializable {
    Long id;
    Long categoryId;
    Long rejectTypeId;
    Long bankInfFeatureId;
    Long groundOfRefusalId;
    LocalDateTime refusalDate;
    String comment;
    String removalReason;
}