package com.cognive.projects.casernkb.model.fes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.prime.db.rnkb.model.BaseDictionary;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link com.prime.db.rnkb.model.fes.FesCasesStatus}
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FesCasesStatusDto implements Serializable {
    Long id;
    Long categoryIdId;
    BaseDictionary caseStatus;
    BaseDictionary caseCondition;
    List<FesMainPageNewDto> fesMainPageNews;
    List<FesMainPageOtherSectionsDto> fesMainPageOtherSections;
}