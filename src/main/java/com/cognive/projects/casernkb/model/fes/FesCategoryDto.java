package com.cognive.projects.casernkb.model.fes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.prime.db.rnkb.model.BaseDictionary;
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
    BaseDictionary category;
    List<FesGeneralInformationDto> fesGeneralInformations;
    List<FesServiceInformationDto> fesServiceInformations;
    List<FesBankInformationDto> fesBankInformations;
    List<FesRefusalReasonDto> fesRefusalReasons;
    List<FesRefusalCaseDetailsDto> fesRefusalCaseDetails;
    List<FesParticipantDto> fesParticipants;
    List<FesAddressDto> fesAddresses;
    List<FesFreezingAppliedMeasuresDto> fesFreezingAppliedMeasures;
    List<FesOperationInformationDto> fesOperationInformations;
    List<FesOperationsReasonDto> fesOperationsReason;
    List<FesSuspiciousActivityIdentifierDto> fesSuspiciousActivityIdentifiers;
    List<FesUnusualOperationFeatureDto> fesUnusualOperationFeatures;
    List<FesCasesStatusDto> fesCasesStatuses;
    List<FesOperationsDetailsDto> fesOperationsDetails;
    List<FesPreciousMetalDataDto> fesPreciousMetalData;
    List<FesAdditionalOperationDto> fesAdditionalOperations;
    List<FesMoneyTransfersDto> fesMoneyTransfersList;
    List<FesCashMoneyTransfersDto> fesCashMoneyTransfers;
    List<FesInspectionDetailsDto> fesInspectionDetails;
    List<FesMoneyPlaceDto> fesMoneyPlaces;

}