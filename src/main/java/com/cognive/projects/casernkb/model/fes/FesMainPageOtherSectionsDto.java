package com.cognive.projects.casernkb.model.fes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.prime.db.rnkb.model.BaseDictionary;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.prime.db.rnkb.model.fes.FesMainPageOtherSections}
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FesMainPageOtherSectionsDto implements Serializable {
    Long id;
    Long casesStatusIdId;
    Long responsibleUserId;
    BaseDictionary uoResponse;
    String xmlFileName;
    LocalDateTime shippingDate;
    LocalDateTime receiptDate;
    String receiptName;
    String comment;
    String shippingContainerName;
    String xmlPath;
    String receiptPath;
    String shippingContainerPath;
    Boolean validateHasErrors;
    String protocolErrorsCount;
    LocalDateTime messageDate;
    Boolean flkHasErrors;
}