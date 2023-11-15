package com.cognive.projects.casernkb.model.fes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.prime.db.rnkb.model.BaseDictionary;
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
    BaseDictionary documentTypeCode;
    String otherDocumentName;
    String documentSeries;
    String documentNum;
    BaseDictionary identityDocumentType;
    List<FesIdentityDocumentDto> fesIdentityDocuments;
    List<FesRightOfResidenceDocumentDto> fesRightOfResidenceDocuments;
}