package com.cognive.projects.casernkb.model.fes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link com.prime.db.rnkb.model.fes.FesBankInformation}
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FesBankInformationDto implements Serializable {
    Long id;
    Long categoryId;
    Boolean reportingAttribute;
    String bankRegNum;
    String bankBic;
    String bankOcato;
    List<FesBranchInformationDto> fesBranchInformations;
}