package com.cognive.projects.casernkb.service;

import com.cognive.projects.casernkb.constant.FesConstants;
import com.cognive.projects.casernkb.model.fes.FesCaseSaveDto;
import com.prime.db.rnkb.model.Address;
import com.prime.db.rnkb.model.BaseDictionary;
import com.prime.db.rnkb.model.Case;
import com.prime.db.rnkb.model.Client;
import com.prime.db.rnkb.model.ClientIndividual;
import com.prime.db.rnkb.model.ClientRelation;
import com.prime.db.rnkb.model.Payment;
import com.prime.db.rnkb.model.SysUser;
import com.prime.db.rnkb.model.VerificationDocument;
import com.prime.db.rnkb.model.fes.FesAddress;
import com.prime.db.rnkb.model.fes.FesBeneficiary;
import com.prime.db.rnkb.model.fes.FesCasesStatus;
import com.prime.db.rnkb.model.fes.FesCashMoneyTransfers;
import com.prime.db.rnkb.model.fes.FesCategory;
import com.prime.db.rnkb.model.fes.FesEio;
import com.prime.db.rnkb.model.fes.FesIdentityDocument;
import com.prime.db.rnkb.model.fes.FesIdentityDocumentGeneral;
import com.prime.db.rnkb.model.fes.FesMainPageNew;
import com.prime.db.rnkb.model.fes.FesMainPageOtherSections;
import com.prime.db.rnkb.model.fes.FesMainPageUserDecision;
import com.prime.db.rnkb.model.fes.FesParticipant;
import com.prime.db.rnkb.model.fes.FesParticipantIndividual;
import com.prime.db.rnkb.model.fes.FesParticipantLegal;
import com.prime.db.rnkb.model.fes.FesRefusalCaseDetails;
import com.prime.db.rnkb.model.fes.FesRightOfResidenceDocument;
import com.prime.db.rnkb.repository.AddressRepository;
import com.prime.db.rnkb.repository.BaseDictionaryRepository;
import com.prime.db.rnkb.repository.CaseRepository;
import com.prime.db.rnkb.repository.VerificationDocumentRepository;
import com.prime.db.rnkb.repository.fes.FesAddressRepository;
import com.prime.db.rnkb.repository.fes.FesBeneficiaryRepository;
import com.prime.db.rnkb.repository.fes.FesCasesStatusRepository;
import com.prime.db.rnkb.repository.fes.FesCategoryRepository;
import com.prime.db.rnkb.repository.fes.FesEioRepository;
import com.prime.db.rnkb.repository.fes.FesIdentityDocumentGeneralRepository;
import com.prime.db.rnkb.repository.fes.FesIdentityDocumentRepository;
import com.prime.db.rnkb.repository.fes.FesMainPageNewRepository;
import com.prime.db.rnkb.repository.fes.FesMainPageOtherSectionsRepository;
import com.prime.db.rnkb.repository.fes.FesMainPageUserDecisionRepository;
import com.prime.db.rnkb.repository.fes.FesParticipantIndividualRepository;
import com.prime.db.rnkb.repository.fes.FesParticipantLegalRepository;
import com.prime.db.rnkb.repository.fes.FesParticipantRepository;
import com.prime.db.rnkb.repository.fes.FesRefusalCaseDetailsRepository;
import com.prime.db.rnkb.repository.fes.FesRightOfResidenceDocumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_14;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_18;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_305;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_309;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_323;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_325;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_331;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_337;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_38;
import static com.cognive.projects.casernkb.constant.FesConstants.SUBNAME_CONTRACT_REJECTION;
import static com.cognive.projects.casernkb.constant.FesConstants.SUBNAME_FREEZING;

@Service
@RequiredArgsConstructor
@Slf4j
public class FesService {
    private final CaseRepository caseRepository;
    private final FesMainPageUserDecisionRepository fesMainPageUserDecisionRepository;
    private final FesMainPageOtherSectionsRepository fesMainPageOtherSectionsRepository;
    private final FesMainPageNewRepository fesMainPageNewRepository;
    private final FesCasesStatusRepository fesCasesStatusRepository;
    private final FesRefusalCaseDetailsRepository fesRefusalCaseDetailsRepository;
    private final FesEioRepository fesEioRepository;
    private static final String ADDRESS_REGISTRATION = "1";
    private static final String ADDRESS_LOCATION = "2";
    private static final String DOC_PASSPORT_RF = "21";
    private static final String DOC_PASSPORT_ZAGRAN = "22";
    private static final String[] DOC_RF = {"21", "22", "26", "27"};
    private static final String LEGAL = "Legal";
    private static final String FOREIGN = "Foreign";
    private static final String INDIVIDUAL = "Individual";
    private static final String WRONG_CLIENT_TYPE = "0";
    private static final String UNKNOWN_RESIDENCE_STATUS = "9";
    private static final String RESIDENT = "1";
    private static final String NON_RESIDENT = "0";

    private final AddressRepository addressRepository;
    private final FesParticipantRepository fesParticipantRepository;
    private final VerificationDocumentRepository verificationDocumentRepository;
    private final BaseDictionaryRepository baseDictionaryRepository;
    private final FesAddressRepository fesAddressRepository;
    private final FesParticipantLegalRepository fesParticipantLegalRepository;
    private final FesRightOfResidenceDocumentRepository fesRightOfResidenceDocumentRepository;
    private final FesIdentityDocumentRepository fesIdentityDocumentRepository;
    private final FesParticipantIndividualRepository fesParticipantIndividualRepository;
    private final FesIdentityDocumentGeneralRepository fesIdentityDocumentGeneralRepository;
    private final FesBeneficiaryRepository fesBeneficiaryRepository;
    private final FesCategoryRepository fesCategoryRepository;

    public void addParticipantIndividualGeneric(FesParticipant fesParticipant,
                                                FesBeneficiary fesBeneficiary,
                                                FesEio fesEio,
                                                Client client) {
        ClientIndividual clientIndividual = client.getClientIndividual();
        List<Address> clientAddresses = addressRepository.findAllByClient(client);
        Address clientAddress = findMainAddress(clientAddresses);
        List<VerificationDocument> verificationDocuments = verificationDocumentRepository.findAllByClient(client);
        VerificationDocument verificationDocument = findMainDocument(verificationDocuments);

        var physicalIdentificationFeature = getBd(DICTIONARY_325, "1");
        var addressType = getBd(DICTIONARY_331, "8");

        FesParticipantIndividual fesParticipantIndividual = getFesParticipantIndividual(fesParticipant, fesBeneficiary, fesEio,
                physicalIdentificationFeature, client, clientIndividual, clientAddress);

        FesIdentityDocumentGeneral fesIdentityDocumentGeneral = getFesIdentityDocumentGeneral(fesParticipantIndividual, verificationDocument);
        saveDocByType(fesIdentityDocumentGeneral, verificationDocument);

        addAddress(null, fesParticipant, fesBeneficiary, fesEio, addressType, clientAddress);
    }

    private void saveDocByType(FesIdentityDocumentGeneral fesIdentityDocumentGeneral, VerificationDocument verificationDocument) {
        if (fesIdentityDocumentGeneral.getIdentityDocumentType() != null
                && fesIdentityDocumentGeneral.getIdentityDocumentType().equals(getBd(DICTIONARY_337, "1"))) {
            getFesIdentityDocument(fesIdentityDocumentGeneral, verificationDocument);
        }
        if (fesIdentityDocumentGeneral.getIdentityDocumentType() != null
                && fesIdentityDocumentGeneral.getIdentityDocumentType().equals(getBd(DICTIONARY_337, "2"))) {
            getFesRightOfResidenceDocument(fesIdentityDocumentGeneral, verificationDocument);
        }
    }

    public FesParticipant addParticipant(FesCategory fesCategory, BaseDictionary participantType, Boolean isResidentRus, String rejectTypeCode) {
        FesParticipant fesParticipant = new FesParticipant();
        fesParticipant.setCategoryId(fesCategory);
        fesParticipant.setParticipantType(participantType);

        String residenceStatus = determineResidenceStatus(isResidentRus, rejectTypeCode);
        fesParticipant.setParticipantResidentFeature(residenceStatus != null ? getBd(DICTIONARY_323, residenceStatus) : null);

        return fesParticipantRepository.save(fesParticipant);
    }

    private String determineResidenceStatus(Boolean isResidentRus, String rejectTypeCode) {
        if (("2".equals(rejectTypeCode) || "3".equals(rejectTypeCode)) && isResidentRus == null) {
            return null;
        }
        return isResidentRus != null
                ? (isResidentRus ? RESIDENT : NON_RESIDENT)
                : UNKNOWN_RESIDENCE_STATUS;
    }

    public void addParticipantLegal(FesParticipant fesParticipant, FesEio fesEio, Client client) {
        var clientLegal = client.getClientLegal();
        FesParticipantLegal fesParticipantLegal = new FesParticipantLegal();
        fesParticipantLegal.setParticipantId(fesParticipant);
        fesParticipantLegal.setEioId(fesEio);
        if (clientLegal != null) {
            fesParticipantLegal.setParticipantLegalName(clientLegal.getLegalname());
            fesParticipantLegal.setKpp(clientLegal.getKpp());
            fesParticipantLegal.setRegistrationDate(clientLegal.getDateofregistrationbeforeogrn());
        }
        fesParticipantLegal.setInn(client.getInn());
        fesParticipantLegal.setOgrn(client.getOgrn());
        fesParticipantLegalRepository.save(fesParticipantLegal);
    }

    public FesEio addEio(FesParticipant fesParticipant, BaseDictionary eioType) {
        FesEio fesEio = new FesEio();
        fesEio.setParticipantId(fesParticipant);
        fesEio.setEioType(eioType);
        fesEio = fesEioRepository.save(fesEio);
        return fesEio;
    }

    public FesBeneficiary addBeneficiary(FesParticipant fesParticipant, BaseDictionary beneficiaryType) {
        FesBeneficiary fesBeneficiary = new FesBeneficiary();
        fesBeneficiary.setParticipantId(fesParticipant);
        fesBeneficiary.setBeneficiaryType(beneficiaryType);
        fesBeneficiary = fesBeneficiaryRepository.save(fesBeneficiary);
        return fesBeneficiary;
    }

    public void addAddress(FesCategory fesCategory, FesParticipant fesParticipant, FesBeneficiary fesBeneficiary, FesEio fesEio, BaseDictionary addressType, Address clientAddress) {
        FesAddress fesAddress = new FesAddress();
        fesAddress.setCategoryId(fesCategory);
        fesAddress.setParticipantId(fesParticipant);
        fesAddress.setBeneficiaryId(fesBeneficiary);
        fesAddress.setEioId(fesEio);
        fesAddress.setAddressType(addressType);
        if (clientAddress != null) {
            fesAddress.setCountryCode(clientAddress.getCountryCode());
            fesAddress.setDistrict(clientAddress.getAreaName());
            fesAddress.setTownship(clientAddress.getCityName());
            fesAddress.setStreet(clientAddress.getStreetName());
            fesAddress.setHouse(clientAddress.getHouseN1());
            fesAddress.setCorpus(clientAddress.getHouseN2());
            fesAddress.setRoom(clientAddress.getRoomN());
            if (clientAddress.getCountryCode() == null &&
                    clientAddress.getAreaName() == null &&
                    clientAddress.getCityName() == null &&
                    clientAddress.getStreetName() == null &&
                    clientAddress.getHouseN1() == null &&
                    clientAddress.getHouseN2() == null &&
                    clientAddress.getRoomN() == null) {
                fesAddress.setAddressText(clientAddress.getAddressLine());
            }
        }
        fesAddressRepository.save(fesAddress);
    }

    public void findEioAddressAndAdd(FesEio fesEio, Client client, String addressTypeCode, String fesAddressTypeCode) {
        Address clientAddress = findMainLegalAddress(client.getAddressList(), addressTypeCode);
        var addressOfRegType = getBd(DICTIONARY_331, fesAddressTypeCode);
        addAddress(null, null, null, fesEio, addressOfRegType, clientAddress);
    }

    public void findForeignAddressAndAdd(FesCategory fesCategory, Client client, String addressTypeCode, String fesAddressTypeCode) {
        Address clientAddress = findMainLegalAddress(client.getAddressList(), addressTypeCode);
        var addressOfRegType = getBd(DICTIONARY_331, fesAddressTypeCode);
        addAddress(fesCategory, null, null, null, addressOfRegType, clientAddress);
    }

    public Address findMainLegalAddress(List<Address> clientAddresses, String addressType) {
        for (Address addr : clientAddresses) {
            if (addr.getType() != null) {
                if (Objects.equals(addr.getType().getCode(), addressType)) {
                    return addr;
                }
            }
        }
        return null;
    }

    public String checkClientType(Client client) {
        var clientTypeCode = client.getClientType() != null ?
                client.getClientType().getCode() : WRONG_CLIENT_TYPE;

        String clientType;
        switch (clientTypeCode) {
            case "4":
            case "5":
            case "6":
                clientType = INDIVIDUAL;
                break;
            case "1":
            case "3":
            case "7":
            case "10":
            case "11":
            case "12":
                clientType = LEGAL;
                break;
            case "2":
                clientType = FOREIGN;
                break;
            default:
                clientType = null;
        }
        return clientType;
    }

    public Client findRelation(Client client, BaseDictionary relationType) {
        Optional<ClientRelation> clientRelation = client.getClientRelationToList().stream()
                .filter(p -> p.getRelationType().equals(relationType))
                .findFirst();
        return clientRelation.map(ClientRelation::getFromClientId).orElse(null);
    }

    public BaseDictionary getBd(Integer typeCode, String code) {
        if (code == null) {
            return null;
        }
        return baseDictionaryRepository.findByType_CodeAndCode(typeCode, code).stream().findFirst().orElse(null);
    }

    private void getFesRightOfResidenceDocument(FesIdentityDocumentGeneral fesIdentityDocumentGeneral, VerificationDocument verificationDocument) {
        FesRightOfResidenceDocument fesRightOfResidenceDocument = new FesRightOfResidenceDocument();
        fesRightOfResidenceDocument.setIdentityDocumentGeneralId(fesIdentityDocumentGeneral);
        if (verificationDocument != null) {
            fesRightOfResidenceDocument.setStartStayDate(verificationDocument.getStartDateDoc());
            fesRightOfResidenceDocument.setEndStayDate(verificationDocument.getEndDateDoc());
        }
        fesRightOfResidenceDocumentRepository.save(fesRightOfResidenceDocument);
    }

    @NotNull
    private FesParticipantIndividual getFesParticipantIndividual(FesParticipant fesParticipant, FesBeneficiary fesBeneficiary, FesEio fesEio, BaseDictionary physicalIdentificationFeature, Client client, ClientIndividual clientIndividual, Address clientAddress) {
        FesParticipantIndividual fesParticipantIndividual = new FesParticipantIndividual();
        fesParticipantIndividual.setParticipantId(fesParticipant);
        fesParticipantIndividual.setBeneficiaryId(fesBeneficiary);
        fesParticipantIndividual.setEioId(fesEio);
        fesParticipantIndividual.setPhysicalIdentificationFeature(physicalIdentificationFeature);
        if (extractPartOfName(client.getFullName(), 0) != null) {
            fesParticipantIndividual.setLastName(extractPartOfName(client.getFullName(), 0));
            fesParticipantIndividual.setFirstName(extractPartOfName(client.getFullName(), 1));
            fesParticipantIndividual.setMiddleName(extractPartOfName(client.getFullName(), 2));
        } else {
            fesParticipantIndividual.setFullName(client.getFullName());
        }
        fesParticipantIndividual.setInn(client.getInn());
        if (clientIndividual != null) {
            fesParticipantIndividual.setSnils(clientIndividual.getSnils());
            fesParticipantIndividual.setHealthCardNum((clientIndividual.getOmsNumber()));
            fesParticipantIndividual.setBirthDate(clientIndividual.getBirthdate());
        }
        fesParticipantIndividual.setPhoneNumber(client.getPhoneNumber());
        fesParticipantIndividual.setOgrnip(client.getOgrn());
        if (clientAddress != null) {
            fesParticipantIndividual.setCitizenshipCountryCode(clientAddress.getCountryCode());
        }
        fesParticipantIndividual = fesParticipantIndividualRepository.save(fesParticipantIndividual);
        return fesParticipantIndividual;
    }

    @NotNull
    private FesIdentityDocumentGeneral getFesIdentityDocumentGeneral(FesParticipantIndividual fesParticipantIndividual, VerificationDocument verificationDocument) {
        FesIdentityDocumentGeneral fesIdentityDocumentGeneral = new FesIdentityDocumentGeneral();
        fesIdentityDocumentGeneral.setParticipantIndividualId(fesParticipantIndividual);
        if (verificationDocument != null) {
            fesIdentityDocumentGeneral.setDocumentTypeCode(verificationDocument.getType());
            fesIdentityDocumentGeneral.setDocumentNum(extractDocParts(verificationDocument.getDocNumber(), 1));
            if (!verificationDocument.getDocNumber().equals(extractDocParts(verificationDocument.getDocNumber(), 0))) {
                fesIdentityDocumentGeneral.setDocumentSeries(extractDocParts(verificationDocument.getDocNumber(), 0));
            }
            fesIdentityDocumentGeneral.setIdentityDocumentType(getIdentityDocType(verificationDocument.getType()));
        }
        return fesIdentityDocumentGeneralRepository.save(fesIdentityDocumentGeneral);
    }

    private void getFesIdentityDocument(FesIdentityDocumentGeneral fesIdentityDocumentGeneral, VerificationDocument verificationDocument) {
        FesIdentityDocument fesIdentityDocument = new FesIdentityDocument();
        fesIdentityDocument.setIdentityDocumentGeneralId(fesIdentityDocumentGeneral);
        if (verificationDocument != null) {
            fesIdentityDocument.setIssueDate(verificationDocument.getIssueDate().atStartOfDay());
            fesIdentityDocument.setDepartmentCode(verificationDocument.getIssueByDepartmentCode());
            fesIdentityDocument.setIssuingAgency(verificationDocument.getIssueByOrganization());
        }
        fesIdentityDocumentRepository.save(fesIdentityDocument);
    }

    public String extractPartOfName(String fullName, int partNumber) {
        String[] nameParts = fullName.split(" ");
        if (nameParts.length != 3) {
            return null;
        }
        switch (partNumber) {
            case 0:
                return nameParts[0]; // Фамилия
            case 1:
                return nameParts[1]; // Имя
            case 2:
                return nameParts[2]; // Отчество
            default:
                return null;
        }
    }

    private String extractDocParts(String docNumber, int partNumber) {
        String[] docParts = docNumber.split(" ");
        if (docParts.length != 2) {
            return docNumber;
        }
        switch (partNumber) {
            case 0:
                return docParts[0]; // Серия
            case 1:
                return docParts[1]; // Номер
            default:
                return null;
        }
    }

    private Address findMainAddress(List<Address> clientAddresses) {
        for (Address addr : clientAddresses) {
            if (addr.getType() != null) {
                if (Objects.equals(addr.getType().getCode(), ADDRESS_REGISTRATION)) {
                    return addr;
                }
            }
        }
        for (Address addr : clientAddresses) {
            if (addr.getType() != null) {
                if (Objects.equals(addr.getType().getCode(), ADDRESS_LOCATION)) {
                    return addr;
                }
            }
        }
        return clientAddresses.isEmpty() ? null : clientAddresses.get(0);
    }

    private VerificationDocument findMainDocument(List<VerificationDocument> verificationDocuments) {
        for (VerificationDocument doc : verificationDocuments) {
            if (doc.getType() != null) {
                if (Objects.equals(doc.getType().getCode(), DOC_PASSPORT_RF)) {
                    return doc;
                }
            }
        }
        for (VerificationDocument doc : verificationDocuments) {
            if (doc.getType() != null) {
                if (Objects.equals(doc.getType().getCode(), DOC_PASSPORT_ZAGRAN)) {
                    return doc;
                }
            }
        }
        return verificationDocuments.isEmpty() ? null : verificationDocuments.get(0);
    }

    private BaseDictionary getIdentityDocType(BaseDictionary type) {
        if (type != null) {
            String typeCode = type.getCode();
            for (String code : DOC_RF) {
                if (typeCode.equals(code)) {
                    return getBd(DICTIONARY_337, "1");
                }
            }
            return getBd(DICTIONARY_337, "2");
        }
        return null;
    }

    @Transactional
    public FesCategory getFesCategory(BaseDictionary caseType, BaseDictionary caseCategory, BaseDictionary caseObjectType, BaseDictionary caseStatus, SysUser responsibleUser, BaseDictionary caseCondition, BaseDictionary rejectType, FesCaseSaveDto fesCaseSaveDto) {
        Case aCase = createCase(caseType, caseCategory, caseObjectType, caseStatus, responsibleUser, caseCondition);
        FesCategory fesCategory = createFesCategory(aCase, caseCategory);
        FesRefusalCaseDetails fesRefusalCaseDetails = createFesRefusalCaseDetails(fesCategory, rejectType);
        fesCategory.setFesRefusalCaseDetails(new ArrayList<>(List.of(fesRefusalCaseDetails)));
        FesCasesStatus fesCasesStatus = createFesCasesStatus(fesCategory, caseStatus, caseCondition);
        fesCategory.setFesCasesStatuses(new ArrayList<>(List.of(fesCasesStatus)));
        FesMainPageNew fesMainPageNew = createFesMainPageNew(fesCasesStatus, aCase);
        fesCasesStatus.setFesMainPageNews(new ArrayList<>(List.of(fesMainPageNew)));
        FesMainPageOtherSections fesMainPageOtherSections = createFesMainPageOtherSections(responsibleUser, fesCasesStatus, fesCaseSaveDto);
        fesCasesStatus.setFesMainPageOtherSections(new ArrayList<>(List.of(fesMainPageOtherSections)));
        createFesMainPageUserDecision(responsibleUser, fesCategory, caseStatus, caseCondition, fesCaseSaveDto);
        fesCategory = fesCategoryRepository.save(fesCategory);
        return fesCategory;
    }

    @NotNull
    private FesCategory createFesCategory(Case aCase, BaseDictionary caseCategory) {
        FesCategory fesCategory = new FesCategory();
        fesCategory.setCaseId(aCase);
        fesCategory.setCategory(caseCategory);
        fesCategory = fesCategoryRepository.save(fesCategory);
        return fesCategory;
    }

    private FesRefusalCaseDetails createFesRefusalCaseDetails(FesCategory fesCategory, BaseDictionary rejectType) {
        FesRefusalCaseDetails fesRefusalCaseDetails = new FesRefusalCaseDetails();
        fesRefusalCaseDetails.setCategoryId(fesCategory);
        fesRefusalCaseDetails.setRefusalDate(LocalDateTime.now());
        fesRefusalCaseDetails.setRejectType(rejectType);
        return fesRefusalCaseDetailsRepository.save(fesRefusalCaseDetails);
    }

    @NotNull
    private FesCasesStatus createFesCasesStatus(FesCategory fesCategory, BaseDictionary caseStatus, BaseDictionary caseCondition) {
        FesCasesStatus fesCasesStatus = new FesCasesStatus();
        fesCasesStatus.setCategoryId(fesCategory);
        fesCasesStatus.setCaseStatus(caseStatus);
        fesCasesStatus.setCaseCondition(caseCondition);
        fesCasesStatus = fesCasesStatusRepository.save(fesCasesStatus);
        return fesCasesStatus;
    }

    private FesMainPageNew createFesMainPageNew(FesCasesStatus fesCasesStatus, Case aCase) {
        FesMainPageNew fesMainPageNew = new FesMainPageNew();
        fesMainPageNew.setCasesStatusId(fesCasesStatus);
        fesMainPageNew.setCaseDate(aCase.getCreationdate());
        return fesMainPageNewRepository.save(fesMainPageNew);
    }

    private FesMainPageOtherSections createFesMainPageOtherSections(SysUser responsibleUser, FesCasesStatus fesCasesStatus, FesCaseSaveDto fesCaseSaveDto) {
        FesMainPageOtherSections fesMainPageOtherSections = new FesMainPageOtherSections();
        fesMainPageOtherSections.setResponsibleUser(responsibleUser);
        fesMainPageOtherSections.setCasesStatusId(fesCasesStatus);
        fesMainPageOtherSections.setComment(fesCaseSaveDto.getComment());
        return fesMainPageOtherSectionsRepository.save(fesMainPageOtherSections);
    }

    private void createFesMainPageUserDecision(SysUser responsibleUser, FesCategory fesCategory, BaseDictionary caseStatus, BaseDictionary caseCondition, FesCaseSaveDto fesCaseSaveDto) {
        FesMainPageUserDecision fesMainPageUserDecision = new FesMainPageUserDecision();
        fesMainPageUserDecision.setResponsibleUser(responsibleUser);
        fesMainPageUserDecision.setCategoryId(fesCategory);
        fesMainPageUserDecision.setChangingDate(LocalDateTime.now());
        fesMainPageUserDecision.setCaseStatus(caseStatus);
        fesMainPageUserDecision.setCaseCondition(caseCondition);
        fesMainPageUserDecision.setComment(fesCaseSaveDto.getComment());
        fesMainPageUserDecisionRepository.save(fesMainPageUserDecision);
    }

    @NotNull
    private Case createCase(BaseDictionary caseType, BaseDictionary caseCategory, BaseDictionary caseObjectType, BaseDictionary caseStatus, SysUser responsibleUser, BaseDictionary caseCondition) {
        Case aCase = new Case();
        aCase.setName(FesConstants.NAME);
        aCase.setSubname(getSubname(caseCategory));
        aCase.setCaseType(caseType);
        aCase.setCaseObjectType(caseObjectType);
        aCase.setStatus(caseStatus);
        aCase.setCreationdate(LocalDateTime.now());
        aCase.setResponsibleUser(responsibleUser);
        aCase.setCaseStatus(caseCondition);
        aCase.setCaseObjectSubType(caseCategory);
        aCase = caseRepository.save(aCase);
        return aCase;
    }

    private String getSubname(BaseDictionary caseCategory) {
        return caseCategory.getCode().equals("2") ? SUBNAME_FREEZING : SUBNAME_CONTRACT_REJECTION;
    }

    public <T, D> void deleteMissingItems(List<D> dtoList, List<T> existingList, JpaRepository<T, Long> repository, Function<D, Long> idExtractorDto, Function<T, Long> idExtractor) {
        Set<Long> dtoIds = dtoList.stream()
                .map(idExtractorDto)
                .collect(Collectors.toSet());

        Iterator<T> iterator = existingList.iterator();
        while (iterator.hasNext()) {
            T existingItem = iterator.next();
            if (!dtoIds.contains(idExtractor.apply(existingItem))) {
                repository.delete(existingItem);
                iterator.remove();
            }
        }
    }

    public  <D, T> List<T> createAndSaveAllItems(List<D> dtoList, Function<D, T> createFunction, JpaRepository<T, Long> repository, Object parentEntity) {
        List<T> resultList = new ArrayList<>();
        for (D dto : dtoList) {
            T entity = createFunction.apply(dto);
            resultList.add(entity);
        }
        repository.saveAll(resultList);
        return resultList;
    }

    public void addFesCashMoneyTransfers(FesParticipant fesParticipant, Payment payment) {
        FesCashMoneyTransfers fesCashMoneyTransfers = new FesCashMoneyTransfers();
        fesCashMoneyTransfers.setParticipantId(fesParticipant);
        fesCashMoneyTransfers.setBankBic(payment.getBankPayerId().getBic());
        fesCashMoneyTransfers.setBankName(payment.getBankPayerId() != null ?
                payment.getBankPayerId().getName():
                null);
    }

    public BaseDictionary getCaseObjectType(String fesCategoryCode, String rejectTypeCode) {
        return Objects.equals(fesCategoryCode, "2") ||
                Objects.equals(rejectTypeCode, "2") ||
                Objects.equals(rejectTypeCode, "3") ?
                getBd(DICTIONARY_14, "1") :
                getBd(DICTIONARY_14, "2");
    }

    public BaseDictionary getCaseStatus() {
        return getBd(DICTIONARY_38, "2");
    }

    public BaseDictionary getCaseType(String fesCategoryCode) {
        if (Objects.equals(fesCategoryCode, "1")) {
            return getBd(DICTIONARY_18, "9");
        }
        if (Objects.equals(fesCategoryCode, "2")) {
            return getBd(DICTIONARY_18, "10");
        }
        return getBd(DICTIONARY_18, "12");
    }

    public BaseDictionary getCaseCategory(String fesCategoryCode) {
       return getBd(DICTIONARY_309, fesCategoryCode);
    }

    public BaseDictionary getCaseCondition() {
        return getBd(DICTIONARY_305, "2");
    }
}
