package com.cognive.projects.casernkb.model.fes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.prime.db.rnkb.model.BaseDictionary;
import lombok.Data;

import java.io.Serializable;

/**
 * DTO for {@link com.prime.db.rnkb.model.fes.FesRefusalOperation}
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FesRefusalOperationDto implements Serializable {
    Long id;
    Long refusalCaseDetailsIdId;
    BaseDictionary cashFeature;
    String refusalOperationCharacteristic;
}