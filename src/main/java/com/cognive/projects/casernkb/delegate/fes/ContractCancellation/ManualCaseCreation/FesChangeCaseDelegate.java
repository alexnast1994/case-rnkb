package com.cognive.projects.casernkb.delegate.fes.ContractCancellation.ManualCaseCreation;

import com.cognive.projects.casernkb.model.fes.FesAdditionalOperationDto;
import com.cognive.projects.casernkb.model.fes.FesAddressDto;
import com.cognive.projects.casernkb.model.fes.FesBankInformationDto;
import com.cognive.projects.casernkb.model.fes.FesBeneficiaryDto;
import com.cognive.projects.casernkb.model.fes.FesBranchInformationDto;
import com.cognive.projects.casernkb.model.fes.FesCaseSaveDto;
import com.cognive.projects.casernkb.model.fes.FesCashMoneyTransfersDto;
import com.cognive.projects.casernkb.model.fes.FesEioDto;
import com.cognive.projects.casernkb.model.fes.FesForeignCardTransactionsDto;
import com.cognive.projects.casernkb.model.fes.FesFreezingAppliedMeasuresDto;
import com.cognive.projects.casernkb.model.fes.FesIdentityDocumentDto;
import com.cognive.projects.casernkb.model.fes.FesIdentityDocumentGeneralDto;
import com.cognive.projects.casernkb.model.fes.FesInspectionDetailsDto;
import com.cognive.projects.casernkb.model.fes.FesMainPageNewDto;
import com.cognive.projects.casernkb.model.fes.FesMoneyPlaceDto;
import com.cognive.projects.casernkb.model.fes.FesMoneyTransfersDto;
import com.cognive.projects.casernkb.model.fes.FesOperationInformationDto;
import com.cognive.projects.casernkb.model.fes.FesOperationsDetailsDto;
import com.cognive.projects.casernkb.model.fes.FesOperationsReasonDto;
import com.cognive.projects.casernkb.model.fes.FesParticipantDto;
import com.cognive.projects.casernkb.model.fes.FesParticipantForeignDto;
import com.cognive.projects.casernkb.model.fes.FesParticipantForeignIdentifierDto;
import com.cognive.projects.casernkb.model.fes.FesParticipantIndividualDto;
import com.cognive.projects.casernkb.model.fes.FesParticipantLegalDto;
import com.cognive.projects.casernkb.model.fes.FesPreciousMetalDataDto;
import com.cognive.projects.casernkb.model.fes.FesRefusalCaseDetailsDto;
import com.cognive.projects.casernkb.model.fes.FesRefusalOperationDto;
import com.cognive.projects.casernkb.model.fes.FesRefusalReasonDto;
import com.cognive.projects.casernkb.model.fes.FesRightOfResidenceDocumentDto;
import com.cognive.projects.casernkb.model.fes.FesServiceInformationDto;
import com.cognive.projects.casernkb.model.fes.FesSuspiciousActivityIdentifierDto;
import com.cognive.projects.casernkb.model.fes.FesTerrorismFinancingDto;
import com.cognive.projects.casernkb.model.fes.FesUnusualOperationFeatureDto;
import com.cognive.projects.casernkb.service.FesService;
import com.prime.db.rnkb.model.BaseDictionary;
import com.prime.db.rnkb.model.fes.FesAdditionalOperation;
import com.prime.db.rnkb.model.fes.FesAddress;
import com.prime.db.rnkb.model.fes.FesBankInformation;
import com.prime.db.rnkb.model.fes.FesBeneficiary;
import com.prime.db.rnkb.model.fes.FesBranchInformation;
import com.prime.db.rnkb.model.fes.FesCasesStatus;
import com.prime.db.rnkb.model.fes.FesCashMoneyTransfers;
import com.prime.db.rnkb.model.fes.FesCategory;
import com.prime.db.rnkb.model.fes.FesEio;
import com.prime.db.rnkb.model.fes.FesForeignCardTransactions;
import com.prime.db.rnkb.model.fes.FesFreezingAppliedMeasures;
import com.prime.db.rnkb.model.fes.FesGeneralInformation;
import com.prime.db.rnkb.model.fes.FesIdentityDocument;
import com.prime.db.rnkb.model.fes.FesIdentityDocumentGeneral;
import com.prime.db.rnkb.model.fes.FesInspectionDetails;
import com.prime.db.rnkb.model.fes.FesMainPageNew;
import com.prime.db.rnkb.model.fes.FesMainPageOtherSections;
import com.prime.db.rnkb.model.fes.FesMoneyPlace;
import com.prime.db.rnkb.model.fes.FesMoneyTransfers;
import com.prime.db.rnkb.model.fes.FesOperationInformation;
import com.prime.db.rnkb.model.fes.FesOperationsDetails;
import com.prime.db.rnkb.model.fes.FesOperationsReason;
import com.prime.db.rnkb.model.fes.FesParticipant;
import com.prime.db.rnkb.model.fes.FesParticipantForeign;
import com.prime.db.rnkb.model.fes.FesParticipantForeignIdentifier;
import com.prime.db.rnkb.model.fes.FesParticipantIndividual;
import com.prime.db.rnkb.model.fes.FesParticipantLegal;
import com.prime.db.rnkb.model.fes.FesPreciousMetalData;
import com.prime.db.rnkb.model.fes.FesRefusalCaseDetails;
import com.prime.db.rnkb.model.fes.FesRefusalOperation;
import com.prime.db.rnkb.model.fes.FesRefusalReason;
import com.prime.db.rnkb.model.fes.FesRightOfResidenceDocument;
import com.prime.db.rnkb.model.fes.FesServiceInformation;
import com.prime.db.rnkb.model.fes.FesSuspiciousActivityIdentifier;
import com.prime.db.rnkb.model.fes.FesTerrorismFinancing;
import com.prime.db.rnkb.model.fes.FesUnusualOperationFeature;
import com.prime.db.rnkb.repository.fes.FesAdditionalOperationRepository;
import com.prime.db.rnkb.repository.fes.FesAddressRepository;
import com.prime.db.rnkb.repository.fes.FesBankInformationRepository;
import com.prime.db.rnkb.repository.fes.FesBeneficiaryRepository;
import com.prime.db.rnkb.repository.fes.FesBranchInformationRepository;
import com.prime.db.rnkb.repository.fes.FesCasesStatusRepository;
import com.prime.db.rnkb.repository.fes.FesCashMoneyTransfersRepository;
import com.prime.db.rnkb.repository.fes.FesCategoryRepository;
import com.prime.db.rnkb.repository.fes.FesEioRepository;
import com.prime.db.rnkb.repository.fes.FesForeignCardTransactionsRepository;
import com.prime.db.rnkb.repository.fes.FesFreezingAppliedMeasuresRepository;
import com.prime.db.rnkb.repository.fes.FesGeneralInformationRepository;
import com.prime.db.rnkb.repository.fes.FesIdentityDocumentGeneralRepository;
import com.prime.db.rnkb.repository.fes.FesIdentityDocumentRepository;
import com.prime.db.rnkb.repository.fes.FesInspectionDetailsRepository;
import com.prime.db.rnkb.repository.fes.FesMainPageNewRepository;
import com.prime.db.rnkb.repository.fes.FesMainPageOtherSectionsRepository;
import com.prime.db.rnkb.repository.fes.FesMoneyPlaceRepository;
import com.prime.db.rnkb.repository.fes.FesMoneyTransfersRepository;
import com.prime.db.rnkb.repository.fes.FesOperationInformationRepository;
import com.prime.db.rnkb.repository.fes.FesOperationsDetailsRepository;
import com.prime.db.rnkb.repository.fes.FesOperationsReasonRepository;
import com.prime.db.rnkb.repository.fes.FesParticipantForeignIdentifierRepository;
import com.prime.db.rnkb.repository.fes.FesParticipantForeignRepository;
import com.prime.db.rnkb.repository.fes.FesParticipantIndividualRepository;
import com.prime.db.rnkb.repository.fes.FesParticipantLegalRepository;
import com.prime.db.rnkb.repository.fes.FesParticipantRepository;
import com.prime.db.rnkb.repository.fes.FesPreciousMetalDataRepository;
import com.prime.db.rnkb.repository.fes.FesRefusalCaseDetailsRepository;
import com.prime.db.rnkb.repository.fes.FesRefusalOperationRepository;
import com.prime.db.rnkb.repository.fes.FesRefusalReasonRepository;
import com.prime.db.rnkb.repository.fes.FesRightOfResidenceDocumentRepository;
import com.prime.db.rnkb.repository.fes.FesServiceInformationRepository;
import com.prime.db.rnkb.repository.fes.FesSuspiciousActivityIdentifierRepository;
import com.prime.db.rnkb.repository.fes.FesTerrorismFinancingRepository;
import com.prime.db.rnkb.repository.fes.FesUnusualOperationFeatureRepository;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_101;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_22;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_24;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_26;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_276;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_307;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_310;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_311;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_312;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_313;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_314;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_315;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_316;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_317;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_318;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_319;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_320;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_321;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_322;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_323;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_324;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_325;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_326;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_327;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_328;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_329;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_330;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_331;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_332;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_333;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_334;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_335;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_336;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_337;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_338;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_342;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_343;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_346;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_347;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_348;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_40;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_45;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_69;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_83;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_86;

@RequiredArgsConstructor
@Component
public class FesChangeCaseDelegate implements JavaDelegate {

    private final FesServiceInformationRepository fesServiceInformationRepository;
    private final FesBankInformationRepository fesBankInformationRepository;
    private final FesRefusalReasonRepository fesRefusalReasonRepository;
    private final FesRefusalCaseDetailsRepository fesRefusalCaseDetailsRepository;
    private final FesCategoryRepository fesCategoryRepository;
    private final FesParticipantRepository fesParticipantRepository;
    private final FesParticipantLegalRepository fesParticipantLegalRepository;
    private final FesParticipantIndividualRepository fesParticipantIndividualRepository;
    private final FesAddressRepository fesAddressRepository;
    private final FesGeneralInformationRepository fesGeneralInformationRepository;
    private final FesBranchInformationRepository fesBranchInformationRepository;
    private final FesIdentityDocumentGeneralRepository fesIdentityDocumentGeneralRepository;
    private final FesIdentityDocumentRepository fesIdentityDocumentRepository;
    private final FesRightOfResidenceDocumentRepository fesRightOfResidenceDocumentRepository;
    private final FesEioRepository fesEioRepository;
    private final FesBeneficiaryRepository fesBeneficiaryRepository;
    private final FesService fesService;
    private final FesMainPageOtherSectionsRepository fesMainPageOtherSectionsRepository;
    private final FesParticipantForeignRepository fesParticipantForeignRepository;
    private final FesParticipantForeignIdentifierRepository fesParticipantForeignIdentifierRepository;
    private final FesFreezingAppliedMeasuresRepository fesFreezingAppliedMeasuresRepository;
    private final FesOperationInformationRepository fesOperationInformationRepository;
    private final FesRefusalOperationRepository fesRefusalOperationRepository;
    private final FesSuspiciousActivityIdentifierRepository fesSuspiciousActivityIdentifierRepository;
    private final FesOperationsReasonRepository fesOperationsReasonRepository;
    private final FesUnusualOperationFeatureRepository fesUnusualOperationFeatureRepository;
    private final FesMainPageNewRepository fesMainPageNewRepository;
    private final FesOperationsDetailsRepository fesOperationsDetailsRepository;
    private final FesPreciousMetalDataRepository fesPreciousMetalDataRepository;
    private final FesMoneyTransfersRepository fesMoneyTransfersRepository;
    private final FesInspectionDetailsRepository fesInspectionDetailsRepository;
    private final FesCashMoneyTransfersRepository fesCashMoneyTransfersRepository;
    private final FesMoneyPlaceRepository fesMoneyPlaceRepository;
    private final FesAdditionalOperationRepository fesAdditionalOperationRepository;
    private final FesTerrorismFinancingRepository fesTerrorismFinancingRepository;
    private final FesForeignCardTransactionsRepository fesForeignCardTransactionsRepository;
    private final FesCasesStatusRepository fesCasesStatusRepository;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        var categoryId = (Long) delegateExecution.getVariable("fesCategoryId");
        var isCaseNew = (boolean) delegateExecution.getVariable("isCaseNew");

        var fesCategory = fesCategoryRepository.findById(categoryId).get();
        var fesCaseSaveDto = (FesCaseSaveDto) delegateExecution.getVariable("fesCaseSaveDto");
        var rejectTypeCode = fesCaseSaveDto.getFesCategory().getFesRefusalCaseDetails().get(0).getRejectType().getCode();
        var fesCategoryCode = fesCaseSaveDto.getFesCategory().getCategory().getCode();
        var recordType = fesService.getBd(DICTIONARY_86, "1");

        if (!isCaseNew) {
            FesCasesStatus fesCasesStatus = fesCategory.getFesCasesStatuses().get(0);
            if (Objects.equals(fesCasesStatus.getCaseStatus().getCode(), "1") &&
                    Objects.equals(fesCasesStatus.getCaseCondition().getCode(), "1")) {
                fesCasesStatus.setCaseStatus(fesService.getCaseStatus());
                fesCasesStatus.setCaseCondition(fesService.getCaseCondition());
                fesCasesStatusRepository.save(fesCasesStatus);
            }
        }

        if (!fesCaseSaveDto.getFesCategory().getFesServiceInformations().isEmpty()) {
            FesServiceInformationDto fesServiceInformationDto = fesCaseSaveDto.getFesCategory().getFesServiceInformations().get(0);
            FesServiceInformation fesServiceInformation;
            if (fesCategory.getFesServiceInformations() == null || fesCategory.getFesServiceInformations().isEmpty()) {
                fesServiceInformation = new FesServiceInformation();
                fesServiceInformation.setCategoryId(fesCategory);
                if (fesCategoryCode.equals("4") && (rejectTypeCode.equals("2") || rejectTypeCode.equals("3"))) {
                    fesServiceInformation.setInformationType(fesService.getBd(DICTIONARY_312, "01"));
                } else if (fesCategoryCode.equals("4") && rejectTypeCode.equals("1")) {
                    fesServiceInformation.setInformationType(fesService.getBd(DICTIONARY_312, "2"));
                } else if (fesCategoryCode.equals("2")) {
                    fesServiceInformation.setInformationType(fesService.getBd(DICTIONARY_312, "3"));
                } else if (fesCategoryCode.equals("1")) {
                    fesServiceInformation.setInformationType(fesService.getBd(DICTIONARY_312, "1"));
                } else if (fesCategoryCode.equals("3")) {
                    fesServiceInformation.setInformationType(fesService.getBd(DICTIONARY_312, "4"));
                }
            } else {
                fesServiceInformation = fesCategory.getFesServiceInformations().get(0);
            }
            if (fesServiceInformationDto.getInformationType() != null) {
                fesServiceInformation.setInformationType(fesService.getBd(DICTIONARY_312, fesServiceInformationDto.getInformationType().getCode()));
            }
            fesServiceInformation.setFormatVersion(fesCaseSaveDto.getFesDataPrefill().getFormatVersion());
            fesServiceInformation.setSoftVersion(fesCaseSaveDto.getFesDataPrefill().getSoftVersion());
            fesServiceInformation.setCorrespondentUuid(fesServiceInformationDto.getCorrespondentUuid());
            fesServiceInformation.setDate(fesServiceInformationDto.getDate());
            fesServiceInformation.setOfficerPosition(fesServiceInformationDto.getOfficerPosition());
            fesServiceInformation.setOfficerLastname(fesServiceInformationDto.getOfficerLastName());
            fesServiceInformation.setOfficerFirstname(fesServiceInformationDto.getOfficerFirstName());
            fesServiceInformation.setOfficerMiddlename(fesServiceInformationDto.getOfficerMiddleName());
            fesServiceInformation.setOfficerPhone(fesServiceInformationDto.getOfficerPhone());
            fesServiceInformation.setOfficerMail(fesServiceInformationDto.getOfficerMail());
            fesServiceInformation.setOfficerFullName(fesServiceInformationDto.getOfficerLastName() != null &&
                    fesServiceInformationDto.getOfficerFirstName() != null ?
                    null : fesServiceInformationDto.getOfficerFullName());
            fesServiceInformationRepository.save(fesServiceInformation);
        }

        List<FesBankInformationDto> fesBankInformationDtoList = fesCaseSaveDto.getFesCategory().getFesBankInformations();
        if (fesBankInformationDtoList != null && !fesBankInformationDtoList.isEmpty()) {
            List<FesBankInformation> fesBankInformations = new ArrayList<>();
            for (FesBankInformationDto fesBankInformationDto : fesBankInformationDtoList) {
                FesBankInformation fesBankInformation = createOrUpdateFesBankInformation(fesBankInformationDto, fesCategory);

                fesBankInformationRepository.save(fesBankInformation);

                List<FesBranchInformationDto> fesBranchInformationDtoList = fesBankInformationDto.getFesBranchInformations();
                List<FesBranchInformation> existingFesBranchInformations = fesBankInformation.getId() != null ?
                        fesBranchInformationRepository.findByBankInformationId(fesBankInformation) : new ArrayList<>();
                if (fesBranchInformationDtoList != null && !fesBranchInformationDtoList.isEmpty()) {
                    List<FesBranchInformation> fesBranchInformations = new ArrayList<>();
                    for (FesBranchInformationDto fesBranchInformationDto : fesBranchInformationDtoList) {
                        FesBranchInformation fesBranchInformation = createOrUpdateFesBranchInformation(fesBranchInformationDto, fesBankInformation);
                        fesBranchInformations.add(fesBranchInformation);
                    }
                    fesBranchInformationRepository.saveAll(fesBranchInformations);
                } else {
                    fesBranchInformationRepository.deleteAll(existingFesBranchInformations);
                }
                fesBankInformations.add(fesBankInformation);
            }
            fesBankInformationRepository.saveAll(fesBankInformations);
        }

        if (fesCategoryCode.equals("2")) {
            FesFreezingAppliedMeasuresDto fesFreezingAppliedMeasuresDto = fesCaseSaveDto.getFesCategory().getFesFreezingAppliedMeasures().get(0);
            FesFreezingAppliedMeasures fesFreezingAppliedMeasures = getFesFreezingAppliedMeasures(fesCategory, fesFreezingAppliedMeasuresDto);
            fesFreezingAppliedMeasuresRepository.save(fesFreezingAppliedMeasures);
        }

        if (fesCategoryCode.equals("3")) {
            List<FesInspectionDetailsDto> fesInspectionDetailsDtoList = fesCaseSaveDto.getFesCategory().getFesInspectionDetails();
            List<FesInspectionDetails> existingFesInspectionDetails = fesInspectionDetailsRepository.findByCategoryId(fesCategory);
            if (fesInspectionDetailsDtoList != null && !fesInspectionDetailsDtoList.isEmpty()) {
                fesService.deleteMissingItems(fesInspectionDetailsDtoList, existingFesInspectionDetails, fesInspectionDetailsRepository, FesInspectionDetailsDto::getId, FesInspectionDetails::getId);
                fesService.createAndSaveAllItems(fesInspectionDetailsDtoList, dto -> createOrUpdateFesInspectionDetails(dto, fesCategory), fesInspectionDetailsRepository, fesCategory);
            } else {
                fesInspectionDetailsRepository.deleteAll(existingFesInspectionDetails);
            }
        }

        if (fesCategoryCode.equals("1") || (rejectTypeCode != null && rejectTypeCode.equals("1"))) {
            List<FesOperationInformationDto> fesOperationInformationDtoList = fesCaseSaveDto.getFesCategory().getFesOperationInformations();
            List<FesOperationInformation> existingFesOperationInformations = fesOperationInformationRepository.findByCategoryId(fesCategory);
            if (fesOperationInformationDtoList != null && !fesOperationInformationDtoList.isEmpty()) {
                fesService.deleteMissingItems(fesOperationInformationDtoList, existingFesOperationInformations, fesOperationInformationRepository, FesOperationInformationDto::getId, FesOperationInformation::getId);
                fesService.createAndSaveAllItems(fesOperationInformationDtoList, dto -> createOrUpdateFesOperationInformation(dto, fesCategory), fesOperationInformationRepository, fesCategory);
            } else {
                fesOperationInformationRepository.deleteAll(existingFesOperationInformations);
            }

            List<FesSuspiciousActivityIdentifierDto> fesSuspiciousActivityIdentifierDtoList = fesCaseSaveDto.getFesCategory().getFesSuspiciousActivityIdentifiers();
            List<FesSuspiciousActivityIdentifier> existingFesSuspiciousActivityIdentifiers = fesSuspiciousActivityIdentifierRepository.findByCategoryId(fesCategory);
            if (fesSuspiciousActivityIdentifierDtoList != null && !fesSuspiciousActivityIdentifierDtoList.isEmpty()) {
                fesService.deleteMissingItems(fesSuspiciousActivityIdentifierDtoList, existingFesSuspiciousActivityIdentifiers, fesSuspiciousActivityIdentifierRepository, FesSuspiciousActivityIdentifierDto::getId, FesSuspiciousActivityIdentifier::getId);
                fesService.createAndSaveAllItems(fesSuspiciousActivityIdentifierDtoList, dto -> createOrUpdateFesSuspiciousActivityIdentifier(dto, fesCategory), fesSuspiciousActivityIdentifierRepository, fesCategory);
            } else {
                fesSuspiciousActivityIdentifierRepository.deleteAll(existingFesSuspiciousActivityIdentifiers);
            }

            List<FesOperationsReasonDto> fesOperationsReasonDtoList = fesCaseSaveDto.getFesCategory().getFesOperationsReasons();
            List<FesOperationsReason> existingFesOperationsReasons = fesOperationsReasonRepository.findByCategoryId(fesCategory);
            if (fesOperationsReasonDtoList != null && !fesOperationsReasonDtoList.isEmpty()) {
                fesService.deleteMissingItems(fesOperationsReasonDtoList, existingFesOperationsReasons, fesOperationsReasonRepository, FesOperationsReasonDto::getId, FesOperationsReason::getId);
                fesService.createAndSaveAllItems(fesOperationsReasonDtoList, dto -> createOrUpdateFesOperationsReason(dto, fesCategory), fesOperationsReasonRepository, fesCategory);
            } else {
                fesOperationsReasonRepository.deleteAll(existingFesOperationsReasons);
            }

            List<FesUnusualOperationFeatureDto> fesUnusualOperationFeatureDtoList = fesCaseSaveDto.getFesCategory().getFesUnusualOperationFeatures();
            List<FesUnusualOperationFeature> existingFesUnusualOperationFeatures = fesUnusualOperationFeatureRepository.findByCategoryId(fesCategory);
            if (fesUnusualOperationFeatureDtoList != null && !fesUnusualOperationFeatureDtoList.isEmpty()) {
                fesService.deleteMissingItems(fesUnusualOperationFeatureDtoList, existingFesUnusualOperationFeatures, fesUnusualOperationFeatureRepository, FesUnusualOperationFeatureDto::getId, FesUnusualOperationFeature::getId);
                fesService.createAndSaveAllItems(fesUnusualOperationFeatureDtoList, dto -> createOrUpdateFesUnusualOperationFeature(dto, fesCategory), fesUnusualOperationFeatureRepository, fesCategory);
            } else {
                fesUnusualOperationFeatureRepository.deleteAll(existingFesUnusualOperationFeatures);
            }
        }

        List<FesRefusalReasonDto> fesRefusalReasonDtoList = fesCaseSaveDto.getFesCategory().getFesRefusalReasons();
        List<FesRefusalReason> existingFesRefusalReasons = fesRefusalReasonRepository.findByCategoryId(fesCategory);
        if (fesRefusalReasonDtoList != null && !fesRefusalReasonDtoList.isEmpty()) {
            fesService.deleteMissingItems(fesRefusalReasonDtoList, existingFesRefusalReasons, fesRefusalReasonRepository, FesRefusalReasonDto::getId, FesRefusalReason::getId);
            fesService.createAndSaveAllItems(fesRefusalReasonDtoList, dto -> createOrUpdateFesRefusalReason(dto, fesCategory), fesRefusalReasonRepository, fesCategory);
        } else {
            fesRefusalReasonRepository.deleteAll(existingFesRefusalReasons);
        }

        if (fesCategoryCode.equals("4") && !fesCaseSaveDto.getFesCategory().getFesRefusalCaseDetails().isEmpty()) {
            FesRefusalCaseDetailsDto fesRefusalCaseDetailsDto = fesCaseSaveDto.getFesCategory().getFesRefusalCaseDetails().get(0);
            FesRefusalCaseDetails fesRefusalCaseDetails = fesCategory.getFesRefusalCaseDetails().get(0);

            fesRefusalCaseDetails.setBankInfFeature(fesService.getBd(DICTIONARY_317, fesRefusalCaseDetailsDto.getBankInfFeature() != null ?
                    fesRefusalCaseDetailsDto.getBankInfFeature().getCode() : null));
            fesRefusalCaseDetails.setGroundOfRefusal(fesService.getBd(DICTIONARY_318, fesRefusalCaseDetailsDto.getGroundOfRefusal() != null ?
                    fesRefusalCaseDetailsDto.getGroundOfRefusal().getCode() : null));
            fesRefusalCaseDetails.setRefusalDate(fesRefusalCaseDetailsDto.getRefusalDate());
            fesRefusalCaseDetails.setRejectType(fesService.getBd(DICTIONARY_307, fesRefusalCaseDetailsDto.getRejectType() != null ?
                    fesRefusalCaseDetailsDto.getRejectType().getCode() : null));
            fesRefusalCaseDetails.setRemovalReason(fesRefusalCaseDetailsDto.getRemovalReason());

            if (rejectTypeCode != null && rejectTypeCode.equals("1")) {
                List<FesRefusalOperationDto> fesRefusalOperationDtoList = fesRefusalCaseDetailsDto.getFesRefusalOperations();
                List<FesRefusalOperation> existingFesRefusalOperations = fesRefusalCaseDetails.getId() > 0 ?
                        fesRefusalOperationRepository.findByRefusalCaseDetailsId(fesRefusalCaseDetails) : new ArrayList<>();
                if (fesRefusalOperationDtoList != null && !fesRefusalOperationDtoList.isEmpty()) {
                    removeUnnecessaryEntities(
                            fesRefusalOperationDtoList,
                            existingFesRefusalOperations,
                            FesRefusalOperationDto::getId,
                            FesRefusalOperation::getId,
                            fesRefusalOperationRepository
                    );
                    List<FesRefusalOperation> fesRefusalOperations = new ArrayList<>();
                    for (FesRefusalOperationDto fesRefusalOperationDto : fesRefusalOperationDtoList) {
                        FesRefusalOperation fesRefusalOperation = createOrUpdateFesRefusalOperation(fesRefusalOperationDto, fesRefusalCaseDetails);
                        fesRefusalOperations.add(fesRefusalOperation);
                    }
                    fesRefusalOperationRepository.saveAll(fesRefusalOperations);
                    fesRefusalCaseDetails.setFesRefusalOperations(fesRefusalOperations);
                }
            }

            fesRefusalCaseDetailsRepository.save(fesRefusalCaseDetails);
            fesCategory.setFesRefusalCaseDetails(new ArrayList<>(List.of(fesRefusalCaseDetails)));
        }

        FesGeneralInformation fesGeneralInformation = getFesGeneralInformation(fesCategory, recordType, fesCaseSaveDto);
        fesGeneralInformationRepository.save(fesGeneralInformation);

        FesMainPageOtherSections fesMainPageOtherSections = getFesMainPageOtherSections(fesCategory, fesCaseSaveDto);
        fesMainPageOtherSectionsRepository.save(fesMainPageOtherSections);

        if (fesCategoryCode.equals("1")) {
            FesMainPageNew fesMainPageNew = getFesMainPageNew(fesCategory, fesCaseSaveDto);
            fesMainPageNewRepository.save(fesMainPageNew);

            FesOperationsDetails fesOperationsDetails = getFesOperationsDetails(fesCategory, fesCaseSaveDto);
            fesOperationsDetailsRepository.save(fesOperationsDetails);

            List<FesPreciousMetalDataDto> fesPreciousMetalDataDtoList = fesCaseSaveDto.getFesCategory().getFesPreciousMetalData();
            List<FesPreciousMetalData> existingFesPreciousMetalDatas = fesPreciousMetalDataRepository.findByCategoryId(fesCategory);
            if (fesPreciousMetalDataDtoList != null && !fesPreciousMetalDataDtoList.isEmpty()) {
                fesService.deleteMissingItems(fesPreciousMetalDataDtoList, existingFesPreciousMetalDatas, fesPreciousMetalDataRepository, FesPreciousMetalDataDto::getId, FesPreciousMetalData::getId);
                fesService.createAndSaveAllItems(fesPreciousMetalDataDtoList, dto -> createOrUpdateFesPreciousMetalData(dto, fesCategory), fesPreciousMetalDataRepository, fesCategory);
            } else {
                fesPreciousMetalDataRepository.deleteAll(existingFesPreciousMetalDatas);
            }

            List<FesMoneyTransfersDto> fesMoneyTransfersDtoList = fesCaseSaveDto.getFesCategory().getFesMoneyTransfersList();
            List<FesMoneyTransfers> existingFesMoneyTransfers = fesMoneyTransfersRepository.findByCategoryId(fesCategory);
            if (fesMoneyTransfersDtoList != null && !fesMoneyTransfersDtoList.isEmpty()) {
                fesService.deleteMissingItems(fesMoneyTransfersDtoList, existingFesMoneyTransfers, fesMoneyTransfersRepository, FesMoneyTransfersDto::getId, FesMoneyTransfers::getId);
                fesService.createAndSaveAllItems(fesMoneyTransfersDtoList, dto -> createOrUpdateFesMoneyTransfers(dto, fesCategory), fesMoneyTransfersRepository, fesCategory);
            } else {
                fesMoneyTransfersRepository.deleteAll(existingFesMoneyTransfers);
            }

            List<FesCashMoneyTransfersDto> fesCashMoneyTransfersDtoList = fesCaseSaveDto.getFesCategory().getFesCashMoneyTransfers();
            List<FesCashMoneyTransfers> existingFesCashMoneyTransfers = fesCashMoneyTransfersRepository.findByCategoryId(fesCategory);
            if (fesCashMoneyTransfersDtoList != null && !fesCashMoneyTransfersDtoList.isEmpty()) {
                fesService.deleteMissingItems(fesCashMoneyTransfersDtoList, existingFesCashMoneyTransfers, fesCashMoneyTransfersRepository, FesCashMoneyTransfersDto::getId, FesCashMoneyTransfers::getId);
                fesService.createAndSaveAllItems(fesCashMoneyTransfersDtoList, dto -> createOrUpdateFesCashMoneyTransfers(dto, fesCategory), fesCashMoneyTransfersRepository, fesCategory);
            } else {
                fesCashMoneyTransfersRepository.deleteAll(existingFesCashMoneyTransfers);
            }

            List<FesMoneyPlaceDto> fesMoneyPlaceDtoList = fesCaseSaveDto.getFesCategory().getFesMoneyPlaces();
            List<FesMoneyPlace> existingFesMoneyPlaces = fesMoneyPlaceRepository.findByCategoryId(fesCategory);
            if (fesMoneyPlaceDtoList != null && !fesMoneyPlaceDtoList.isEmpty()) {
                fesService.deleteMissingItems(fesMoneyPlaceDtoList, existingFesMoneyPlaces, fesMoneyPlaceRepository, FesMoneyPlaceDto::getId, FesMoneyPlace::getId);
                fesService.createAndSaveAllItems(fesMoneyPlaceDtoList, dto -> createOrUpdateFesMoneyPlace(dto, fesCategory), fesMoneyPlaceRepository, fesCategory);
            } else {
                fesMoneyPlaceRepository.deleteAll(existingFesMoneyPlaces);
            }

            List<FesAdditionalOperationDto> fesAdditionalOperationDtoList = fesCaseSaveDto.getFesCategory().getFesAdditionalOperations();
            List<FesAdditionalOperation> existingFesAdditionalOperations = fesAdditionalOperationRepository.findByCategoryId(fesCategory);
            if (fesAdditionalOperationDtoList != null && !fesAdditionalOperationDtoList.isEmpty()) {
                fesService.deleteMissingItems(fesAdditionalOperationDtoList, existingFesAdditionalOperations, fesAdditionalOperationRepository, FesAdditionalOperationDto::getId, FesAdditionalOperation::getId);
                fesService.createAndSaveAllItems(fesAdditionalOperationDtoList, dto -> createOrUpdateFesAdditionalOperation(dto, fesCategory), fesAdditionalOperationRepository, fesCategory);
            } else {
                fesAdditionalOperationRepository.deleteAll(existingFesAdditionalOperations);
            }

            List<FesTerrorismFinancingDto> fesTerrorismFinancingDtoList = fesCaseSaveDto.getFesCategory().getFesTerrorismFinancings();
            List<FesTerrorismFinancing> existingFesTerrorismFinancings = fesTerrorismFinancingRepository.findByCategoryId(fesCategory);
            if (fesTerrorismFinancingDtoList != null && !fesTerrorismFinancingDtoList.isEmpty()) {
                fesService.deleteMissingItems(fesTerrorismFinancingDtoList, existingFesTerrorismFinancings, fesTerrorismFinancingRepository, FesTerrorismFinancingDto::getId, FesTerrorismFinancing::getId);
                fesService.createAndSaveAllItems(fesTerrorismFinancingDtoList, dto -> createOrUpdateFesTerrorismFinancing(dto, fesCategory), fesTerrorismFinancingRepository, fesCategory);
            } else {
                fesTerrorismFinancingRepository.deleteAll(existingFesTerrorismFinancings);
            }

            List<FesForeignCardTransactionsDto> fesForeignCardTransactionsDtoList = fesCaseSaveDto.getFesCategory().getFesForeignCardTransactions();
            List<FesForeignCardTransactions> existingFesForeignCardTransactions = fesForeignCardTransactionsRepository.findByCategoryId(fesCategory);
            if (fesForeignCardTransactionsDtoList != null && !fesForeignCardTransactionsDtoList.isEmpty()) {
                fesService.deleteMissingItems(fesForeignCardTransactionsDtoList, existingFesForeignCardTransactions, fesForeignCardTransactionsRepository, FesForeignCardTransactionsDto::getId, FesForeignCardTransactions::getId);
                fesService.createAndSaveAllItems(fesForeignCardTransactionsDtoList, dto -> createOrUpdateFesForeignCardTransactions(dto, fesCategory), fesForeignCardTransactionsRepository, fesCategory);
            } else {
                fesForeignCardTransactionsRepository.deleteAll(existingFesForeignCardTransactions);
            }
        }

        List<FesParticipantDto> fesParticipantDtoList = fesCaseSaveDto.getFesCategory().getFesParticipants();
        List<FesParticipant> existingFesParticipants = fesParticipantRepository.findFesParticipantsByCategoryId(fesCategory);
        if (fesParticipantDtoList != null && !fesParticipantDtoList.isEmpty()) {
            removeUnnecessaryEntities(
                    fesParticipantDtoList,
                    existingFesParticipants,
                    FesParticipantDto::getId,
                    FesParticipant::getId,
                    fesParticipantRepository
            );
            List<FesParticipant> fesParticipants = new ArrayList<>();
            for (FesParticipantDto fesParticipantDto : fesParticipantDtoList) {
                FesParticipant fesParticipant = createOrUpdateFesParticipant(fesParticipantDto, fesCategory);

                fesParticipantRepository.save(fesParticipant);

                List<FesEioDto> fesEioDtoList = fesParticipantDto.getFesEios();
                List<FesEio> existingFesEios = fesParticipant.getId() > 0 ?
                        fesEioRepository.findByParticipantId(fesParticipant) : new ArrayList<>();
                if (fesEioDtoList != null && !fesEioDtoList.isEmpty()) {
                    removeUnnecessaryEntities(
                            fesEioDtoList,
                            existingFesEios,
                            FesEioDto::getId,
                            FesEio::getId,
                            fesEioRepository
                    );
                    List<FesEio> fesEios = new ArrayList<>();
                    for (FesEioDto fesEioDto : fesEioDtoList) {
                        FesEio fesEio = createOrUpdateFesEio(fesEioDto, fesParticipant);

                        fesEioRepository.save(fesEio);

                        List<FesParticipantLegalDto> fesParticipantLegalDtoList = fesEioDto.getFesParticipantLegals();
                        List<FesParticipantLegal> existingFesParticipantLegals = fesEio.getId() > 0 ?
                                fesParticipantLegalRepository.findByEioId(fesEio) : new ArrayList<>();
                        if (fesParticipantLegalDtoList != null && !fesParticipantLegalDtoList.isEmpty()) {
                            removeUnnecessaryEntities(
                                    fesParticipantLegalDtoList,
                                    existingFesParticipantLegals,
                                    FesParticipantLegalDto::getId,
                                    FesParticipantLegal::getId,
                                    fesParticipantLegalRepository
                            );
                            List<FesParticipantLegal> fesParticipantLegals = new ArrayList<>();
                            for (FesParticipantLegalDto fesParticipantLegalDto : fesParticipantLegalDtoList) {
                                FesParticipantLegal fesParticipantLegal = createOrUpdateFesParticipantLegal(fesParticipantLegalDto, null, fesEio);
                                fesParticipantLegals.add(fesParticipantLegal);
                            }
                            fesParticipantLegalRepository.saveAll(fesParticipantLegals);
                            fesParticipant.setFesParticipantLegals(fesParticipantLegals);
                        } else {
                            fesParticipantLegalRepository.deleteAll(existingFesParticipantLegals);
                        }

                        List<FesParticipantIndividualDto> fesParticipantIndividualDtoList = fesEioDto.getFesParticipantIndividuals();
                        List<FesParticipantIndividual> existingFesParticipantIndividuals = fesEio.getId() > 0 ?
                                fesParticipantIndividualRepository.findByEioId(fesEio) : new ArrayList<>();
                        if (fesParticipantIndividualDtoList != null && !fesParticipantIndividualDtoList.isEmpty()) {
                            removeUnnecessaryEntities(
                                    fesParticipantIndividualDtoList,
                                    existingFesParticipantIndividuals,
                                    FesParticipantIndividualDto::getId,
                                    FesParticipantIndividual::getId,
                                    fesParticipantIndividualRepository
                            );
                            List<FesParticipantIndividual> fesParticipantIndividuals = new ArrayList<>();
                            for (FesParticipantIndividualDto fesParticipantIndividualDto : fesParticipantIndividualDtoList) {
                                FesParticipantIndividual fesParticipantIndividual = createOrUpdateFesParticipantIndividual(fesParticipantIndividualDto, null, null, fesEio);

                                fesParticipantIndividualRepository.save(fesParticipantIndividual);

                                fesDocumentsProcessing(fesParticipantIndividuals, fesParticipantIndividualDto, fesParticipantIndividual);
                            }
                            fesParticipantIndividualRepository.saveAll(fesParticipantIndividuals);
                            fesParticipant.setFesParticipantIndividuals(fesParticipantIndividuals);
                        } else {
                            fesParticipantIndividualRepository.deleteAll(existingFesParticipantIndividuals);
                        }

                        List<FesAddressDto> fesAddressDtoList = fesEioDto.getFesAddresses();
                        List<FesAddress> existingFesAddresses = fesEio.getId() > 0 ?
                                fesAddressRepository.findByEioId(fesEio) : new ArrayList<>();
                        processingAddress(fesAddressDtoList, existingFesAddresses, null, null, fesEio, null);
                        fesEios.add(fesEio);
                    }
                    fesEioRepository.saveAll(fesEios);
                    fesParticipant.setFesEios(fesEios);
                } else {
                    fesEioRepository.deleteAll(existingFesEios);
                }

                List<FesBeneficiaryDto> fesBeneficiaryDtoList = fesParticipantDto.getFesBeneficiaryList();
                List<FesBeneficiary> existingFesBeneficiaries = fesParticipant.getId() > 0 ?
                        fesBeneficiaryRepository.findByParticipantId(fesParticipant) : new ArrayList<>();
                if (fesBeneficiaryDtoList != null && !fesBeneficiaryDtoList.isEmpty()) {
                    removeUnnecessaryEntities(
                            fesBeneficiaryDtoList,
                            existingFesBeneficiaries,
                            FesBeneficiaryDto::getId,
                            FesBeneficiary::getId,
                            fesBeneficiaryRepository
                    );
                    List<FesBeneficiary> fesBeneficiaries = new ArrayList<>();
                    for (FesBeneficiaryDto fesBeneficiaryDto : fesBeneficiaryDtoList) {
                        FesBeneficiary fesBeneficiary = createOrUpdateFesBeneficiary(fesBeneficiaryDto, fesParticipant);

                        fesBeneficiaryRepository.save(fesBeneficiary);

                        List<FesParticipantIndividualDto> fesParticipantIndividualDtoList = fesBeneficiaryDto.getFesParticipantIndividuals();
                        List<FesParticipantIndividual> existingFesParticipantIndividuals = fesBeneficiary.getId() > 0 ?
                                fesParticipantIndividualRepository.findByBeneficiaryId(fesBeneficiary) : new ArrayList<>();
                        if (fesParticipantIndividualDtoList != null && !fesParticipantIndividualDtoList.isEmpty()) {
                            removeUnnecessaryEntities(
                                    fesParticipantIndividualDtoList,
                                    existingFesParticipantIndividuals,
                                    FesParticipantIndividualDto::getId,
                                    FesParticipantIndividual::getId,
                                    fesParticipantIndividualRepository
                            );
                            List<FesParticipantIndividual> fesParticipantIndividuals = new ArrayList<>();
                            for (FesParticipantIndividualDto fesParticipantIndividualDto : fesParticipantIndividualDtoList) {
                                FesParticipantIndividual fesParticipantIndividual = createOrUpdateFesParticipantIndividual(fesParticipantIndividualDto, null, fesBeneficiary, null);

                                fesParticipantIndividualRepository.save(fesParticipantIndividual);

                                fesDocumentsProcessing(fesParticipantIndividuals, fesParticipantIndividualDto, fesParticipantIndividual);
                            }
                            fesParticipantIndividualRepository.saveAll(fesParticipantIndividuals);
                            fesParticipant.setFesParticipantIndividuals(fesParticipantIndividuals);
                        } else {
                            fesParticipantIndividualRepository.deleteAll(existingFesParticipantIndividuals);
                        }


                        List<FesAddressDto> fesAddressDtoList = fesBeneficiaryDto.getFesAddresses();
                        List<FesAddress> existingFesAddresses = fesBeneficiary.getId() > 0 ?
                                fesAddressRepository.findByBeneficiaryId(fesBeneficiary) : new ArrayList<>();
                        processingAddress(fesAddressDtoList, existingFesAddresses, null, null, null, fesBeneficiary);

                        fesBeneficiaries.add(fesBeneficiary);
                    }
                    fesBeneficiaryRepository.saveAll(fesBeneficiaries);
                    fesParticipant.setFesBeneficiaryList(fesBeneficiaries);
                } else {
                    fesBeneficiaryRepository.deleteAll(existingFesBeneficiaries);
                }

                List<FesParticipantIndividualDto> fesParticipantIndividualDtoList = fesParticipantDto.getFesParticipantIndividuals();
                List<FesParticipantIndividual> existingFesParticipantIndividuals = fesParticipant.getId() > 0 ?
                        fesParticipantIndividualRepository.findByParticipantId(fesParticipant) : new ArrayList<>();
                if (fesParticipantIndividualDtoList != null && !fesParticipantIndividualDtoList.isEmpty()) {
                    removeUnnecessaryEntities(
                            fesParticipantIndividualDtoList,
                            existingFesParticipantIndividuals,
                            FesParticipantIndividualDto::getId,
                            FesParticipantIndividual::getId,
                            fesParticipantIndividualRepository
                    );
                    List<FesParticipantIndividual> fesParticipantIndividuals = new ArrayList<>();
                    for (FesParticipantIndividualDto fesParticipantIndividualDto : fesParticipantIndividualDtoList) {
                        FesParticipantIndividual fesParticipantIndividual = createOrUpdateFesParticipantIndividual(fesParticipantIndividualDto, fesParticipant, null, null);

                        fesParticipantIndividualRepository.save(fesParticipantIndividual);

                        fesDocumentsProcessing(fesParticipantIndividuals, fesParticipantIndividualDto, fesParticipantIndividual);
                    }
                    fesParticipantIndividualRepository.saveAll(fesParticipantIndividuals);
                    fesParticipant.setFesParticipantIndividuals(fesParticipantIndividuals);
                } else {
                    fesParticipantIndividualRepository.deleteAll(existingFesParticipantIndividuals);
                }

                List<FesParticipantLegalDto> fesParticipantLegalDtoList = fesParticipantDto.getFesParticipantLegals();
                List<FesParticipantLegal> existingFesParticipantLegals = fesParticipant.getId() > 0 ?
                        fesParticipantLegalRepository.findByParticipantId(fesParticipant) : new ArrayList<>();
                if (fesParticipantLegalDtoList != null && !fesParticipantLegalDtoList.isEmpty()) {
                    removeUnnecessaryEntities(
                            fesParticipantLegalDtoList,
                            existingFesParticipantLegals,
                            FesParticipantLegalDto::getId,
                            FesParticipantLegal::getId,
                            fesParticipantLegalRepository
                    );
                    List<FesParticipantLegal> fesParticipantLegals = new ArrayList<>();
                    for (FesParticipantLegalDto fesParticipantLegalDto : fesParticipantLegalDtoList) {
                        FesParticipantLegal fesParticipantLegal = createOrUpdateFesParticipantLegal(fesParticipantLegalDto, fesParticipant, null);
                        fesParticipantLegals.add(fesParticipantLegal);
                    }
                    fesParticipantLegalRepository.saveAll(fesParticipantLegals);
                    fesParticipant.setFesParticipantLegals(fesParticipantLegals);
                } else {
                    fesParticipantLegalRepository.deleteAll(existingFesParticipantLegals);
                }

                List<FesParticipantForeignDto> fesParticipantForeignDtoList = fesParticipantDto.getFesParticipantForeigns();
                List<FesParticipantForeign> existingFesParticipantForeigns = fesParticipant.getId() > 0 ?
                        fesParticipantForeignRepository.findByParticipantId(fesParticipant) : new ArrayList<>();
                if (fesParticipantForeignDtoList != null && !fesParticipantForeignDtoList.isEmpty()) {
                    removeUnnecessaryEntities(
                            fesParticipantForeignDtoList,
                            existingFesParticipantForeigns,
                            FesParticipantForeignDto::getId,
                            FesParticipantForeign::getId,
                            fesParticipantForeignRepository
                    );
                    List<FesParticipantForeign> fesParticipantForeigns = new ArrayList<>();
                    for (FesParticipantForeignDto fesParticipantForeignDto : fesParticipantForeignDtoList) {
                        FesParticipantForeign fesParticipantForeign = createOrUpdateFesParticipantForeign(fesParticipantForeignDto, fesParticipant);

                        fesParticipantForeignRepository.save(fesParticipantForeign);

                        List<FesParticipantForeignIdentifierDto> fesParticipantForeignIdentifierDtoList = fesParticipantForeignDto.getFesParticipantForeignIdentifiers();
                        List<FesParticipantForeignIdentifier> existingFesParticipantForeignIdentifiers = fesParticipantForeign.getId() > 0 ?
                                fesParticipantForeignIdentifierRepository.findByParticipantForeignId(fesParticipantForeign) : new ArrayList<>();
                        if (fesParticipantForeignIdentifierDtoList != null && !fesParticipantForeignIdentifierDtoList.isEmpty()) {
                            removeUnnecessaryEntities(
                                    fesParticipantForeignIdentifierDtoList,
                                    existingFesParticipantForeignIdentifiers,
                                    FesParticipantForeignIdentifierDto::getId,
                                    FesParticipantForeignIdentifier::getId,
                                    fesParticipantForeignIdentifierRepository
                            );
                            List<FesParticipantForeignIdentifier> fesParticipantForeignIdentifiers = new ArrayList<>();
                            for (FesParticipantForeignIdentifierDto fesParticipantForeignIdentifierDto : fesParticipantForeignIdentifierDtoList) {
                                FesParticipantForeignIdentifier fesParticipantForeignIdentifier = createOrUpdateFesParticipantForeignIdentifier(fesParticipantForeignIdentifierDto, fesParticipantForeign);
                                fesParticipantForeignIdentifiers.add(fesParticipantForeignIdentifier);
                            }
                            fesParticipantForeignIdentifierRepository.saveAll(fesParticipantForeignIdentifiers);
                            fesParticipantForeign.setFesParticipantForeignIdentifiers(fesParticipantForeignIdentifiers);
                        } else {
                            fesParticipantForeignIdentifierRepository.deleteAll(existingFesParticipantForeignIdentifiers);
                        }
                        fesParticipantForeigns.add(fesParticipantForeign);
                    }
                    fesParticipantForeignRepository.saveAll(fesParticipantForeigns);
                    fesParticipant.setFesParticipantForeigns(fesParticipantForeigns);
                } else {
                    fesParticipantForeignRepository.deleteAll(existingFesParticipantForeigns);
                }

                List<FesAddressDto> fesAddressDtoList = fesParticipantDto.getFesAddresses();
                List<FesAddress> existingFesAddresses = fesParticipant.getId() > 0 ?
                        fesAddressRepository.findByParticipantId(fesParticipant) : new ArrayList<>();
                processingAddress(fesAddressDtoList, existingFesAddresses, null, fesParticipant, null, null);

                fesParticipants.add(fesParticipant);
            }
            fesParticipantRepository.saveAll(fesParticipants);
        } else {
            fesParticipantRepository.deleteAll(existingFesParticipants);
        }

        List<FesAddressDto> fesAddressDtoList = fesCaseSaveDto.getFesCategory().getFesAddressList();
        List<FesAddress> existingFesAddresses = fesAddressRepository.findByCategoryId(fesCategory);
        processingAddress(fesAddressDtoList, existingFesAddresses, fesCategory, null, null, null);

        if (fesCategory.getCaseId() != null) {
            delegateExecution.setVariable("caseId", fesCategory.getCaseId().getId());
        }
    }

    private FesFreezingAppliedMeasures getFesFreezingAppliedMeasures(FesCategory fesCategory, FesFreezingAppliedMeasuresDto dto) {
        FesFreezingAppliedMeasures fesFreezingAppliedMeasures;

        if (fesCategory.getFesFreezingAppliedMeasures() == null || fesCategory.getFesFreezingAppliedMeasures().isEmpty()) {
            fesFreezingAppliedMeasures = new FesFreezingAppliedMeasures();
            fesFreezingAppliedMeasures.setCategoryId(fesCategory);
        } else {
            fesFreezingAppliedMeasures = fesCategory.getFesFreezingAppliedMeasures().get(0);
        }
        fesFreezingAppliedMeasures.setReasonType(fesService.getBd(DICTIONARY_316, getCode(dto.getReasonType())));
        fesFreezingAppliedMeasures.setPersonCode(dto.getPersonCode());
        fesFreezingAppliedMeasures.setFreezingDate(dto.getFreezingDate());
        fesFreezingAppliedMeasures.setFreezingTime(dto.getFreezingTime());
        fesFreezingAppliedMeasures.setFreezingMoneyType(fesService.getBd(DICTIONARY_314, getCode(dto.getFreezingMoneyType())));
        fesFreezingAppliedMeasures.setAccountNum(dto.getAccountNum());
        fesFreezingAppliedMeasures.setCurrency(fesService.getBd(DICTIONARY_26, getCode(dto.getCurrency())));
        fesFreezingAppliedMeasures.setAccountAmount(dto.getAccountAmount());
        fesFreezingAppliedMeasures.setAccountAmountNationalCurrency(dto.getAccountAmountNationalCurrency());
        fesFreezingAppliedMeasures.setSecuritiesType(fesService.getBd(DICTIONARY_315, getCode(dto.getSecuritiesType())));
        fesFreezingAppliedMeasures.setSecuritiesNominalPrice(dto.getSecuritiesNominalPrice());
        fesFreezingAppliedMeasures.setSecuritiesMarketPrice(dto.getSecuritiesMarketPrice());

        return fesFreezingAppliedMeasures;
    }

    private void fesDocumentsProcessing(List<FesParticipantIndividual> fesParticipantIndividuals, FesParticipantIndividualDto fesParticipantIndividualDto, FesParticipantIndividual fesParticipantIndividual) {
        List<FesIdentityDocumentGeneralDto> fesIdentityDocumentGeneralDtoList = fesParticipantIndividualDto.getFesIdentityDocumentGenerals();
        List<FesIdentityDocumentGeneral> existingFesIdentityDocumentGenerals = fesParticipantIndividual.getId() > 0 ?
                fesIdentityDocumentGeneralRepository.findByParticipantIndividualId(fesParticipantIndividual) : new ArrayList<>();
        if (fesIdentityDocumentGeneralDtoList != null && !fesIdentityDocumentGeneralDtoList.isEmpty()) {
            removeUnnecessaryEntities(
                    fesIdentityDocumentGeneralDtoList,
                    existingFesIdentityDocumentGenerals,
                    FesIdentityDocumentGeneralDto::getId,
                    FesIdentityDocumentGeneral::getId,
                    fesIdentityDocumentGeneralRepository
            );
            List<FesIdentityDocumentGeneral> fesIdentityDocumentGenerals = new ArrayList<>();
            for (FesIdentityDocumentGeneralDto fesIdentityDocumentGeneralDto : fesIdentityDocumentGeneralDtoList) {
                FesIdentityDocumentGeneral fesIdentityDocumentGeneral = createOrUpdateFesIdentityDocumentGeneral(fesIdentityDocumentGeneralDto, fesParticipantIndividual);

                fesIdentityDocumentGeneralRepository.save(fesIdentityDocumentGeneral);

                List<FesIdentityDocumentDto> fesIdentityDocumentDtoList = fesIdentityDocumentGeneralDto.getFesIdentityDocuments();
                List<FesIdentityDocument> existingFesIdentityDocuments = fesIdentityDocumentGeneral.getId() > 0 ?
                        fesIdentityDocumentRepository.findByIdentityDocumentGeneralId(fesIdentityDocumentGeneral) : new ArrayList<>();
                if (fesIdentityDocumentDtoList != null && !fesIdentityDocumentDtoList.isEmpty()) {
                    removeUnnecessaryEntities(
                            fesIdentityDocumentDtoList,
                            existingFesIdentityDocuments,
                            FesIdentityDocumentDto::getId,
                            FesIdentityDocument::getId,
                            fesIdentityDocumentRepository
                    );
                    List<FesIdentityDocument> fesIdentityDocuments = new ArrayList<>();
                    for (FesIdentityDocumentDto fesIdentityDocumentDto : fesIdentityDocumentDtoList) {
                        FesIdentityDocument fesIdentityDocument = createOrUpdateFesIdentityDocument(fesIdentityDocumentDto, fesIdentityDocumentGeneral);
                        fesIdentityDocuments.add(fesIdentityDocument);
                    }
                    fesIdentityDocumentRepository.saveAll(fesIdentityDocuments);
                    fesIdentityDocumentGeneral.setFesIdentityDocuments(fesIdentityDocuments);
                } else {
                    fesIdentityDocumentRepository.deleteAll(existingFesIdentityDocuments);
                }

                List<FesRightOfResidenceDocumentDto> fesRightOfResidenceDocumentDtoList = fesIdentityDocumentGeneralDto.getFesRightOfResidenceDocuments();
                List<FesRightOfResidenceDocument> existingFesRightOfResidenceDocuments = fesIdentityDocumentGeneral.getId() > 0 ?
                        fesRightOfResidenceDocumentRepository.findByIdentityDocumentGeneralId(fesIdentityDocumentGeneral) : new ArrayList<>();
                if (fesRightOfResidenceDocumentDtoList != null && !fesRightOfResidenceDocumentDtoList.isEmpty()) {
                    removeUnnecessaryEntities(
                            fesRightOfResidenceDocumentDtoList,
                            existingFesRightOfResidenceDocuments,
                            FesRightOfResidenceDocumentDto::getId,
                            FesRightOfResidenceDocument::getId,
                            fesRightOfResidenceDocumentRepository
                    );
                    List<FesRightOfResidenceDocument> fesRightOfResidenceDocuments = new ArrayList<>();
                    for (FesRightOfResidenceDocumentDto fesRightOfResidenceDocumentDto : fesRightOfResidenceDocumentDtoList) {
                        FesRightOfResidenceDocument fesRightOfResidenceDocument = createOrUpdateFesRightOfResidenceDocument(fesRightOfResidenceDocumentDto, fesIdentityDocumentGeneral);
                        fesRightOfResidenceDocuments.add(fesRightOfResidenceDocument);
                    }
                    fesRightOfResidenceDocumentRepository.saveAll(fesRightOfResidenceDocuments);
                    fesIdentityDocumentGeneral.setFesRightOfResidenceDocuments(fesRightOfResidenceDocuments);
                } else {
                    fesRightOfResidenceDocumentRepository.deleteAll(existingFesRightOfResidenceDocuments);
                }

                fesIdentityDocumentGenerals.add(fesIdentityDocumentGeneral);
            }
            fesIdentityDocumentGeneralRepository.saveAll(fesIdentityDocumentGenerals);
            fesParticipantIndividual.setFesIdentityDocumentGenerals(fesIdentityDocumentGenerals);
        } else {
            fesIdentityDocumentGeneralRepository.deleteAll(existingFesIdentityDocumentGenerals);
        }
        fesParticipantIndividuals.add(fesParticipantIndividual);
    }

    @NotNull
    private FesGeneralInformation getFesGeneralInformation(FesCategory fesCategory, BaseDictionary recordType, FesCaseSaveDto fesCaseSaveDto) {
        FesGeneralInformation fesGeneralInformation;
        if (fesCategory.getFesGeneralInformations() == null || fesCategory.getFesGeneralInformations().isEmpty()) {
            fesGeneralInformation = new FesGeneralInformation();
            fesGeneralInformation.setCategoryId(fesCategory);
        } else {
            fesGeneralInformation = fesCategory.getFesGeneralInformations().get(0);
        }
        fesGeneralInformation.setCategoryId(fesCategory);
        if (fesGeneralInformation.getNum() == null) {
            FesBankInformationDto fesBankInformation = fesCaseSaveDto.getFesCategory().getFesBankInformations().get(0);

            String regNum = fesBankInformation.getBankRegNum();
            String branchNum = fesBankInformation.getFesBranchInformations().get(0).getBranchNum();

            String num = fesService.generateNum(regNum, branchNum, fesCategory);
            if (num != null) {
                fesGeneralInformation.setNum(num);
            }
        }
        fesGeneralInformation.setComment(fesCaseSaveDto.getFesCategory().getFesGeneralInformations().get(0).getComment());
        fesGeneralInformation.setRecordType(recordType);
        return fesGeneralInformation;
    }

    @NotNull
    private FesMainPageOtherSections getFesMainPageOtherSections(FesCategory fesCategory, FesCaseSaveDto fesCaseSaveDto) {
        FesCasesStatus fesCasesStatuses = fesCategory.getFesCasesStatuses().get(0);
        List<FesMainPageOtherSections> otherSectionsList = fesCasesStatuses.getFesMainPageOtherSections();
        FesMainPageOtherSections fesMainPageOtherSections = (otherSectionsList == null || otherSectionsList.isEmpty())
                ? new FesMainPageOtherSections()
                : otherSectionsList.get(0);
        if (otherSectionsList == null || otherSectionsList.isEmpty()) {
            fesMainPageOtherSections.setCasesStatusId(fesCasesStatuses);
        }
        fesMainPageOtherSections.setComment(fesCaseSaveDto.getFesCategory().getFesGeneralInformations().get(0).getComment());
        return fesMainPageOtherSections;
    }

    private FesMainPageNew getFesMainPageNew(FesCategory fesCategory, FesCaseSaveDto fesCaseSaveDto) {
        FesCasesStatus fesCasesStatuses = fesCategory.getFesCasesStatuses().get(0);
        FesMainPageNewDto dto = fesCaseSaveDto.getFesCategory().getFesCasesStatuses().get(0).getFesMainPageNews().get(0);
        List<FesMainPageNew> fesMainPageNewList = fesCasesStatuses.getFesMainPageNews();
        FesMainPageNew fesMainPageNew = (fesMainPageNewList == null || fesMainPageNewList.isEmpty())
                ? new FesMainPageNew()
                : fesMainPageNewList.get(0);
        if (fesMainPageNewList == null || fesMainPageNewList.isEmpty()) {
            fesMainPageNew.setCasesStatusId(fesCasesStatuses);
        }
        fesMainPageNew.setOperationDate(dto.getOperationDate());
        fesMainPageNew.setPaymentReference(dto.getPaymentReference());
        fesMainPageNew.setPurpose(dto.getPurpose());
        fesMainPageNew.setPayerName(dto.getPayerName());
        fesMainPageNew.setPayerInn(dto.getPayerInn());
        fesMainPageNew.setPayerType(fesService.getBd(DICTIONARY_24, getCode(dto.getPayerType())));
        fesMainPageNew.setPayerMark(fesService.getBd(DICTIONARY_22, getCode(dto.getPayerMark())));
        fesMainPageNew.setPayeeName(dto.getPayeeName());
        fesMainPageNew.setPayeeInn(dto.getPayeeInn());
        fesMainPageNew.setPayeeType(fesService.getBd(DICTIONARY_24, getCode(dto.getPayeeType())));
        fesMainPageNew.setPayeeMark(fesService.getBd(DICTIONARY_22, getCode(dto.getPayeeMark())));
        fesMainPageNew.setOperationStatus(fesService.getBd(DICTIONARY_45, getCode(dto.getOperationStatus())));

        return fesMainPageNew;
    }

    private FesOperationsDetails getFesOperationsDetails(FesCategory fesCategory, FesCaseSaveDto fesCaseSaveDto) {
        FesOperationsDetailsDto dto = fesCaseSaveDto.getFesCategory().getFesOperationsDetails().get(0);
        List<FesOperationsDetails> fesOperationsDetailsList = fesCategory.getFesOperationsDetails();
        FesOperationsDetails entity = (fesOperationsDetailsList == null || fesOperationsDetailsList.isEmpty())
                ? new FesOperationsDetails()
                : fesOperationsDetailsList.get(0);
        if (fesOperationsDetailsList == null || fesOperationsDetailsList.isEmpty()) {
            entity.setCategoryId(fesCategory);
        }
        entity.setPaySystemName1(dto.getPaySystemName1());
        entity.setPaySystemName2(dto.getPaySystemName2());
        entity.setConversionCurrency(fesService.getBd(DICTIONARY_26, getCode(dto.getConversionCurrency())));
        entity.setAmountConversionCurrency(dto.getAmountConversionCurrency());
        entity.setEspOperationFeature(fesService.getBd(DICTIONARY_311, getCode(dto.getEspOperationFeature())));
        entity.setOperationCharacteristic(dto.getOperationCharacteristic());
        entity.setEspTime(dto.getEspTime());
        return entity;
    }

    private void processingAddress(List<FesAddressDto> fesAddressDtoList, List<FesAddress> existingFesAddresses, FesCategory fesCategory, FesParticipant fesParticipant, FesEio fesEio, FesBeneficiary fesBeneficiary) {
        if (fesAddressDtoList != null && !fesAddressDtoList.isEmpty()) {
            removeUnnecessaryEntities(
                    fesAddressDtoList,
                    existingFesAddresses,
                    FesAddressDto::getId,
                    FesAddress::getId,
                    fesAddressRepository
            );
            List<FesAddress> fesAddresses = new ArrayList<>();
            for (FesAddressDto fesAddressDto : fesAddressDtoList) {
                FesAddress fesAddress = createOrUpdateFesAddress(fesAddressDto, fesCategory, fesParticipant, fesEio, fesBeneficiary);
                fesAddresses.add(fesAddress);
            }
            fesAddressRepository.saveAll(fesAddresses);
        } else {
            fesAddressRepository.deleteAll(existingFesAddresses);
        }
    }

    private FesRefusalReason createOrUpdateFesRefusalReason(FesRefusalReasonDto fesRefusalReasonDto, FesCategory fesCategory) {
        FesRefusalReason fesRefusalReason = new FesRefusalReason();

        if (fesRefusalReasonDto.getId() != null) {
            fesRefusalReason = fesRefusalReasonRepository.findById(fesRefusalReasonDto.getId()).orElse(fesRefusalReason);
        }

        fesRefusalReason.setCategoryId(fesCategory);
        fesRefusalReason.setRefusalReason(fesService.getBd(DICTIONARY_320, fesRefusalReasonDto.getRefusalReason() != null ?
                fesRefusalReasonDto.getRefusalReason().getCode() : null));
        fesRefusalReason.setRefusalReasonOthername(fesRefusalReasonDto.getRefusalReasonOthername());

        return fesRefusalReason;
    }

    private FesOperationInformation createOrUpdateFesOperationInformation(FesOperationInformationDto dto, FesCategory fesCategory) {
        FesOperationInformation fesOperationInformation = new FesOperationInformation();
        if (dto.getId() != null) {
            fesOperationInformation = fesOperationInformationRepository.findById(dto.getId()).orElse(fesOperationInformation);
        }
        fesOperationInformation.setCategoryId(fesCategory);
        fesOperationInformation.setOperationFeature(fesService.getBd(DICTIONARY_83, getCode(dto.getOperationFeature())));
        fesOperationInformation.setCurrency(fesService.getBd(DICTIONARY_26, getCode(dto.getCurrency())));
        fesOperationInformation.setAmount(dto.getAmount());
        fesOperationInformation.setAmountNationalCurrency(dto.getAmountNationalCurrency());
        fesOperationInformation.setCurrencyTransactionAttribute(dto.getCurrencyTransactionAttribute());
        fesOperationInformation.setOperationType(fesService.getBd(DICTIONARY_310, getCode(dto.getOperationType())));

        return fesOperationInformation;
    }

    private FesSuspiciousActivityIdentifier createOrUpdateFesSuspiciousActivityIdentifier(FesSuspiciousActivityIdentifierDto dto, FesCategory rootEntity) {
        FesSuspiciousActivityIdentifier entity = new FesSuspiciousActivityIdentifier();
        if (dto.getId() != null) {
            entity = fesSuspiciousActivityIdentifierRepository.findById(dto.getId()).orElse(entity);
        }
        entity.setCategoryId(rootEntity);
        entity.setSuspiciousActivityIdentifier(dto.getSuspiciousActivityIdentifier());
        entity.setInn(dto.getInn());
        entity.setForeignNum(dto.getForeignNum());

        return entity;
    }

    private FesInspectionDetails createOrUpdateFesInspectionDetails(FesInspectionDetailsDto dto, FesCategory rootEntity) {
        FesInspectionDetails entity = new FesInspectionDetails();
        if (dto.getId() != null) {
            entity = fesInspectionDetailsRepository.findById(dto.getId()).orElse(entity);
        }
        entity.setCategoryId(rootEntity);
        entity.setPreviousInspectionDate(dto.getPreviousInspectionDate());
        entity.setCurrentInspectionDate(dto.getCurrentInspectionDate());
        entity.setCount30(dto.getCount30());
        entity.setCount50(dto.getCount50());
        entity.setCountCommon(dto.getCountCommon());
        entity.setCountCommon1(dto.getCountCommon1());
        entity.setCountCommon2(dto.getCountCommon2());
        entity.setCountCommon3(dto.getCountCommon3());
        entity.setCountCommon0(dto.getCountCommon0());
        entity.setCountCommon01(dto.getCountCommon01());
        entity.setCountCommon02(dto.getCountCommon02());
        entity.setCountCommon03(dto.getCountCommon03());
        entity.setCountLegal(dto.getCountLegal());
        entity.setCountLegal1(dto.getCountLegal1());
        entity.setCountLegal2(dto.getCountLegal2());
        entity.setCountLegal3(dto.getCountLegal3());
        entity.setCountLegal0(dto.getCountLegal0());
        entity.setCountLegal01(dto.getCountLegal01());
        entity.setCountLegal02(dto.getCountLegal02());
        entity.setCountLegal03(dto.getCountLegal03());
        entity.setCountIndividual(dto.getCountIndividual());
        entity.setCountIndividual1(dto.getCountIndividual1());
        entity.setCountIndividual2(dto.getCountIndividual2());
        entity.setCountIndividual3(dto.getCountIndividual3());
        entity.setCountIndividual0(dto.getCountIndividual0());
        entity.setCountIndividual01(dto.getCountIndividual01());
        entity.setCountIndividual02(dto.getCountIndividual02());
        entity.setCountIndividual03(dto.getCountIndividual03());
        return entity;
    }

    private FesPreciousMetalData createOrUpdateFesPreciousMetalData(FesPreciousMetalDataDto dto, FesCategory rootEntity) {
        FesPreciousMetalData entity = new FesPreciousMetalData();
        if (dto.getId() != null) {
            entity = fesPreciousMetalDataRepository.findById(dto.getId()).orElse(entity);
        }
        entity.setCategoryId(rootEntity);
        entity.setPreciousMetal(fesService.getBd(DICTIONARY_276, getCode(dto.getPreciousMetal())));
        entity.setPreciousMetalOthername(dto.getPreciousMetalOthername());
        return entity;
    }

    private FesMoneyTransfers createOrUpdateFesMoneyTransfers(FesMoneyTransfersDto dto, FesCategory rootEntity) {
        FesMoneyTransfers entity = new FesMoneyTransfers();
        if (dto.getId() != null) {
            entity = fesMoneyTransfersRepository.findById(dto.getId()).orElse(entity);
        }
        entity.setCategoryId(rootEntity);
        entity.setTransferType(fesService.getBd(DICTIONARY_332, getCode(dto.getTransferType())));
        entity.setMoneyTransferOperatorType(fesService.getBd(DICTIONARY_335, getCode(dto.getMoneyTransferOperatorType())));
        entity.setPayerAccountNum(dto.getPayerAccountNum());
        entity.setPayerBankAccount(dto.getPayerBankAccount());
        entity.setPayeeAccountNum(dto.getPayeeAccountNum());
        entity.setPayeeBankAccount(dto.getPayeeBankAccount());
        entity.setPayerBankBic(dto.getPayerBankBic());
        entity.setPayeeBankBic(dto.getPayeeBankBic());
        entity.setPayerBankName(dto.getPayerBankName());
        entity.setPayeeBankName(dto.getPayeeBankName());
        entity.setTransferStatus(fesService.getBd(DICTIONARY_333, getCode(dto.getTransferStatus())));
        entity.setTerritoryCode(dto.getTerritoryCode());
        entity.setPayerEspUuid(dto.getPayerEspUuid());
        entity.setPayeeEspUuid(dto.getPayeeEspUuid());
        entity.setPayerIpAddress(dto.getPayerIpAddress());
        entity.setPayerMacAddress(dto.getPayerMacAddress());
        return entity;
    }

    private FesCashMoneyTransfers createOrUpdateFesCashMoneyTransfers(FesCashMoneyTransfersDto dto, FesCategory rootEntity) {
        FesCashMoneyTransfers entity = new FesCashMoneyTransfers();
        if (dto.getId() != null) {
            entity = fesCashMoneyTransfersRepository.findById(dto.getId()).orElse(entity);
        }
        entity.setCategoryId(rootEntity);
        entity.setTransactionCharacterCode(fesService.getBd(DICTIONARY_334, getCode(dto.getTransactionCharacterCode())));
        entity.setClientCardNum(dto.getClientCardNum());
        entity.setClientAccountNum(dto.getClientAccountNum());
        return entity;
    }

    private FesMoneyPlace createOrUpdateFesMoneyPlace(FesMoneyPlaceDto dto, FesCategory rootEntity) {
        FesMoneyPlace entity = new FesMoneyPlace();
        if (dto.getId() != null) {
            entity = fesMoneyPlaceRepository.findById(dto.getId()).orElse(entity);
        }
        entity.setCategoryId(rootEntity);
        entity.setMoneyPlaceType(fesService.getBd(DICTIONARY_331, getCode(dto.getMoneyPlaceType())));
        entity.setTerminalNum(dto.getTerminalNum());
        entity.setBankBic(dto.getBankBic());
        entity.setBankName(dto.getBankName());
        return entity;
    }

    private FesAdditionalOperation createOrUpdateFesAdditionalOperation(FesAdditionalOperationDto dto, FesCategory rootEntity) {
        FesAdditionalOperation entity = new FesAdditionalOperation();
        if (dto.getId() != null) {
            entity = fesAdditionalOperationRepository.findById(dto.getId()).orElse(entity);
        }
        entity.setCategoryId(rootEntity);
        entity.setAdditionalOperationType(fesService.getBd(DICTIONARY_310, getCode(dto.getAdditionalOperationType())));

        return entity;
    }

    private FesTerrorismFinancing createOrUpdateFesTerrorismFinancing(FesTerrorismFinancingDto dto, FesCategory rootEntity) {
        FesTerrorismFinancing entity = new FesTerrorismFinancing();
        if (dto.getId() != null) {
            entity = fesTerrorismFinancingRepository.findById(dto.getId()).orElse(entity);
        }
        entity.setCategoryId(rootEntity);
        entity.setTerrorismFinancingType(fesService.getBd(DICTIONARY_313, getCode(dto.getTerrorismFinancingType())));

        return entity;
    }

    private FesForeignCardTransactions createOrUpdateFesForeignCardTransactions(FesForeignCardTransactionsDto dto, FesCategory rootEntity) {
        FesForeignCardTransactions entity = new FesForeignCardTransactions();
        if (dto.getId() != null) {
            entity = fesForeignCardTransactionsRepository.findById(dto.getId()).orElse(entity);
        }
        entity.setCategoryId(rootEntity);
        entity.setTerritoryCode(dto.getTerritoryCode());
        entity.setCardNum(dto.getCardNum());
        entity.setCardHolder(dto.getCardHolder());
        entity.setAuthorizedOfficerOperationFeature(fesService.getBd(DICTIONARY_336, getCode(dto.getAuthorizedOfficerOperationFeature())));
        entity.setForeignBankName(dto.getForeignBankName());
        entity.setSwift(dto.getSwift());
        return entity;
    }

    private FesOperationsReason createOrUpdateFesOperationsReason(FesOperationsReasonDto dto, FesCategory rootEntity) {
        FesOperationsReason entity = new FesOperationsReason();
        if (dto.getId() != null) {
            entity = fesOperationsReasonRepository.findById(dto.getId()).orElse(entity);
        }
        entity.setCategoryId(rootEntity);
        entity.setDocType(fesService.getBd(DICTIONARY_69, getCode(dto.getDocType())));
        entity.setDocOtherName(dto.getDocOtherName());
        entity.setDocDate(dto.getDocDate());
        entity.setDocNum(dto.getDocNum());
        entity.setSummary(dto.getSummary());

        return entity;
    }

    private FesUnusualOperationFeature createOrUpdateFesUnusualOperationFeature(FesUnusualOperationFeatureDto dto, FesCategory rootEntity) {
        FesUnusualOperationFeature entity = new FesUnusualOperationFeature();
        if (dto.getId() != null) {
            entity = fesUnusualOperationFeatureRepository.findById(dto.getId()).orElse(entity);
        }
        entity.setCategoryId(rootEntity);
        entity.setUnusualOperationType(fesService.getBd(DICTIONARY_310, getCode(dto.getUnusualOperationType())));

        return entity;
    }

    private FesRefusalCaseDetails createOrUpdateFesRefusalCaseDetails(FesRefusalCaseDetailsDto fesRefusalCaseDetailsDto, FesCategory fesCategory) {
        FesRefusalCaseDetails fesRefusalCaseDetails = new FesRefusalCaseDetails();

        if (fesRefusalCaseDetailsDto.getId() != null) {
            fesRefusalCaseDetails = fesRefusalCaseDetailsRepository.findById(fesRefusalCaseDetailsDto.getId()).orElse(fesRefusalCaseDetails);
        }

        fesRefusalCaseDetails.setCategoryId(fesCategory);
        fesRefusalCaseDetails.setBankInfFeature(fesService.getBd(DICTIONARY_317, fesRefusalCaseDetailsDto.getBankInfFeature() != null ?
                fesRefusalCaseDetailsDto.getBankInfFeature().getCode() : null));
        fesRefusalCaseDetails.setGroundOfRefusal(fesService.getBd(DICTIONARY_318, fesRefusalCaseDetailsDto.getGroundOfRefusal() != null ?
                fesRefusalCaseDetailsDto.getGroundOfRefusal().getCode() : null));
        fesRefusalCaseDetails.setRefusalDate(LocalDateTime.now());
        fesRefusalCaseDetails.setRejectType(fesService.getBd(DICTIONARY_307, fesRefusalCaseDetailsDto.getRejectType() != null ?
                fesRefusalCaseDetailsDto.getRejectType().getCode() : null));
        fesRefusalCaseDetails.setRemovalReason(fesRefusalCaseDetailsDto.getRemovalReason());

        return fesRefusalCaseDetails;
    }

    private FesParticipant createOrUpdateFesParticipant(FesParticipantDto fesParticipantDto, FesCategory fesCategory) {
        FesParticipant fesParticipant = new FesParticipant();

        if (fesParticipantDto.getId() != null) {
            fesParticipant = fesParticipantRepository.findById(fesParticipantDto.getId()).orElse(fesParticipant);
        }
        fesParticipant.setCategoryId(fesCategory);
        fesParticipant.setParticipantStatus(fesService.getBd(DICTIONARY_321, fesParticipantDto.getParticipantStatus() != null ?
                fesParticipantDto.getParticipantStatus().getCode() : null));
        fesParticipant.setParticipantType(fesService.getBd(DICTIONARY_322, fesParticipantDto.getParticipantType() != null ?
                fesParticipantDto.getParticipantType().getCode() : null));
        fesParticipant.setParticipantResidentFeature(fesService.getBd(DICTIONARY_323, fesParticipantDto.getParticipantResidentFeature() != null ?
                fesParticipantDto.getParticipantResidentFeature().getCode() : null));
        fesParticipant.setParticipantFeature(fesService.getBd(DICTIONARY_324, fesParticipantDto.getParticipantFeature() != null ?
                fesParticipantDto.getParticipantFeature().getCode() : null));
        fesParticipant.setParticipantFrommuUuid(fesParticipantDto.getParticipantFrommuUuid());
        fesParticipant.setParticipantCode(fesService.getBd(DICTIONARY_348, fesParticipantDto.getParticipantCode() != null ?
                fesParticipantDto.getParticipantCode().getCode() : null));

        return fesParticipant;
    }

    private FesParticipantLegal createOrUpdateFesParticipantLegal(FesParticipantLegalDto fesParticipantLegalDto, FesParticipant fesParticipant, FesEio fesEio) {
        FesParticipantLegal fesParticipantLegal = new FesParticipantLegal();
        if (fesParticipantLegalDto.getId() != null) {
            fesParticipantLegal = fesParticipantLegalRepository.findById(fesParticipantLegalDto.getId()).orElse(fesParticipantLegal);
        }
        fesParticipantLegal.setParticipantId(fesParticipant);
        fesParticipantLegal.setEioId(fesEio);
        fesParticipantLegal.setParticipantLegalName(fesParticipantLegalDto.getParticipantLegalName());
        fesParticipantLegal.setBranchFeature(fesService.getBd(DICTIONARY_342, fesParticipantLegalDto.getBranchFeature() != null ?
                fesParticipantLegalDto.getBranchFeature().getCode() : null));
        fesParticipantLegal.setInn(fesParticipantLegalDto.getInn());
        fesParticipantLegal.setKpp(fesParticipantLegalDto.getKpp());
        fesParticipantLegal.setOgrn(fesParticipantLegalDto.getOgrn());
        fesParticipantLegal.setRegistrationDate(fesParticipantLegalDto.getRegistrationDate());
        return fesParticipantLegal;
    }

    private FesParticipantForeign createOrUpdateFesParticipantForeign(FesParticipantForeignDto fesParticipantForeignDto, FesParticipant fesParticipant) {
        FesParticipantForeign fesParticipantForeign = new FesParticipantForeign();
        if (fesParticipantForeignDto.getId() != null) {
            fesParticipantForeign = fesParticipantForeignRepository.findById(fesParticipantForeignDto.getId()).orElse(fesParticipantForeign);
        }
        fesParticipantForeign.setParticipantId(fesParticipant);
        fesParticipantForeign.setParticipantForeignName(fesParticipantForeignDto.getParticipantForeignName());
        fesParticipantForeign.setOrgFormFeature(fesService.getBd(DICTIONARY_343, fesParticipantForeignDto.getOrgFormFeature() != null ?
                fesParticipantForeignDto.getOrgFormFeature().getCode() : null));
        fesParticipantForeign.setFounderLastname(fesParticipantForeignDto.getFounderLastname());
        fesParticipantForeign.setFounderFirstname(fesParticipantForeignDto.getFounderFirstname());
        fesParticipantForeign.setFounderMiddlename(fesParticipantForeignDto.getFounderMiddlename());
        fesParticipantForeign.setFounderFullName(fesParticipantForeignDto.getFounderLastname() != null &&
                fesParticipantForeignDto.getFounderFirstname() != null ?
                null : fesParticipantForeignDto.getFounderFullName());
        return fesParticipantForeign;
    }

    private FesParticipantForeignIdentifier createOrUpdateFesParticipantForeignIdentifier(FesParticipantForeignIdentifierDto fesParticipantForeignIdentifierDto, FesParticipantForeign fesParticipantForeign) {
        FesParticipantForeignIdentifier fesParticipantForeignIdentifier = new FesParticipantForeignIdentifier();
        if (fesParticipantForeignIdentifierDto.getId() != null) {
            fesParticipantForeignIdentifier = fesParticipantForeignIdentifierRepository.findById(fesParticipantForeignIdentifierDto.getId()).orElse(fesParticipantForeignIdentifier);
        }
        fesParticipantForeignIdentifier.setParticipantForeignId(fesParticipantForeign);
        fesParticipantForeignIdentifier.setForeignNum(fesParticipantForeignIdentifierDto.getForeignNum());
        fesParticipantForeignIdentifier.setForeignCode(fesParticipantForeignIdentifierDto.getForeignCode());
        return fesParticipantForeignIdentifier;
    }

    private FesParticipantIndividual createOrUpdateFesParticipantIndividual(FesParticipantIndividualDto fesParticipantIndividualDto, FesParticipant fesParticipant, FesBeneficiary fesBeneficiary, FesEio fesEio) {
        FesParticipantIndividual fesParticipantIndividual = new FesParticipantIndividual();
        if (fesParticipantIndividualDto.getId() != null) {
            fesParticipantIndividual = fesParticipantIndividualRepository.findById(fesParticipantIndividualDto.getId()).orElse(fesParticipantIndividual);
        }
        fesParticipantIndividual.setParticipantId(fesParticipant);
        fesParticipantIndividual.setBeneficiaryId(fesBeneficiary);
        fesParticipantIndividual.setEioId(fesEio);
        fesParticipantIndividual.setPhysicalIdentificationFeature(fesService.getBd(DICTIONARY_325, fesParticipantIndividualDto.getPhysicalIdentificationFeature() != null ?
                fesParticipantIndividualDto.getPhysicalIdentificationFeature().getCode() : null));
        fesParticipantIndividual.setLastName(fesParticipantIndividualDto.getLastName());
        fesParticipantIndividual.setFirstName(fesParticipantIndividualDto.getFirstName());
        fesParticipantIndividual.setMiddleName(fesParticipantIndividualDto.getMiddleName());
        fesParticipantIndividual.setInn(fesParticipantIndividualDto.getInn());
        fesParticipantIndividual.setSnils(fesParticipantIndividualDto.getSnils());
        fesParticipantIndividual.setHealthCardNum(fesParticipantIndividualDto.getHealthCardNum());
        fesParticipantIndividual.setPhoneNumber(fesParticipantIndividualDto.getPhoneNumber());
        fesParticipantIndividual.setPrivatePractitionerType(fesService.getBd(DICTIONARY_326, fesParticipantIndividualDto.getPrivatePractitionerType() != null ?
                fesParticipantIndividualDto.getPrivatePractitionerType().getCode() : null));
        fesParticipantIndividual.setPrivatePractitionerRegNum(fesParticipantIndividualDto.getPrivatePractitionerRegNum());
        fesParticipantIndividual.setFullName(fesParticipantIndividualDto.getLastName() != null &&
                fesParticipantIndividualDto.getFirstName() != null ?
                null : fesParticipantIndividualDto.getFullName());
        fesParticipantIndividual.setBirthDate(fesParticipantIndividualDto.getBirthDate());
        fesParticipantIndividual.setCitizenshipCountryCode(fesService.getBd(DICTIONARY_40, fesParticipantIndividualDto.getCitizenshipCountryCode() != null ?
                fesParticipantIndividualDto.getCitizenshipCountryCode().getCode() : null));
        fesParticipantIndividual.setPublicFigureFeature(fesService.getBd(DICTIONARY_330, fesParticipantIndividualDto.getPublicFigureFeature() != null ?
                fesParticipantIndividualDto.getPublicFigureFeature().getCode() : null));
        fesParticipantIndividual.setIdentityDocumentFeature(fesService.getBd(DICTIONARY_327, fesParticipantIndividualDto.getIdentityDocumentFeature() != null ?
                fesParticipantIndividualDto.getIdentityDocumentFeature().getCode() : null));
        fesParticipantIndividual.setOgrnip(fesParticipantIndividualDto.getOgrnip());

        return fesParticipantIndividual;
    }

    private FesIdentityDocumentGeneral createOrUpdateFesIdentityDocumentGeneral(FesIdentityDocumentGeneralDto dto, FesParticipantIndividual rootEntity) {
        FesIdentityDocumentGeneral fesIdentityDocumentGeneral = new FesIdentityDocumentGeneral();
        if (dto.getId() != null) {
            fesIdentityDocumentGeneral = fesIdentityDocumentGeneralRepository.findById(dto.getId()).orElse(fesIdentityDocumentGeneral);
        }
        fesIdentityDocumentGeneral.setParticipantIndividualId(rootEntity);
        fesIdentityDocumentGeneral.setDocumentTypeCode(fesService.getBd(DICTIONARY_329, dto.getDocumentTypeCode() != null ?
                dto.getDocumentTypeCode().getCode() : null));
        fesIdentityDocumentGeneral.setOtherDocumentName(dto.getOtherDocumentName());
        fesIdentityDocumentGeneral.setDocumentSeries(dto.getDocumentSeries());
        fesIdentityDocumentGeneral.setDocumentNum(dto.getDocumentNum());
        fesIdentityDocumentGeneral.setIdentityDocumentType(fesService.getBd(DICTIONARY_337, dto.getIdentityDocumentType() != null ?
                dto.getIdentityDocumentType().getCode() : null));

        return fesIdentityDocumentGeneral;
    }

    private FesIdentityDocument createOrUpdateFesIdentityDocument(FesIdentityDocumentDto dto, FesIdentityDocumentGeneral rootEntity) {
        FesIdentityDocument fesIdentityDocument = new FesIdentityDocument();
        if (dto.getId() != null) {
            fesIdentityDocument = fesIdentityDocumentRepository.findById(dto.getId()).orElse(fesIdentityDocument);
        }
        fesIdentityDocument.setIdentityDocumentGeneralId(rootEntity);
        fesIdentityDocument.setIssueDate(dto.getIssueDate());
        fesIdentityDocument.setDepartmentCode(dto.getDepartmentCode());
        fesIdentityDocument.setIssuingAgency(dto.getIssuingAgency());

        return fesIdentityDocument;
    }

    private FesEio createOrUpdateFesEio(FesEioDto dto, FesParticipant rootEntity) {
        FesEio entity = new FesEio();
        if (dto.getId() != null) {
            entity = fesEioRepository.findById(dto.getId()).orElse(entity);
        }
        entity.setParticipantId(rootEntity);
        entity.setEioType(fesService.getBd(DICTIONARY_346, dto.getEioType() != null ? dto.getEioType().getCode() : null));
        entity.setEioResidentFeature(fesService.getBd(DICTIONARY_347, dto.getEioResidentFeature() != null ?
                dto.getEioResidentFeature().getCode() : null));

        return entity;
    }

    private FesRefusalOperation createOrUpdateFesRefusalOperation(FesRefusalOperationDto dto, FesRefusalCaseDetails rootEntity) {
        FesRefusalOperation entity = new FesRefusalOperation();
        if (dto.getId() != null) {
            entity = fesRefusalOperationRepository.findById(dto.getId()).orElse(entity);
        }
        entity.setRefusalCaseDetailsId(rootEntity);
        entity.setCashFeature(fesService.getBd(DICTIONARY_319, getCode(dto.getCashFeature())));
        entity.setRefusalOperationCharacteristic(dto.getRefusalOperationCharacteristic());

        return entity;
    }

    private FesBeneficiary createOrUpdateFesBeneficiary(FesBeneficiaryDto dto, FesParticipant rootEntity) {
        FesBeneficiary entity = new FesBeneficiary();
        if (dto.getId() != null) {
            entity = fesBeneficiaryRepository.findById(dto.getId()).orElse(entity);
        }
        entity.setParticipantId(rootEntity);
        entity.setBeneficiaryType(fesService.getBd(DICTIONARY_328, dto.getBeneficiaryType() != null ?
                dto.getBeneficiaryType().getCode() : null));
        entity.setBeneficiaryResidentFeature(fesService.getBd(DICTIONARY_338, dto.getBeneficiaryResidentFeature() != null ?
                dto.getBeneficiaryResidentFeature().getCode() : null));

        return entity;
    }

    private FesRightOfResidenceDocument createOrUpdateFesRightOfResidenceDocument(FesRightOfResidenceDocumentDto dto, FesIdentityDocumentGeneral rootEntity) {
        FesRightOfResidenceDocument entity = new FesRightOfResidenceDocument();
        if (dto.getId() != null) {
            entity = fesRightOfResidenceDocumentRepository.findById(dto.getId()).orElse(entity);
        }
        entity.setIdentityDocumentGeneralId(rootEntity);
        entity.setStartStayDate(dto.getStartStayDate());
        entity.setEndStayDate(dto.getEndStayDate());

        return entity;
    }

    private FesAddress createOrUpdateFesAddress(FesAddressDto fesAddressDto, FesCategory fesCategory,
                                                FesParticipant fesParticipant, FesEio fesEio, FesBeneficiary fesBeneficiary) {
        FesAddress fesAddress = new FesAddress();

        if (fesAddressDto.getId() != null) {
            fesAddress = fesAddressRepository.findById(fesAddressDto.getId()).orElse(fesAddress);
        }
        fesAddress.setCategoryId(fesCategory);
        fesAddress.setAddressType(fesService.getBd(DICTIONARY_331, fesAddressDto.getAddressType() != null ?
                fesAddressDto.getAddressType().getCode() : null));
        fesAddress.setPostal(fesAddressDto.getPostal());
        fesAddress.setCountryCode(fesService.getBd(DICTIONARY_40, fesAddressDto.getCountryCode() != null ?
                fesAddressDto.getCountryCode().getCode() : null));
        fesAddress.setOkato(fesService.getBd(DICTIONARY_101, fesAddressDto.getOkato() != null ?
                fesAddressDto.getOkato().getCode() : null));
        fesAddress.setDistrict(fesAddressDto.getDistrict());
        fesAddress.setTownship(fesAddressDto.getTownship());
        fesAddress.setStreet(fesAddressDto.getStreet());
        fesAddress.setHouse(fesAddressDto.getHouse());
        fesAddress.setCorpus(fesAddressDto.getCorpus());
        fesAddress.setRoom(fesAddressDto.getRoom());
        fesAddress.setAddressText(fesAddressDto.getCountryCode() != null &&
                fesAddressDto.getOkato() != null &&
                fesAddressDto.getDistrict() != null &&
                fesAddressDto.getTownship() != null &&
                fesAddressDto.getStreet() != null &&
                fesAddressDto.getHouse() != null &&
                fesAddressDto.getCorpus() != null &&
                fesAddressDto.getRoom() != null ?
                null : fesAddressDto.getAddressText());
        fesAddress.setParticipantId(fesParticipant);
        fesAddress.setEioId(fesEio);
        fesAddress.setBeneficiaryId(fesBeneficiary);

        return fesAddress;
    }

    private FesBankInformation createOrUpdateFesBankInformation(FesBankInformationDto dto, FesCategory rootEntity) {
        FesBankInformation entity = new FesBankInformation();
        if (dto.getId() != null) {
            entity = fesBankInformationRepository.findById(dto.getId()).orElse(entity);
        }
        entity.setCategoryId(rootEntity);
        entity.setReportingAttribute(dto.getReportingAttribute() != null && dto.getReportingAttribute());
        entity.setBankRegNum(dto.getBankRegNum());
        entity.setBankBic(dto.getBankBic());
        entity.setBankOcato(dto.getBankOcato());
        return entity;
    }

    private FesBranchInformation createOrUpdateFesBranchInformation(FesBranchInformationDto dto, FesBankInformation rootEntity) {
        FesBranchInformation entity = new FesBranchInformation();
        if (dto.getId() != null) {
            entity = fesBranchInformationRepository.findById(dto.getId()).orElse(entity);
        }
        entity.setBankInformationId(rootEntity);
        entity.setBranchNum(dto.getBranchNum());
        entity.setTransferringBranchNum(dto.getTransferringBranchNum());
        return entity;
    }

    private <DtoType, EntityType, ID> void removeUnnecessaryEntities(
            List<DtoType> dtoList,
            List<EntityType> existingList,
            Function<DtoType, ID> getIdFromDto,
            Function<EntityType, ID> getIdFromEntity,
            JpaRepository<EntityType, ID> repository) {

        Set<ID> dtoIds = dtoList.stream()
                .map(getIdFromDto)
                .collect(Collectors.toSet());

        List<EntityType> toRemove = existingList.stream()
                .filter(entity -> !dtoIds.contains(getIdFromEntity.apply(entity)))
                .collect(Collectors.toList());

        if (!toRemove.isEmpty()) {
            repository.deleteAll(toRemove);
        }
    }

    private String getCode(BaseDictionary baseDictionary) {
        return (baseDictionary != null) ? baseDictionary.getCode() : null;
    }
}
