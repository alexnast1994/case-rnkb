package com.cognive.projects.casernkb.model.fes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.prime.db.rnkb.model.BaseDictionary;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.prime.db.rnkb.model.fes.FesServiceInformation}
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FesServiceInformationDto implements Serializable {
    Long id;
    Long categoryId;
    BaseDictionary informationType;
    String formatVersion;
    String softVersion;
    String correspondentUuid;
    LocalDateTime date;
    String officerPosition;
    String officerLastName;
    String officerFirstName;
    String officerMiddleName;
    String officerFullName;
    String officerPhone;
    String officerMail;
}