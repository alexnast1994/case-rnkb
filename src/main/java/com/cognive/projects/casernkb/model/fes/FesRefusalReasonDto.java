package com.cognive.projects.casernkb.model.fes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * DTO for {@link com.prime.db.rnkb.model.fes.FesRefusalReason}
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FesRefusalReasonDto implements Serializable {
    Long id;
    Long categoryId;
    Long refusalReasonId;
    String refusalReasonOthername;
}