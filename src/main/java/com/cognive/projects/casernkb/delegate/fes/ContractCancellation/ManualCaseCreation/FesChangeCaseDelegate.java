package com.cognive.projects.casernkb.delegate.fes.ContractCancellation.ManualCaseCreation;

import com.cognive.projects.casernkb.model.fes.FesAddressDto;
import com.cognive.projects.casernkb.model.fes.FesBankInformationDto;
import com.cognive.projects.casernkb.model.fes.FesCaseSaveDto;
import com.cognive.projects.casernkb.model.fes.FesParticipantDto;
import com.cognive.projects.casernkb.model.fes.FesParticipantIndividualDto;
import com.cognive.projects.casernkb.model.fes.FesParticipantLegalDto;
import com.cognive.projects.casernkb.model.fes.FesRefusalCaseDetailsDto;
import com.cognive.projects.casernkb.model.fes.FesRefusalReasonDto;
import com.cognive.projects.casernkb.model.fes.FesServiceInformationDto;
import com.prime.db.rnkb.model.BaseDictionary;
import com.prime.db.rnkb.model.fes.FesAddress;
import com.prime.db.rnkb.model.fes.FesBankInformation;
import com.prime.db.rnkb.model.fes.FesParticipant;
import com.prime.db.rnkb.model.fes.FesParticipantIndividual;
import com.prime.db.rnkb.model.fes.FesParticipantLegal;
import com.prime.db.rnkb.model.fes.FesRefusalCaseDetails;
import com.prime.db.rnkb.model.fes.FesRefusalReason;
import com.prime.db.rnkb.model.fes.FesServiceInformation;
import com.prime.db.rnkb.repository.BaseDictionaryRepository;
import com.prime.db.rnkb.repository.fes.FesAddressRepository;
import com.prime.db.rnkb.repository.fes.FesBankInformationRepository;
import com.prime.db.rnkb.repository.fes.FesBeneficiaryRepository;
import com.prime.db.rnkb.repository.fes.FesCategoryRepository;
import com.prime.db.rnkb.repository.fes.FesEioRepository;
import com.prime.db.rnkb.repository.fes.FesParticipantIndividualRepository;
import com.prime.db.rnkb.repository.fes.FesParticipantLegalRepository;
import com.prime.db.rnkb.repository.fes.FesParticipantRepository;
import com.prime.db.rnkb.repository.fes.FesRefusalCaseDetailsRepository;
import com.prime.db.rnkb.repository.fes.FesRefusalReasonRepository;
import com.prime.db.rnkb.repository.fes.FesServiceInformationRepository;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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
    private final FesEioRepository fesEioRepository;
    private final FesBeneficiaryRepository fesBeneficiaryRepository;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        var categoryId = (Long) delegateExecution.getVariable("categoryId");

        var fesCategory = fesCategoryRepository.findById(categoryId).get();
        var fesCaseSaveDto = (FesCaseSaveDto) delegateExecution.getVariable("fesCaseSaveDto");

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
            fesServiceInformation.setDate(fesServiceInformationDto.getDate());
            fesServiceInformation.setOfficerPosition(fesServiceInformationDto.getOfficerPosition());
            fesServiceInformation.setOfficerLastname(fesServiceInformationDto.getOfficerLastName());
            fesServiceInformation.setOfficerFirstname(fesServiceInformationDto.getOfficerFirstName());
            fesServiceInformation.setOfficerMiddlename(fesServiceInformationDto.getOfficerMiddleName());
            fesServiceInformation.setOfficerPhone(fesServiceInformationDto.getOfficerPhone());
            fesServiceInformation.setOfficerMail(fesServiceInformationDto.getOfficerMail());
            fesServiceInformation.setOfficerFullName(fesServiceInformationDto.getOfficerFullName());
            fesServiceInformationRepository.save(fesServiceInformation);
        }

        if (!fesCaseSaveDto.getFesCategory().getFesBankInformations().isEmpty()) {
            FesBankInformationDto fesBankInformationDto = fesCaseSaveDto.getFesCategory().getFesBankInformations().get(0);
            FesBankInformation fesBankInformation;
            if (fesCategory.getFesBankInformations() == null || fesCategory.getFesBankInformations().isEmpty()) {
                fesBankInformation = new FesBankInformation();
                fesBankInformation.setCategoryId(fesCategory);
            } else {
                fesBankInformation = fesCategory.getFesBankInformations().get(0);
            }
            fesBankInformation.setReportingAttribute(fesBankInformationDto.getReportingAttribute());
            fesBankInformation.setBankRegNum(fesBankInformationDto.getBankRegNum());
            fesBankInformation.setBankBic(fesBankInformationDto.getBankBic());
            fesBankInformation.setBankOcato(fesBankInformationDto.getBankOcato());
            fesBankInformationRepository.save(fesBankInformation);
        }

        if(!fesCaseSaveDto.getFesCategory().getFesRefusalReasons().isEmpty()) {
            FesRefusalReasonDto fesRefusalReasonDto = fesCaseSaveDto.getFesCategory().getFesRefusalReasons().get(0);
            FesRefusalReason fesRefusalReason;
            if (fesCategory.getFesRefusalReasons() == null || fesCategory.getFesRefusalReasons().isEmpty()) {
                fesRefusalReason = new FesRefusalReason();
                fesRefusalReason.setCategoryId(fesCategory);
            } else {
                fesRefusalReason = fesCategory.getFesRefusalReasons().get(0);
            }
            fesRefusalReason.setRefusalReason(getBdById(fesRefusalReasonDto.getRefusalReasonId()));
            fesRefusalReason.setRefusalReasonOthername(fesRefusalReasonDto.getRefusalReasonOthername());
            fesRefusalReasonRepository.save(fesRefusalReason);
        }

        if (!fesCaseSaveDto.getFesCategory().getFesRefusalCaseDetails().isEmpty()) {
            FesRefusalCaseDetailsDto fesRefusalCaseDetailsDto = fesCaseSaveDto.getFesCategory().getFesRefusalCaseDetails().get(0);
            FesRefusalCaseDetails fesRefusalCaseDetails = fesCategory.getFesRefusalCaseDetails().get(0);
            fesRefusalCaseDetails.setBankInfFeature(getBdById(fesRefusalCaseDetailsDto.getBankInfFeatureId()));
            fesRefusalCaseDetails.setGroundOfRefusal(getBdById(fesRefusalCaseDetailsDto.getGroundOfRefusalId()));
            fesRefusalCaseDetails.setRefusalDate(fesRefusalCaseDetailsDto.getRefusalDate());
            fesRefusalCaseDetails.setComment(fesRefusalCaseDetailsDto.getComment());
            fesRefusalCaseDetails.setRejectType(getBdById(fesRefusalCaseDetailsDto.getRejectTypeId()));
            fesRefusalCaseDetails.setRemovalReason(fesRefusalCaseDetailsDto.getRemovalReason());
            fesRefusalCaseDetailsRepository.save(fesRefusalCaseDetails);
        }

        List<FesParticipantDto> fesParticipantDtoList = fesCaseSaveDto.getFesCategory().getFesParticipants();
        if (fesParticipantDtoList != null && !fesParticipantDtoList.isEmpty()) {
            for (FesParticipantDto fesParticipantDto : fesParticipantDtoList) {
                FesParticipant fesParticipant = fesParticipantRepository.findById(fesParticipantDto.getId()).orElse(null);
                if (fesParticipant == null) {
                    fesParticipant = new FesParticipant();
                    fesParticipant.setCategoryId(fesCategory);
                }
                fesParticipant.setParticipantStatus(getBdById(fesParticipantDto.getParticipantStatusId()));
                fesParticipant.setParticipantType(getBdById(fesParticipantDto.getParticipantTypeId()));
                fesParticipant.setParticipantResidentFeature(getBdById(fesParticipantDto.getParticipantResidentFeatureId()));
                fesParticipant.setParticipantFeature(getBdById(fesParticipantDto.getParticipantFeatureId()));
                fesParticipant.setParticipantFrommuUuid(fesParticipantDto.getParticipantFrommuUuid());
                fesParticipant.setParticipantCode(getBdById(fesParticipantDto.getParticipantCodeId()));

                List<FesParticipantLegalDto> fesParticipantLegalDtoList = fesParticipantDto.getFesParticipantLegals();
                List<FesParticipantLegal> fesParticipantLegals = new ArrayList<>();
                if (fesParticipantLegalDtoList != null && !fesParticipantLegalDtoList.isEmpty()) {
                    for (FesParticipantLegalDto fesParticipantLegalDto : fesParticipantLegalDtoList) {
                        FesParticipantLegal fesParticipantLegal = fesParticipantLegalRepository.findById(fesParticipantLegalDto.getId()).orElse(null);
                        if (fesParticipantLegal == null) {
                            fesParticipantLegal = new FesParticipantLegal();
                            fesParticipantLegal.setParticipantId(fesParticipant);
                        }
                        fesParticipantLegal.setParticipantLegalName(fesParticipantLegalDto.getParticipantLegalName());
                        fesParticipantLegal.setBranchFeature(getBdById(fesParticipantLegalDto.getBranchFeatureId()));
                        fesParticipantLegal.setInn(fesParticipantLegalDto.getInn());
                        fesParticipantLegal.setKpp(fesParticipantLegalDto.getKpp());
                        fesParticipantLegal.setOgrn(fesParticipantLegalDto.getOgrn());
                        fesParticipantLegal.setRegistrationDate(fesParticipantLegalDto.getRegistrationDate());

                        fesParticipantLegalRepository.save(fesParticipantLegal);
                        fesParticipantLegals.add(fesParticipantLegal);
                    }
                }
                fesParticipant.setFesParticipantLegals(fesParticipantLegals);
                fesParticipantRepository.save(fesParticipant);

                List<FesParticipantIndividualDto> fesParticipantIndividualDtoList = fesParticipantDto.getFesParticipantIndividuals();
                List<FesParticipantIndividual> fesParticipantIndividuals = new ArrayList<>();
                if (fesParticipantIndividualDtoList != null && !fesParticipantIndividualDtoList.isEmpty()) {
                    for (FesParticipantIndividualDto fesParticipantIndividualDto : fesParticipantIndividualDtoList) {
                        FesParticipantIndividual fesParticipantIndividual = fesParticipantIndividualRepository.findById(fesParticipantIndividualDto.getId()).orElse(null);
                        if (fesParticipantIndividual == null) {
                            fesParticipantIndividual = new FesParticipantIndividual();
                            fesParticipantIndividual.setParticipantId(fesParticipant);
                        }
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

                        fesParticipantIndividualRepository.save(fesParticipantIndividual);
                        fesParticipantIndividuals.add(fesParticipantIndividual);
                    }
                }
                fesParticipant.setFesParticipantLegals(fesParticipantLegals);
                fesParticipant.setFesParticipantIndividuals(fesParticipantIndividuals);
                fesParticipantRepository.save(fesParticipant);
            }
        }

        List<FesAddressDto> fesAddressDtoList = fesCaseSaveDto.getFesCategory().getFesAddresses();
        List<FesAddress> fesAddressList = new ArrayList<>();
        if (fesAddressDtoList != null && !fesAddressDtoList.isEmpty()) {
            for (FesAddressDto fesAddressDto : fesAddressDtoList) {
                FesAddress fesAddress = fesAddressRepository.findById(fesAddressDto.getId()).orElse(null);
                if (fesAddress == null) {
                    fesAddress = new FesAddress();
                }
                fesAddress.setCategoryId(fesAddressDto.getCategoryId() != null ? fesCategory : null);
                fesAddress.setAddressType(getBdById(fesAddressDto.getAddressTypeId()));
                fesAddress.setPostal(fesAddressDto.getPostal());
                fesAddress.setCountryCode(getBdById(fesAddressDto.getCountryCodeId()));
                fesAddress.setOkato(getBdById(fesAddressDto.getOkatoId()));
                fesAddress.setDistrict(fesAddressDto.getDistrict());
                fesAddress.setTownship(fesAddressDto.getTownship());
                fesAddress.setStreet(fesAddressDto.getTownship());
                fesAddress.setHouse(fesAddressDto.getHouse());
                fesAddress.setCorpus(fesAddressDto.getCorpus());
                fesAddress.setRoom(fesAddressDto.getRoom());
                fesAddress.setAddressText(fesAddressDto.getAddressText());
                fesAddress.setParticipantId(fesAddressDto.getParticipantId() != null ?
                        fesParticipantRepository.findById(fesAddressDto.getParticipantId()).orElse(null):null);
                fesAddress.setEioId(fesAddressDto.getEioId() != null ?
                        fesEioRepository.findById(fesAddressDto.getEioId()).orElse(null):null);
                fesAddress.setBeneficiaryId(fesAddressDto.getBeneficiaryId() != null ?
                        fesBeneficiaryRepository.findById(fesAddressDto.getBeneficiaryId()).orElse(null):null);

                fesAddressList.add(fesAddress);
                fesAddressRepository.save(fesAddress);
            }
            fesCategory.setFesAddressList(fesAddressList);
            fesCategoryRepository.save(fesCategory);
        }

        if (fesCategory.getCaseId() != null) {
            delegateExecution.setVariable("caseId", fesCategory.getCaseId().getId());
        }
        System.out.println("Кейс сохранен");
    }

    private BaseDictionary getBdById(Long id) {
        if (id == null) {
            return null;
        }
        return baseDictionaryRepository.findById(id).orElse(null);
    }
}
