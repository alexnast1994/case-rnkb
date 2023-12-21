package com.cognive.projects.casernkb.model.fes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.prime.db.rnkb.model.BaseDictionary;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.prime.db.rnkb.model.fes.FesOperationsReason}
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FesOperationsReasonDto implements Serializable {
    Long id;
    Long categoryId;
    BaseDictionary docType;
    String docOtherName;
    LocalDateTime docDate;
    String docNum;
    String summary;
}