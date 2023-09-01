package com.cognive.projects.casernkb.model.fes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * DTO for {@link com.prime.db.rnkb.model.fes.FesGeneralInformation}
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FesGeneralInformationDto implements Serializable {
    Long id;
    Long categoryId;
    Long recordTypeId;
    String num;
}