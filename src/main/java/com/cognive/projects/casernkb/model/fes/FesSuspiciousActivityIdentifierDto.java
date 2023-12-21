package com.cognive.projects.casernkb.model.fes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * DTO for {@link com.prime.db.rnkb.model.fes.FesSuspiciousActivityIdentifier}
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FesSuspiciousActivityIdentifierDto implements Serializable {
    Long id;
    Long categoryId;
    String suspiciousActivityIdentifier;
    String inn;
    String foreignNum;
}