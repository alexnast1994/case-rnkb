package com.cognive.projects.casernkb.model.fes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.prime.db.rnkb.model.fes.FesRightOfResidenceDocument}
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FesRightOfResidenceDocumentDto implements Serializable {
    Long id;
    Long identityDocumentGeneralId;
    LocalDateTime startStayDate;
    LocalDateTime endStayDate;
}