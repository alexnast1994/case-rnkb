package com.cognive.projects.casernkb.model.fes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * DTO for {@link com.prime.db.rnkb.model.fes.FesBranchInformation}
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FesBranchInformationDto implements Serializable {
    Long id;
    String transferringBranchNum;
    String branchNum;
}