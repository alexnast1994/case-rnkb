package com.cognive.projects.casernkb.model.fes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link com.prime.db.rnkb.model.fes.FesCategory}
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FesCategoryDto implements Serializable {
    Long id;
    Long categoryId;
    List<FesGeneralInformationDto> fesGeneralInformations;
    List<FesServiceInformationDto> fesServiceInformations;
    List<FesBankInformationDto> fesBankInformations;
    List<FesRefusalReasonDto> fesRefusalReasons;
    List<FesRefusalCaseDetailsDto> fesRefusalCaseDetails;
    List<FesParticipantDto> fesParticipants;
    List<FesAddressDto> fesAddresses;
}