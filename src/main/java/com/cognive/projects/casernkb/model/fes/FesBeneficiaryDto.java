package com.cognive.projects.casernkb.model.fes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * DTO for {@link com.prime.db.rnkb.model.fes.FesBeneficiary}
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FesBeneficiaryDto implements Serializable {
    long id;
    long participantId;
    Long beneficiaryTypeId;
    Long beneficiaryResidentFeatureId;
}