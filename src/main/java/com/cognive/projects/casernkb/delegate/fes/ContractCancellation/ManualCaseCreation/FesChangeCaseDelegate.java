package com.cognive.projects.casernkb.delegate.fes.ContractCancellation.ManualCaseCreation;

import com.cognive.projects.casernkb.model.fes.FesAddressDto;
import com.cognive.projects.casernkb.model.fes.FesBankInformationDto;
import com.cognive.projects.casernkb.model.fes.FesBeneficiaryDto;
import com.cognive.projects.casernkb.model.fes.FesBranchInformationDto;
import com.cognive.projects.casernkb.model.fes.FesCaseSaveDto;
import com.cognive.projects.casernkb.model.fes.FesEioDto;
import com.cognive.projects.casernkb.model.fes.FesIdentityDocumentDto;
import com.cognive.projects.casernkb.model.fes.FesIdentityDocumentGeneralDto;
import com.cognive.projects.casernkb.model.fes.FesParticipantDto;
import com.cognive.projects.casernkb.model.fes.FesParticipantIndividualDto;
import com.cognive.projects.casernkb.model.fes.FesParticipantLegalDto;
import com.cognive.projects.casernkb.model.fes.FesRefusalCaseDetailsDto;
import com.cognive.projects.casernkb.model.fes.FesRefusalReasonDto;
import com.cognive.projects.casernkb.model.fes.FesRightOfResidenceDocumentDto;
import com.cognive.projects.casernkb.model.fes.FesServiceInformationDto;
import com.cognive.projects.casernkb.service.FesService;
import com.prime.db.rnkb.model.BaseDictionary;
import com.prime.db.rnkb.model.fes.FesAddress;
import com.prime.db.rnkb.model.fes.FesBankInformation;
import com.prime.db.rnkb.model.fes.FesBeneficiary;
import com.prime.db.rnkb.model.fes.FesBranchInformation;
import com.prime.db.rnkb.model.fes.FesCategory;
import com.prime.db.rnkb.model.fes.FesEio;
import com.prime.db.rnkb.model.fes.FesGeneralInformation;
import com.prime.db.rnkb.model.fes.FesIdentityDocument;
import com.prime.db.rnkb.model.fes.FesIdentityDocumentGeneral;
import com.prime.db.rnkb.model.fes.FesParticipant;
import com.prime.db.rnkb.model.fes.FesParticipantIndividual;
import com.prime.db.rnkb.model.fes.FesParticipantLegal;
import com.prime.db.rnkb.model.fes.FesRefusalCaseDetails;
import com.prime.db.rnkb.model.fes.FesRefusalReason;
import com.prime.db.rnkb.model.fes.FesRightOfResidenceDocument;
import com.prime.db.rnkb.model.fes.FesServiceInformation;
import com.prime.db.rnkb.repository.BaseDictionaryRepository;
import com.prime.db.rnkb.repository.fes.FesAddressRepository;
import com.prime.db.rnkb.repository.fes.FesBankInformationRepository;
import com.prime.db.rnkb.repository.fes.FesBeneficiaryRepository;
import com.prime.db.rnkb.repository.fes.FesBranchInformationRepository;
import com.prime.db.rnkb.repository.fes.FesCategoryRepository;
import com.prime.db.rnkb.repository.fes.FesEioRepository;
import com.prime.db.rnkb.repository.fes.FesGeneralInformationRepository;
import com.prime.db.rnkb.repository.fes.FesIdentityDocumentGeneralRepository;
import com.prime.db.rnkb.repository.fes.FesIdentityDocumentRepository;
import com.prime.db.rnkb.repository.fes.FesParticipantIndividualRepository;
import com.prime.db.rnkb.repository.fes.FesParticipantLegalRepository;
import com.prime.db.rnkb.repository.fes.FesParticipantRepository;
import com.prime.db.rnkb.repository.fes.FesRefusalCaseDetailsRepository;
import com.prime.db.rnkb.repository.fes.FesRefusalReasonRepository;
import com.prime.db.rnkb.repository.fes.FesRightOfResidenceDocumentRepository;
import com.prime.db.rnkb.repository.fes.FesServiceInformationRepository;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class FesChangeCaseDelegate implements JavaDelegate {

    private final FesServiceInformationRepository fesServiceInformationRepository;
    private final BaseDictionaryRepository baseDictionaryRepository;
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

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        var categoryId = (Long) delegateExecution.getVariable("fesCategoryId");

        var fesCategory = fesCategoryRepository.findById(categoryId).get();
        var fesCaseSaveDto = (FesCaseSaveDto) delegateExecution.getVariable("fesCaseSaveDto");
        var recordType = baseDictionaryRepository.getBaseDictionary("1", 86);

        if (!fesCaseSaveDto.getFesCategory().getFesServiceInformations().isEmpty()) {
            FesServiceInformationDto fesServiceInformationDto = fesCaseSaveDto.getFesCategory().getFesServiceInformations().get(0);
            FesServiceInformation fesServiceInformation;
            if (fesCategory.getFesServiceInformations() == null || fesCategory.getFesServiceInformations().isEmpty()) {
                fesServiceInformation = new FesServiceInformation();
                fesServiceInformation.setCategoryId(fesCategory);
            } else {
                fesServiceInformation = fesCategory.getFesServiceInformations().get(0);
            }
            fesServiceInformation.setInformationType(getBdById(fesServiceInformationDto.getInformationTypeId()));
            fesServiceInformation.setFormatVersion(fesCaseSaveDto.getFesDataPrefill().getFormatVersion());
            fesServiceInformation.setSoftVersion(fesCaseSaveDto.getFesDataPrefill().getSoftVersion());
            fesServiceInformation.setCorrespondentUuid(fesServiceInformationDto.getCorrespondentUuid());
            fesServiceInformation.setDate(LocalDateTime.now());
            fesServiceInformation.setOfficerPosition(fesServiceInformationDto.getOfficerPosition());
            fesServiceInformation.setOfficerLastname(fesServiceInformationDto.getOfficerLastName());
            fesServiceInformation.setOfficerFirstname(fesServiceInformationDto.getOfficerFirstName());
            fesServiceInformation.setOfficerMiddlename(fesServiceInformationDto.getOfficerMiddleName());
            fesServiceInformation.setOfficerPhone(fesServiceInformationDto.getOfficerPhone());
            fesServiceInformation.setOfficerMail(fesServiceInformationDto.getOfficerMail());
            fesServiceInformation.setOfficerFullName(fesServiceInformationDto.getOfficerFullName());
            fesServiceInformationRepository.save(fesServiceInformation);
        }

        FesGeneralInformation fesGeneralInformation;
        if (fesCategory.getFesGeneralInformations() == null || fesCategory.getFesGeneralInformations().isEmpty()) {
            fesGeneralInformation = new FesGeneralInformation();
            fesGeneralInformation.setCategoryId(fesCategory);
            fesGeneralInformation.setNum(fesService.generateNum(fesCaseSaveDto.getFesDataPrefill().getBankRegNum()));
        } else {
            fesGeneralInformation = fesCategory.getFesGeneralInformations().get(0);
        }
        fesGeneralInformation.setCategoryId(fesCategory);
        fesGeneralInformation.setRecordType(recordType);
        fesGeneralInformationRepository.save(fesGeneralInformation);

        List<FesBankInformationDto> fesBankInformationDtoList = fesCaseSaveDto.getFesCategory().getFesBankInformations();
        if (fesBankInformationDtoList != null && !fesBankInformationDtoList.isEmpty()) {
            List<FesBankInformation> fesBankInformations = new ArrayList<>();
            for (FesBankInformationDto fesBankInformationDto : fesBankInformationDtoList) {
                FesBankInformation fesBankInformation = createOrUpdateFesBankInformation(fesBankInformationDto, fesCategory);

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

        List<FesRefusalReasonDto> fesRefusalReasonDtoList = fesCaseSaveDto.getFesCategory().getFesRefusalReasons();
        List<FesRefusalReason> existingFesRefusalReasons = fesRefusalReasonRepository.findByCategoryId(fesCategory);
        if (fesRefusalReasonDtoList != null && !fesRefusalReasonDtoList.isEmpty()) {
            fesService.deleteMissingItems(fesRefusalReasonDtoList, existingFesRefusalReasons, fesRefusalReasonRepository, FesRefusalReasonDto::getId, FesRefusalReason::getId);
            fesService.createAndSaveAllItems(fesRefusalReasonDtoList, dto -> createOrUpdateFesRefusalReason(dto, fesCategory), fesRefusalReasonRepository, fesCategory);
        } else {
            fesRefusalReasonRepository.deleteAll(existingFesRefusalReasons);
        }

        if (!fesCaseSaveDto.getFesCategory().getFesRefusalCaseDetails().isEmpty()) {
            FesRefusalCaseDetailsDto fesRefusalCaseDetailsDto = fesCaseSaveDto.getFesCategory().getFesRefusalCaseDetails().get(0);
            FesRefusalCaseDetails fesRefusalCaseDetails = fesCategory.getFesRefusalCaseDetails().get(0);
            fesRefusalCaseDetails.setBankInfFeature(getBdById(fesRefusalCaseDetailsDto.getBankInfFeatureId()));
            fesRefusalCaseDetails.setGroundOfRefusal(getBdById(fesRefusalCaseDetailsDto.getGroundOfRefusalId()));
            fesRefusalCaseDetails.setRefusalDate(LocalDateTime.now());
            fesRefusalCaseDetails.setComment(fesRefusalCaseDetailsDto.getComment());
            fesRefusalCaseDetails.setRejectType(getBdById(fesRefusalCaseDetailsDto.getRejectTypeId()));
            fesRefusalCaseDetails.setRemovalReason(fesRefusalCaseDetailsDto.getRemovalReason());
            fesRefusalCaseDetailsRepository.save(fesRefusalCaseDetails);
        }

        List<FesParticipantDto> fesParticipantDtoList = fesCaseSaveDto.getFesCategory().getFesParticipants();
        List<FesParticipant> existingFesParticipants = fesParticipantRepository.findFesParticipantsByCategoryId(fesCategory);
        if (fesParticipantDtoList != null && !fesParticipantDtoList.isEmpty()) {
            Set<Long> fesParticipantDtoIds = fesParticipantDtoList.stream()
                    .map(FesParticipantDto::getId)
                    .collect(Collectors.toSet());
            for (FesParticipant existingFesParticipant : existingFesParticipants) {
                if (!fesParticipantDtoIds.contains(existingFesParticipant.getId())) {
                    fesParticipantRepository.delete(existingFesParticipant);
                }
            }
            List<FesParticipant> fesParticipants = new ArrayList<>();
            for (FesParticipantDto fesParticipantDto : fesParticipantDtoList) {
                FesParticipant fesParticipant = createOrUpdateFesParticipant(fesParticipantDto, fesCategory);

                List<FesEioDto> fesEioDtoList = fesParticipantDto.getFesEios();
                List<FesEio> existingFesEios = fesParticipant.getId() > 0 ?
                        fesEioRepository.findByParticipantId(fesParticipant) : new ArrayList<>();
                if (fesEioDtoList != null && !fesEioDtoList.isEmpty()) {
                    Set<Long> fesEioDtoIds = fesEioDtoList.stream()
                            .map(FesEioDto::getId)
                            .collect(Collectors.toSet());
                    for (FesEio existingFesEio : existingFesEios) {
                        if (!fesEioDtoIds.contains(existingFesEio.getId())) {
                            fesEioRepository.delete(existingFesEio);
                        }
                    }
                    List<FesEio> fesEios = new ArrayList<>();
                    for (FesEioDto fesEioDto : fesEioDtoList) {
                        FesEio fesEio = createOrUpdateFesEio(fesEioDto, fesParticipant);

                        List<FesParticipantLegalDto> fesParticipantLegalDtoList = fesEioDto.getFesParticipantLegals();
                        List<FesParticipantLegal> existingFesParticipantLegals = fesEio.getId() > 0 ?
                                fesParticipantLegalRepository.findByEioId(fesEio) : new ArrayList<>();
                        if (fesParticipantLegalDtoList != null && !fesParticipantLegalDtoList.isEmpty()) {
                            Set<Long> fesParticipantLegalDtoIds = fesParticipantLegalDtoList.stream()
                                    .map(FesParticipantLegalDto::getId)
                                    .collect(Collectors.toSet());
                            for (FesParticipantLegal existingFesParticipantLegal : existingFesParticipantLegals) {
                                if (!fesParticipantLegalDtoIds.contains(existingFesParticipantLegal.getId())) {
                                    fesParticipantLegalRepository.delete(existingFesParticipantLegal);
                                }
                            }
                            List<FesParticipantLegal> fesParticipantLegals = new ArrayList<>();
                            for (FesParticipantLegalDto fesParticipantLegalDto : fesParticipantLegalDtoList) {
                                FesParticipantLegal fesParticipantLegal = createOrUpdateFesParticipantLegal(fesParticipantLegalDto, null, fesEio);
                                fesParticipantLegals.add(fesParticipantLegal);
                            }
                            fesParticipantLegalRepository.saveAll(fesParticipantLegals);
                            fesParticipant.setFesParticipantLegals(fesParticipantLegals);
                        }

                        List<FesParticipantIndividualDto> fesParticipantIndividualDtoList = fesEioDto.getFesParticipantIndividuals();
                        List<FesParticipantIndividual> existingFesParticipantIndividuals = fesEio.getId() > 0 ?
                                fesParticipantIndividualRepository.findByEioId(fesEio) : new ArrayList<>();
                        if (fesParticipantIndividualDtoList != null && !fesParticipantIndividualDtoList.isEmpty()) {
                            Set<Long> fesParticipantIndividualDtoIds = fesParticipantIndividualDtoList.stream()
                                    .map(FesParticipantIndividualDto::getId)
                                    .collect(Collectors.toSet());
                            for (FesParticipantIndividual existingFesParticipantIndividual : existingFesParticipantIndividuals) {
                                if (!fesParticipantIndividualDtoIds.contains(existingFesParticipantIndividual.getId())) {
                                    fesParticipantIndividualRepository.delete(existingFesParticipantIndividual);
                                }
                            }
                            List<FesParticipantIndividual> fesParticipantIndividuals = new ArrayList<>();
                            for (FesParticipantIndividualDto fesParticipantIndividualDto : fesParticipantIndividualDtoList) {
                                FesParticipantIndividual fesParticipantIndividual = createOrUpdateFesParticipantIndividual(fesParticipantIndividualDto, null, null, fesEio);

                                List<FesIdentityDocumentGeneralDto> fesIdentityDocumentGeneralDtoList = fesParticipantIndividualDto.getFesIdentityDocumentGenerals();
                                List<FesIdentityDocumentGeneral> existingFesIdentityDocumentGenerals = fesParticipantIndividual.getId() > 0 ?
                                        fesIdentityDocumentGeneralRepository.findByParticipantIndividualId(fesParticipantIndividual) : new ArrayList<>();
                                if (fesIdentityDocumentGeneralDtoList != null && !fesIdentityDocumentGeneralDtoList.isEmpty()) {
                                    Set<Long> fesIdentityDocumentGeneralDtoIds = fesIdentityDocumentGeneralDtoList.stream()
                                            .map(FesIdentityDocumentGeneralDto::getId)
                                            .collect(Collectors.toSet());
                                    for (FesIdentityDocumentGeneral existingEntity : existingFesIdentityDocumentGenerals) {
                                        if (!fesIdentityDocumentGeneralDtoIds.contains(existingEntity.getId())) {
                                            fesIdentityDocumentGeneralRepository.delete(existingEntity);
                                        }
                                    }
                                    List<FesIdentityDocumentGeneral> fesIdentityDocumentGenerals = new ArrayList<>();
                                    for (FesIdentityDocumentGeneralDto fesIdentityDocumentGeneralDto : fesIdentityDocumentGeneralDtoList) {
                                        FesIdentityDocumentGeneral fesIdentityDocumentGeneral = createOrUpdateFesIdentityDocumentGeneral(fesIdentityDocumentGeneralDto, fesParticipantIndividual);

                                        List<FesIdentityDocumentDto> fesIdentityDocumentDtoList = fesIdentityDocumentGeneralDto.getFesIdentityDocuments();
                                        List<FesIdentityDocument> existingFesIdentityDocuments = fesIdentityDocumentGeneral.getId() > 0 ?
                                                fesIdentityDocumentRepository.findByIdentityDocumentGeneralId(fesIdentityDocumentGeneral) : new ArrayList<>();
                                        if (fesIdentityDocumentDtoList != null && !fesIdentityDocumentDtoList.isEmpty()) {
                                            Set<Long> fesIdentityDocumentDtoIds = fesIdentityDocumentDtoList.stream()
                                                    .map(FesIdentityDocumentDto::getId)
                                                    .collect(Collectors.toSet());
                                            for (FesIdentityDocument existingEntity : existingFesIdentityDocuments) {
                                                if (!fesIdentityDocumentDtoIds.contains(existingEntity.getId())) {
                                                    fesIdentityDocumentRepository.delete(existingEntity);
                                                }
                                            }
                                            List<FesIdentityDocument> fesIdentityDocuments = new ArrayList<>();
                                            for (FesIdentityDocumentDto fesIdentityDocumentDto : fesIdentityDocumentDtoList) {
                                                FesIdentityDocument fesIdentityDocument = createOrUpdateFesIdentityDocument(fesIdentityDocumentDto, fesIdentityDocumentGeneral);
                                                fesIdentityDocuments.add(fesIdentityDocument);
                                            }
                                            fesIdentityDocumentRepository.saveAll(fesIdentityDocuments);
                                            fesIdentityDocumentGeneral.setFesIdentityDocuments(fesIdentityDocuments);
                                        }

                                        List<FesRightOfResidenceDocumentDto> fesRightOfResidenceDocumentDtoList = fesIdentityDocumentGeneralDto.getFesRightOfResidenceDocuments();
                                        List<FesRightOfResidenceDocument> existingFesRightOfResidenceDocuments = fesIdentityDocumentGeneral.getId() > 0 ?
                                                fesRightOfResidenceDocumentRepository.findByIdentityDocumentGeneralId(fesIdentityDocumentGeneral) : new ArrayList<>();
                                        if (fesRightOfResidenceDocumentDtoList != null && !fesRightOfResidenceDocumentDtoList.isEmpty()) {
                                            Set<Long> fesRightOfResidenceDocumentDtoIds = fesRightOfResidenceDocumentDtoList.stream()
                                                    .map(FesRightOfResidenceDocumentDto::getId)
                                                    .collect(Collectors.toSet());
                                            for (FesRightOfResidenceDocument existingEntity : existingFesRightOfResidenceDocuments) {
                                                if (!fesRightOfResidenceDocumentDtoIds.contains(existingEntity.getId())) {
                                                    fesRightOfResidenceDocumentRepository.delete(existingEntity);
                                                }
                                            }
                                            List<FesRightOfResidenceDocument> fesRightOfResidenceDocuments = new ArrayList<>();
                                            for (FesRightOfResidenceDocumentDto fesRightOfResidenceDocumentDto : fesRightOfResidenceDocumentDtoList) {
                                                FesRightOfResidenceDocument fesRightOfResidenceDocument = createOrUpdateFesRightOfResidenceDocument(fesRightOfResidenceDocumentDto, fesIdentityDocumentGeneral);
                                                fesRightOfResidenceDocuments.add(fesRightOfResidenceDocument);
                                            }
                                            fesRightOfResidenceDocumentRepository.saveAll(fesRightOfResidenceDocuments);
                                            fesIdentityDocumentGeneral.setFesRightOfResidenceDocuments(fesRightOfResidenceDocuments);
                                        }
                                        fesIdentityDocumentGenerals.add(fesIdentityDocumentGeneral);
                                    }
                                    fesIdentityDocumentGeneralRepository.saveAll(fesIdentityDocumentGenerals);
                                    fesParticipantIndividual.setFesIdentityDocumentGenerals(fesIdentityDocumentGenerals);
                                }
                                fesParticipantIndividuals.add(fesParticipantIndividual);
                            }
                            fesParticipantIndividualRepository.saveAll(fesParticipantIndividuals);
                            fesParticipant.setFesParticipantIndividuals(fesParticipantIndividuals);
                        }


                        List<FesAddressDto> fesAddressDtoList = fesEioDto.getFesAddresses();
                        List<FesAddress> existingFesAddresses = fesEio.getId() > 0 ?
                                fesAddressRepository.findByEioId(fesEio) : new ArrayList<>();
                        processingAddress(fesAddressDtoList, existingFesAddresses, null, null, fesEio, null);

                        fesEios.add(fesEio);
                    }
                    fesEioRepository.saveAll(fesEios);
                    fesParticipant.setFesEios(fesEios);
                }

                List<FesBeneficiaryDto> fesBeneficiaryDtoList = fesParticipantDto.getFesBeneficiaryList();
                List<FesBeneficiary> existingFesBeneficiaries = fesParticipant.getId() > 0 ?
                        fesBeneficiaryRepository.findByParticipantId(fesParticipant) : new ArrayList<>();
                if (fesBeneficiaryDtoList != null && !fesBeneficiaryDtoList.isEmpty()) {
                    Set<Long> fesBeneficiaryDtoIds = fesBeneficiaryDtoList.stream()
                            .map(FesBeneficiaryDto::getId)
                            .collect(Collectors.toSet());
                    for (FesBeneficiary existingFesBeneficiary : existingFesBeneficiaries) {
                        if (!fesBeneficiaryDtoIds.contains(existingFesBeneficiary.getId())) {
                            fesBeneficiaryRepository.delete(existingFesBeneficiary);
                        }
                    }
                    List<FesBeneficiary> fesBeneficiaries = new ArrayList<>();
                    for (FesBeneficiaryDto fesBeneficiaryDto : fesBeneficiaryDtoList) {
                        FesBeneficiary fesBeneficiary = createOrUpdateFesBeneficiary(fesBeneficiaryDto, fesParticipant);

                        List<FesParticipantIndividualDto> fesParticipantIndividualDtoList = fesBeneficiaryDto.getFesParticipantIndividuals();
                        List<FesParticipantIndividual> existingFesParticipantIndividuals = fesBeneficiary.getId() > 0 ?
                                fesParticipantIndividualRepository.findByBeneficiaryId(fesBeneficiary) : new ArrayList<>();
                        if (fesParticipantIndividualDtoList != null && !fesParticipantIndividualDtoList.isEmpty()) {
                            Set<Long> fesParticipantIndividualDtoIds = fesParticipantIndividualDtoList.stream()
                                    .map(FesParticipantIndividualDto::getId)
                                    .collect(Collectors.toSet());
                            for (FesParticipantIndividual existingFesParticipantIndividual : existingFesParticipantIndividuals) {
                                if (!fesParticipantIndividualDtoIds.contains(existingFesParticipantIndividual.getId())) {
                                    fesParticipantIndividualRepository.delete(existingFesParticipantIndividual);
                                }
                            }
                            List<FesParticipantIndividual> fesParticipantIndividuals = new ArrayList<>();
                            for (FesParticipantIndividualDto fesParticipantIndividualDto : fesParticipantIndividualDtoList) {
                                FesParticipantIndividual fesParticipantIndividual = createOrUpdateFesParticipantIndividual(fesParticipantIndividualDto, null, fesBeneficiary, null);

                                List<FesIdentityDocumentGeneralDto> fesIdentityDocumentGeneralDtoList = fesParticipantIndividualDto.getFesIdentityDocumentGenerals();
                                List<FesIdentityDocumentGeneral> existingFesIdentityDocumentGenerals = fesParticipantIndividual.getId() > 0 ?
                                        fesIdentityDocumentGeneralRepository.findByParticipantIndividualId(fesParticipantIndividual) : new ArrayList<>();
                                if (fesIdentityDocumentGeneralDtoList != null && !fesIdentityDocumentGeneralDtoList.isEmpty()) {
                                    Set<Long> fesIdentityDocumentGeneralDtoIds = fesIdentityDocumentGeneralDtoList.stream()
                                            .map(FesIdentityDocumentGeneralDto::getId)
                                            .collect(Collectors.toSet());
                                    for (FesIdentityDocumentGeneral existingEntity : existingFesIdentityDocumentGenerals) {
                                        if (!fesIdentityDocumentGeneralDtoIds.contains(existingEntity.getId())) {
                                            fesIdentityDocumentGeneralRepository.delete(existingEntity);
                                        }
                                    }
                                    List<FesIdentityDocumentGeneral> fesIdentityDocumentGenerals = new ArrayList<>();
                                    for (FesIdentityDocumentGeneralDto fesIdentityDocumentGeneralDto : fesIdentityDocumentGeneralDtoList) {
                                        FesIdentityDocumentGeneral fesIdentityDocumentGeneral = createOrUpdateFesIdentityDocumentGeneral(fesIdentityDocumentGeneralDto, fesParticipantIndividual);

                                        List<FesIdentityDocumentDto> fesIdentityDocumentDtoList = fesIdentityDocumentGeneralDto.getFesIdentityDocuments();
                                        List<FesIdentityDocument> existingFesIdentityDocuments = fesIdentityDocumentGeneral.getId() > 0 ?
                                                fesIdentityDocumentRepository.findByIdentityDocumentGeneralId(fesIdentityDocumentGeneral) : new ArrayList<>();
                                        if (fesIdentityDocumentDtoList != null && !fesIdentityDocumentDtoList.isEmpty()) {
                                            Set<Long> fesIdentityDocumentDtoIds = fesIdentityDocumentDtoList.stream()
                                                    .map(FesIdentityDocumentDto::getId)
                                                    .collect(Collectors.toSet());
                                            for (FesIdentityDocument existingEntity : existingFesIdentityDocuments) {
                                                if (!fesIdentityDocumentDtoIds.contains(existingEntity.getId())) {
                                                    fesIdentityDocumentRepository.delete(existingEntity);
                                                }
                                            }
                                            List<FesIdentityDocument> fesIdentityDocuments = new ArrayList<>();
                                            for (FesIdentityDocumentDto fesIdentityDocumentDto : fesIdentityDocumentDtoList) {
                                                FesIdentityDocument fesIdentityDocument = createOrUpdateFesIdentityDocument(fesIdentityDocumentDto, fesIdentityDocumentGeneral);
                                                fesIdentityDocuments.add(fesIdentityDocument);
                                            }
                                            fesIdentityDocumentRepository.saveAll(fesIdentityDocuments);
                                            fesIdentityDocumentGeneral.setFesIdentityDocuments(fesIdentityDocuments);
                                        }

                                        List<FesRightOfResidenceDocumentDto> fesRightOfResidenceDocumentDtoList = fesIdentityDocumentGeneralDto.getFesRightOfResidenceDocuments();
                                        List<FesRightOfResidenceDocument> existingFesRightOfResidenceDocuments = fesIdentityDocumentGeneral.getId() > 0 ?
                                                fesRightOfResidenceDocumentRepository.findByIdentityDocumentGeneralId(fesIdentityDocumentGeneral) : new ArrayList<>();
                                        if (fesRightOfResidenceDocumentDtoList != null && !fesRightOfResidenceDocumentDtoList.isEmpty()) {
                                            Set<Long> fesRightOfResidenceDocumentDtoIds = fesRightOfResidenceDocumentDtoList.stream()
                                                    .map(FesRightOfResidenceDocumentDto::getId)
                                                    .collect(Collectors.toSet());
                                            for (FesRightOfResidenceDocument existingEntity : existingFesRightOfResidenceDocuments) {
                                                if (!fesRightOfResidenceDocumentDtoIds.contains(existingEntity.getId())) {
                                                    fesRightOfResidenceDocumentRepository.delete(existingEntity);
                                                }
                                            }
                                            List<FesRightOfResidenceDocument> fesRightOfResidenceDocuments = new ArrayList<>();
                                            for (FesRightOfResidenceDocumentDto fesRightOfResidenceDocumentDto : fesRightOfResidenceDocumentDtoList) {
                                                FesRightOfResidenceDocument fesRightOfResidenceDocument = createOrUpdateFesRightOfResidenceDocument(fesRightOfResidenceDocumentDto, fesIdentityDocumentGeneral);
                                                fesRightOfResidenceDocuments.add(fesRightOfResidenceDocument);
                                            }
                                            fesRightOfResidenceDocumentRepository.saveAll(fesRightOfResidenceDocuments);
                                            fesIdentityDocumentGeneral.setFesRightOfResidenceDocuments(fesRightOfResidenceDocuments);
                                        }
                                        fesIdentityDocumentGenerals.add(fesIdentityDocumentGeneral);
                                    }
                                    fesIdentityDocumentGeneralRepository.saveAll(fesIdentityDocumentGenerals);
                                    fesParticipantIndividual.setFesIdentityDocumentGenerals(fesIdentityDocumentGenerals);
                                }
                                fesParticipantIndividuals.add(fesParticipantIndividual);
                            }
                            fesParticipantIndividualRepository.saveAll(fesParticipantIndividuals);
                            fesParticipant.setFesParticipantIndividuals(fesParticipantIndividuals);
                        }


                        List<FesAddressDto> fesAddressDtoList = fesBeneficiaryDto.getFesAddresses();
                        List<FesAddress> existingFesAddresses = fesBeneficiary.getId() > 0 ?
                                fesAddressRepository.findByBeneficiaryId(fesBeneficiary) : new ArrayList<>();
                        processingAddress(fesAddressDtoList, existingFesAddresses, null, null, null, fesBeneficiary);

                        fesBeneficiaries.add(fesBeneficiary);
                    }
                    fesBeneficiaryRepository.saveAll(fesBeneficiaries);
                    fesParticipant.setFesBeneficiaryList(fesBeneficiaries);
                }

                List<FesParticipantIndividualDto> fesParticipantIndividualDtoList = fesParticipantDto.getFesParticipantIndividuals();
                List<FesParticipantIndividual> existingFesParticipantIndividuals = fesParticipant.getId() > 0 ?
                        fesParticipantIndividualRepository.findByParticipantId(fesParticipant) : new ArrayList<>();
                if (fesParticipantIndividualDtoList != null && !fesParticipantIndividualDtoList.isEmpty()) {
                    Set<Long> fesParticipantIndividualDtoIds = fesParticipantIndividualDtoList.stream()
                            .map(FesParticipantIndividualDto::getId)
                            .collect(Collectors.toSet());
                    for (FesParticipantIndividual existingFesParticipantIndividual : existingFesParticipantIndividuals) {
                        if (!fesParticipantIndividualDtoIds.contains(existingFesParticipantIndividual.getId())) {
                            fesParticipantIndividualRepository.delete(existingFesParticipantIndividual);
                        }
                    }
                    List<FesParticipantIndividual> fesParticipantIndividuals = new ArrayList<>();
                    for (FesParticipantIndividualDto fesParticipantIndividualDto : fesParticipantIndividualDtoList) {
                        FesParticipantIndividual fesParticipantIndividual = createOrUpdateFesParticipantIndividual(fesParticipantIndividualDto, fesParticipant, null, null);

                        List<FesIdentityDocumentGeneralDto> fesIdentityDocumentGeneralDtoList = fesParticipantIndividualDto.getFesIdentityDocumentGenerals();
                        List<FesIdentityDocumentGeneral> existingFesIdentityDocumentGenerals = fesParticipantIndividual.getId() > 0 ?
                                fesIdentityDocumentGeneralRepository.findByParticipantIndividualId(fesParticipantIndividual) : new ArrayList<>();
                        if (fesIdentityDocumentGeneralDtoList != null && !fesIdentityDocumentGeneralDtoList.isEmpty()) {
                            Set<Long> fesIdentityDocumentGeneralDtoIds = fesIdentityDocumentGeneralDtoList.stream()
                                    .map(FesIdentityDocumentGeneralDto::getId)
                                    .collect(Collectors.toSet());
                            for (FesIdentityDocumentGeneral existingEntity : existingFesIdentityDocumentGenerals) {
                                if (!fesIdentityDocumentGeneralDtoIds.contains(existingEntity.getId())) {
                                    fesIdentityDocumentGeneralRepository.delete(existingEntity);
                                }
                            }
                            List<FesIdentityDocumentGeneral> fesIdentityDocumentGenerals = new ArrayList<>();
                            for (FesIdentityDocumentGeneralDto fesIdentityDocumentGeneralDto : fesIdentityDocumentGeneralDtoList) {
                                FesIdentityDocumentGeneral fesIdentityDocumentGeneral = createOrUpdateFesIdentityDocumentGeneral(fesIdentityDocumentGeneralDto, fesParticipantIndividual);

                                List<FesIdentityDocumentDto> fesIdentityDocumentDtoList = fesIdentityDocumentGeneralDto.getFesIdentityDocuments();
                                List<FesIdentityDocument> existingFesIdentityDocuments = fesIdentityDocumentGeneral.getId() > 0 ?
                                        fesIdentityDocumentRepository.findByIdentityDocumentGeneralId(fesIdentityDocumentGeneral) : new ArrayList<>();
                                if (fesIdentityDocumentDtoList != null && !fesIdentityDocumentDtoList.isEmpty()) {
                                    Set<Long> fesIdentityDocumentDtoIds = fesIdentityDocumentDtoList.stream()
                                            .map(FesIdentityDocumentDto::getId)
                                            .collect(Collectors.toSet());
                                    for (FesIdentityDocument existingEntity : existingFesIdentityDocuments) {
                                        if (!fesIdentityDocumentDtoIds.contains(existingEntity.getId())) {
                                            fesIdentityDocumentRepository.delete(existingEntity);
                                        }
                                    }
                                    List<FesIdentityDocument> fesIdentityDocuments = new ArrayList<>();
                                    for (FesIdentityDocumentDto fesIdentityDocumentDto : fesIdentityDocumentDtoList) {
                                        FesIdentityDocument fesIdentityDocument = createOrUpdateFesIdentityDocument(fesIdentityDocumentDto, fesIdentityDocumentGeneral);
                                        fesIdentityDocuments.add(fesIdentityDocument);
                                    }
                                    fesIdentityDocumentRepository.saveAll(fesIdentityDocuments);
                                    fesIdentityDocumentGeneral.setFesIdentityDocuments(fesIdentityDocuments);
                                }

                                List<FesRightOfResidenceDocumentDto> fesRightOfResidenceDocumentDtoList = fesIdentityDocumentGeneralDto.getFesRightOfResidenceDocuments();
                                List<FesRightOfResidenceDocument> existingFesRightOfResidenceDocuments = fesIdentityDocumentGeneral.getId() > 0 ?
                                        fesRightOfResidenceDocumentRepository.findByIdentityDocumentGeneralId(fesIdentityDocumentGeneral) : new ArrayList<>();
                                if (fesRightOfResidenceDocumentDtoList != null && !fesRightOfResidenceDocumentDtoList.isEmpty()) {
                                    Set<Long> fesRightOfResidenceDocumentDtoIds = fesRightOfResidenceDocumentDtoList.stream()
                                            .map(FesRightOfResidenceDocumentDto::getId)
                                            .collect(Collectors.toSet());
                                    for (FesRightOfResidenceDocument existingEntity : existingFesRightOfResidenceDocuments) {
                                        if (!fesRightOfResidenceDocumentDtoIds.contains(existingEntity.getId())) {
                                            fesRightOfResidenceDocumentRepository.delete(existingEntity);
                                        }
                                    }
                                    List<FesRightOfResidenceDocument> fesRightOfResidenceDocuments = new ArrayList<>();
                                    for (FesRightOfResidenceDocumentDto fesRightOfResidenceDocumentDto : fesRightOfResidenceDocumentDtoList) {
                                        FesRightOfResidenceDocument fesRightOfResidenceDocument = createOrUpdateFesRightOfResidenceDocument(fesRightOfResidenceDocumentDto, fesIdentityDocumentGeneral);
                                        fesRightOfResidenceDocuments.add(fesRightOfResidenceDocument);
                                    }
                                    fesRightOfResidenceDocumentRepository.saveAll(fesRightOfResidenceDocuments);
                                    fesIdentityDocumentGeneral.setFesRightOfResidenceDocuments(fesRightOfResidenceDocuments);
                                }
                                fesIdentityDocumentGenerals.add(fesIdentityDocumentGeneral);
                            }
                            fesIdentityDocumentGeneralRepository.saveAll(fesIdentityDocumentGenerals);
                            fesParticipantIndividual.setFesIdentityDocumentGenerals(fesIdentityDocumentGenerals);
                        }
                        fesParticipantIndividuals.add(fesParticipantIndividual);
                    }
                    fesParticipantIndividualRepository.saveAll(fesParticipantIndividuals);
                    fesParticipant.setFesParticipantIndividuals(fesParticipantIndividuals);
                }

                List<FesParticipantLegalDto> fesParticipantLegalDtoList = fesParticipantDto.getFesParticipantLegals();
                List<FesParticipantLegal> existingFesParticipantLegals = fesParticipant.getId() > 0 ?
                        fesParticipantLegalRepository.findByParticipantId(fesParticipant) : new ArrayList<>();
                if (fesParticipantLegalDtoList != null && !fesParticipantLegalDtoList.isEmpty()) {
                    Set<Long> fesParticipantLegalDtoIds = fesParticipantLegalDtoList.stream()
                            .map(FesParticipantLegalDto::getId)
                            .collect(Collectors.toSet());
                    for (FesParticipantLegal existingFesParticipantLegal : existingFesParticipantLegals) {
                        if (!fesParticipantLegalDtoIds.contains(existingFesParticipantLegal.getId())) {
                            fesParticipantLegalRepository.delete(existingFesParticipantLegal);
                        }
                    }
                    List<FesParticipantLegal> fesParticipantLegals = new ArrayList<>();
                    for (FesParticipantLegalDto fesParticipantLegalDto : fesParticipantLegalDtoList) {
                        FesParticipantLegal fesParticipantLegal = createOrUpdateFesParticipantLegal(fesParticipantLegalDto, fesParticipant, null);
                        fesParticipantLegals.add(fesParticipantLegal);
                    }
                    fesParticipantLegalRepository.saveAll(fesParticipantLegals);
                    fesParticipant.setFesParticipantLegals(fesParticipantLegals);
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

        List<FesAddressDto> fesAddressDtoList = fesCaseSaveDto.getFesCategory().getFesAddresses();
        List<FesAddress> existingFesAddresses = fesAddressRepository.findByCategoryId(fesCategory);
        processingAddress(fesAddressDtoList, existingFesAddresses, fesCategory, null, null, null);

        if (fesCategory.getCaseId() != null) {
            delegateExecution.setVariable("caseId", fesCategory.getCaseId().getId());
        }
        System.out.println("Кейс сохранен");
    }

    private void processingAddress(List<FesAddressDto> fesAddressDtoList, List<FesAddress> existingFesAddresses, FesCategory fesCategory, FesParticipant fesParticipant, FesEio fesEio, FesBeneficiary fesBeneficiary) {
        if (fesAddressDtoList != null && !fesAddressDtoList.isEmpty()) {
            Set<Long> fesAddressDtoIds = fesAddressDtoList.stream()
                    .map(FesAddressDto::getId)
                    .collect(Collectors.toSet());
            for (FesAddress existingFesAddress : existingFesAddresses) {
                if (!fesAddressDtoIds.contains(existingFesAddress.getId())) {
                    fesAddressRepository.delete(existingFesAddress);
                }
            }
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

    private BaseDictionary getBdById(Long id) {
        if (id == null) {
            return null;
        }
        return baseDictionaryRepository.findById(id).orElse(null);
    }

    private FesRefusalReason createOrUpdateFesRefusalReason(FesRefusalReasonDto fesRefusalReasonDto, FesCategory fesCategory) {
        FesRefusalReason fesRefusalReason = new FesRefusalReason();

        if (fesRefusalReasonDto.getId() != null) {
            fesRefusalReason = fesRefusalReasonRepository.findById(fesRefusalReasonDto.getId()).orElse(fesRefusalReason);
        }

        fesRefusalReason.setCategoryId(fesCategory);
        fesRefusalReason.setRefusalReason(getBdById(fesRefusalReasonDto.getRefusalReasonId()));
        fesRefusalReason.setRefusalReasonOthername(fesRefusalReasonDto.getRefusalReasonOthername());

        return fesRefusalReason;
    }

    private FesParticipant createOrUpdateFesParticipant(FesParticipantDto fesParticipantDto, FesCategory fesCategory) {
        FesParticipant fesParticipant = new FesParticipant();

        if (fesParticipantDto.getId() != null) {
            fesParticipant = fesParticipantRepository.findById(fesParticipantDto.getId()).orElse(fesParticipant);
        }
        fesParticipant.setCategoryId(fesCategory);
        fesParticipant.setParticipantStatus(getBdById(fesParticipantDto.getParticipantStatusId()));
        fesParticipant.setParticipantType(getBdById(fesParticipantDto.getParticipantTypeId()));
        fesParticipant.setParticipantResidentFeature(getBdById(fesParticipantDto.getParticipantResidentFeatureId()));
        fesParticipant.setParticipantFeature(getBdById(fesParticipantDto.getParticipantFeatureId()));
        fesParticipant.setParticipantFrommuUuid(fesParticipantDto.getParticipantFrommuUuid());
        fesParticipant.setParticipantCode(getBdById(fesParticipantDto.getParticipantCodeId()));

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
        fesParticipantLegal.setBranchFeature(getBdById(fesParticipantLegalDto.getBranchFeatureId()));
        fesParticipantLegal.setInn(fesParticipantLegalDto.getInn());
        fesParticipantLegal.setKpp(fesParticipantLegalDto.getKpp());
        fesParticipantLegal.setOgrn(fesParticipantLegalDto.getOgrn());
        fesParticipantLegal.setRegistrationDate(fesParticipantLegalDto.getRegistrationDate());
        return fesParticipantLegal;
    }

    private FesParticipantIndividual createOrUpdateFesParticipantIndividual(FesParticipantIndividualDto fesParticipantIndividualDto, FesParticipant fesParticipant, FesBeneficiary fesBeneficiary, FesEio fesEio) {
        FesParticipantIndividual fesParticipantIndividual = new FesParticipantIndividual();
        if (fesParticipantIndividualDto.getId() != null) {
            fesParticipantIndividual = fesParticipantIndividualRepository.findById(fesParticipantIndividualDto.getId()).orElse(fesParticipantIndividual);
        }
        fesParticipantIndividual.setParticipantId(fesParticipant);
        fesParticipantIndividual.setBeneficiaryId(fesBeneficiary);
        fesParticipantIndividual.setEioId(fesEio);
        fesParticipantIndividual.setPhysicalIdentificationFeature(getBdById(fesParticipantIndividualDto.getPhysicalIdentificationFeatureId()));
        fesParticipantIndividual.setLastName(fesParticipantIndividualDto.getLastName());
        fesParticipantIndividual.setFirstName(fesParticipantIndividualDto.getFirstName());
        fesParticipantIndividual.setMiddleName(fesParticipantIndividualDto.getMiddleName());
        fesParticipantIndividual.setInn(fesParticipantIndividualDto.getInn());
        fesParticipantIndividual.setSnils(fesParticipantIndividualDto.getSnils());
        fesParticipantIndividual.setHealthCardNum(fesParticipantIndividualDto.getHealthCardNum());
        fesParticipantIndividual.setPhoneNumber(fesParticipantIndividualDto.getPhoneNumber());
        fesParticipantIndividual.setPrivatePractitionerType(getBdById(fesParticipantIndividualDto.getPrivatePractitionerTypeId()));
        fesParticipantIndividual.setPrivatePractitionerRegNum(fesParticipantIndividualDto.getPrivatePractitionerRegNum());
        fesParticipantIndividual.setFullName(fesParticipantIndividualDto.getFullName());
        fesParticipantIndividual.setBirthDate(fesParticipantIndividualDto.getBirthDate());
        fesParticipantIndividual.setCitizenshipCountryCode(getBdById(fesParticipantIndividualDto.getCitizenshipCountryCodeId()));
        fesParticipantIndividual.setPublicFigureFeature(getBdById(fesParticipantIndividualDto.getPublicFigureFeatureId()));
        fesParticipantIndividual.setIdentityDocumentFeature(getBdById(fesParticipantIndividualDto.getIdentityDocumentFeatureId()));

        return fesParticipantIndividual;
    }

    private FesIdentityDocumentGeneral createOrUpdateFesIdentityDocumentGeneral(FesIdentityDocumentGeneralDto dto, FesParticipantIndividual rootEntity) {
        FesIdentityDocumentGeneral fesIdentityDocumentGeneral = new FesIdentityDocumentGeneral();
        if (dto.getId() != null) {
            fesIdentityDocumentGeneral = fesIdentityDocumentGeneralRepository.findById(dto.getId()).orElse(fesIdentityDocumentGeneral);
        }
        fesIdentityDocumentGeneral.setParticipantIndividualId(rootEntity);
        fesIdentityDocumentGeneral.setDocumentTypeCode(getBdById(dto.getDocumentTypeCodeId()));
        fesIdentityDocumentGeneral.setOtherDocumentName(dto.getOtherDocumentName());
        fesIdentityDocumentGeneral.setDocumentSeries(dto.getDocumentSeries());
        fesIdentityDocumentGeneral.setDocumentNum(dto.getDocumentNum());
        fesIdentityDocumentGeneral.setIdentityDocumentType(getBdById(dto.getIdentityDocumentTypeId()));

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
        entity.setEioType(getBdById(dto.getEioTypeId()));
        entity.setEioResidentFeature(getBdById(dto.getEioResidentFeatureId()));

        return entity;
    }

    private FesBeneficiary createOrUpdateFesBeneficiary(FesBeneficiaryDto dto, FesParticipant rootEntity) {
        FesBeneficiary entity = new FesBeneficiary();
        if (dto.getId() != null) {
            entity = fesBeneficiaryRepository.findById(dto.getId()).orElse(entity);
        }
        entity.setParticipantId(rootEntity);
        entity.setBeneficiaryType(getBdById(dto.getBeneficiaryTypeId()));
        entity.setBeneficiaryResidentFeature(getBdById(dto.getBeneficiaryResidentFeatureId()));

        return entity;
    }

    private FesRightOfResidenceDocument createOrUpdateFesRightOfResidenceDocument(FesRightOfResidenceDocumentDto dto, FesIdentityDocumentGeneral rootEntity) {
        FesRightOfResidenceDocument fesRightOfResidenceDocument = new FesRightOfResidenceDocument();
        if (dto.getId() != null) {
            fesRightOfResidenceDocument = fesRightOfResidenceDocumentRepository.findById(dto.getId()).orElse(fesRightOfResidenceDocument);
        }
        fesRightOfResidenceDocument.setIdentityDocumentGeneralId(rootEntity);
        fesRightOfResidenceDocument.setStartStayDate(dto.getStartStayDate());
        fesRightOfResidenceDocument.setEndStayDate(dto.getEndStayDate());

        return fesRightOfResidenceDocument;
    }

    private FesAddress createOrUpdateFesAddress(FesAddressDto fesAddressDto, FesCategory fesCategory,
                                                FesParticipant fesParticipant, FesEio fesEio, FesBeneficiary fesBeneficiary) {
        FesAddress fesAddress = new FesAddress();

        if (fesAddressDto.getId() != null) {
            fesAddress = fesAddressRepository.findById(fesAddressDto.getId()).orElse(fesAddress);
        }
        fesAddress.setCategoryId(fesCategory);
        fesAddress.setAddressType(getBdById(fesAddressDto.getAddressTypeId()));
        fesAddress.setPostal(fesAddressDto.getPostal());
        fesAddress.setCountryCode(getBdById(fesAddressDto.getCountryCodeId()));
        fesAddress.setOkato(getBdById(fesAddressDto.getOkatoId()));
        fesAddress.setDistrict(fesAddressDto.getDistrict());
        fesAddress.setTownship(fesAddressDto.getTownship());
        fesAddress.setStreet(fesAddressDto.getStreet());
        fesAddress.setHouse(fesAddressDto.getHouse());
        fesAddress.setCorpus(fesAddressDto.getCorpus());
        fesAddress.setRoom(fesAddressDto.getRoom());
        fesAddress.setAddressText(fillAddressText(fesAddressDto));
        fesAddress.setParticipantId(fesParticipant);
        fesAddress.setEioId(fesEio);
        fesAddress.setBeneficiaryId(fesBeneficiary);

        return fesAddress;
    }

    private String fillAddressText(FesAddressDto fesAddress) {
        if (fesAddress.getAddressText() == null || fesAddress.getAddressText().isEmpty()) {
            String postal = (fesAddress.getPostal() != null) ? fesAddress.getPostal() : "";
            String countryCode = (fesAddress.getCountryCodeId() != null) ? String.valueOf(fesAddress.getCountryCodeId()) : "";
            String okato = (fesAddress.getOkatoId() != null) ? String.valueOf(fesAddress.getOkatoId()) : "";
            String district = (fesAddress.getDistrict() != null) ? fesAddress.getDistrict() : "";
            String township = (fesAddress.getTownship() != null) ? fesAddress.getTownship() : "";
            String street = (fesAddress.getStreet() != null) ? fesAddress.getStreet() : "";
            String house = (fesAddress.getHouse() != null) ? fesAddress.getHouse() : "";
            String corpus = (fesAddress.getCorpus() != null) ? fesAddress.getCorpus() : "";
            String room = (fesAddress.getRoom() != null) ? fesAddress.getRoom() : "";
            return String.join(",", postal, countryCode, okato, district, township, street, house, corpus, room);
        } else {
            return fesAddress.getAddressText();
        }
    }

    private FesBankInformation createOrUpdateFesBankInformation(FesBankInformationDto fesBankInformationDto, FesCategory fesCategory) {
        FesBankInformation fesBankInformation = new FesBankInformation();

        if (fesBankInformationDto.getId() != null) {
            fesBankInformation = fesBankInformationRepository.findById(fesBankInformationDto.getId()).orElse(fesBankInformation);
        }

        fesBankInformation.setCategoryId(fesCategory);
        fesBankInformation.setReportingAttribute(fesBankInformationDto.getReportingAttribute());
        fesBankInformation.setBankRegNum(fesBankInformationDto.getBankRegNum());
        fesBankInformation.setBankBic(fesBankInformationDto.getBankBic());
        fesBankInformation.setBankOcato(fesBankInformationDto.getBankOcato());

        return fesBankInformation;
    }

    private FesBranchInformation createOrUpdateFesBranchInformation(FesBranchInformationDto fesBranchInformationDto, FesBankInformation fesBankInformation) {
        FesBranchInformation fesBranchInformation = new FesBranchInformation();

        if (fesBranchInformationDto.getId() != null) {
            fesBranchInformation = fesBranchInformationRepository.findById(fesBranchInformationDto.getId()).orElse(fesBranchInformation);
        }
        fesBranchInformation.setBankInformationId(fesBankInformation);
        fesBranchInformation.setBranchNum(fesBranchInformationDto.getBranchNum());
        fesBranchInformation.setTransferringBranchNum(fesBranchInformationDto.getTransferringBranchNum());

        return fesBranchInformation;
    }
}
