package com.cognive.projects.casernkb.model.fes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.prime.db.rnkb.model.fes.FesIdentityDocument}
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FesIdentityDocumentDto implements Serializable {
    long id;
    long identityDocumentGeneralId;
    LocalDateTime issueDate;
    String departmentCode;
    String issuingAgency;
}