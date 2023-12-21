package com.cognive.projects.casernkb.model.fes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.prime.db.rnkb.model.BaseDictionary;
import lombok.Data;

import java.io.Serializable;

/**
 * DTO for {@link com.prime.db.rnkb.model.fes.FesTerrorismFinancing}
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FesTerrorismFinancingDto implements Serializable {
    Long id;
    Long categoryIdId;
    BaseDictionary terrorismFinancingType;
}