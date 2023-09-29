package com.cognive.projects.casernkb.model.fes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link com.prime.db.rnkb.model.fes.FesIdentityDocumentGeneral}
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FesIdentityDocumentGeneralDto implements Serializable {
    Long id;
    Long participantIndividualId;
    Long documentTypeCodeId;
    String otherDocumentName;
    String documentSeries;
    String documentNum;
    Long identityDocumentTypeId;
    List<FesIdentityDocumentDto> fesIdentityDocuments;
    List<FesRightOfResidenceDocumentDto> fesRightOfResidenceDocuments;
}