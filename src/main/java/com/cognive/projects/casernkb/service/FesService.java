package com.cognive.projects.casernkb.service;

import com.cognive.projects.casernkb.model.fes.FesCaseSaveDto;
import com.prime.db.rnkb.model.Address;
import com.prime.db.rnkb.model.BaseDictionary;
import com.prime.db.rnkb.model.Case;
import com.prime.db.rnkb.model.Client;
import com.prime.db.rnkb.model.ClientIndividual;
import com.prime.db.rnkb.model.ClientRelation;
import com.prime.db.rnkb.model.SysUser;
import com.prime.db.rnkb.model.VerificationDocument;
import com.prime.db.rnkb.model.fes.FesAddress;
import com.prime.db.rnkb.model.fes.FesBeneficiary;
import com.prime.db.rnkb.model.fes.FesCasesStatus;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
    private static final String NAME = "ФЭС";
    private static final String SUBNAME = "Отказ от заключения договора (расторжение)";

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

    public void addParticipantIndividual(FesCategory fesCategory, FesParticipant fesParticipant, Client client) {

        ClientIndividual clientIndividual = client.getClientIndividual();
        List<Address> clientAddresses = addressRepository.findAllByClient(client);
        Address clientAddress = findMainAddress(clientAddresses);
        List<VerificationDocument> verificationDocuments = verificationDocumentRepository.findAllByClient(client);
        VerificationDocument verificationDocument = findMainDocument(verificationDocuments);

        var physicalIdentificationFeature =
                baseDictionaryRepository.getBaseDictionary("1", 325);
        var addressType =
                baseDictionaryRepository.getBaseDictionary("8", 331);

        FesParticipantIndividual fesParticipantIndividual = getFesParticipantIndividual(fesParticipant, null, null, physicalIdentificationFeature, client, clientIndividual, clientAddress);
        FesIdentityDocumentGeneral fesIdentityDocumentGeneral = getFesIdentityDocumentGeneral(fesParticipantIndividual, verificationDocument);
        if (fesIdentityDocumentGeneral.getIdentityDocumentType() != null
                && fesIdentityDocumentGeneral.getIdentityDocumentType().equals(getBd("1", 337))) {
            getFesIdentityDocument(fesIdentityDocumentGeneral, verificationDocument);
        }
        if (fesIdentityDocumentGeneral.getIdentityDocumentType() != null
                && fesIdentityDocumentGeneral.getIdentityDocumentType().equals(getBd("2", 337))) {
            getFesRightOfResidenceDocument(fesIdentityDocumentGeneral, verificationDocument);
        }
        addAddress(fesCategory, null, null, addressType, clientAddress);

    }

    public void addParticipantIndividualBeneficiary(FesBeneficiary fesBeneficiary, Client client) {

        ClientIndividual clientIndividual = client.getClientIndividual();
        List<Address> clientAddresses = addressRepository.findAllByClient(client);
        Address clientAddress = findMainAddress(clientAddresses);
        List<VerificationDocument> verificationDocuments = verificationDocumentRepository.findAllByClient(client);
        VerificationDocument verificationDocument = findMainDocument(verificationDocuments);

        var physicalIdentificationFeature =
                baseDictionaryRepository.getBaseDictionary("1", 325);
        var addressType =
                baseDictionaryRepository.getBaseDictionary("8", 331);

        FesParticipantIndividual fesParticipantIndividual = getFesParticipantIndividual(null, fesBeneficiary, null, physicalIdentificationFeature, client, clientIndividual, clientAddress);
        FesIdentityDocumentGeneral fesIdentityDocumentGeneral = getFesIdentityDocumentGeneral(fesParticipantIndividual, verificationDocument);
        if (fesIdentityDocumentGeneral.getIdentityDocumentType() != null
                && fesIdentityDocumentGeneral.getIdentityDocumentType().equals(getBd("1", 337))) {
            getFesIdentityDocument(fesIdentityDocumentGeneral, verificationDocument);
        }
        if (fesIdentityDocumentGeneral.getIdentityDocumentType() != null
                && fesIdentityDocumentGeneral.getIdentityDocumentType().equals(getBd("2", 337))) {
            getFesRightOfResidenceDocument(fesIdentityDocumentGeneral, verificationDocument);
        }
        addAddress(null, fesBeneficiary, null, addressType, clientAddress);

    }

    public void addParticipantIndividualEio(FesEio fesEio, Client client) {

        ClientIndividual clientIndividual = client.getClientIndividual();
        List<Address> clientAddresses = addressRepository.findAllByClient(client);
        Address clientAddress = findMainAddress(clientAddresses);
        List<VerificationDocument> verificationDocuments = verificationDocumentRepository.findAllByClient(client);
        VerificationDocument verificationDocument = findMainDocument(verificationDocuments);

        var physicalIdentificationFeature =
                baseDictionaryRepository.getBaseDictionary("1", 325);
        var addressType =
                baseDictionaryRepository.getBaseDictionary("8", 331);

        FesParticipantIndividual fesParticipantIndividual = getFesParticipantIndividual(null, null, fesEio, physicalIdentificationFeature, client, clientIndividual, clientAddress);
        FesIdentityDocumentGeneral fesIdentityDocumentGeneral = getFesIdentityDocumentGeneral(fesParticipantIndividual, verificationDocument);
        if (fesIdentityDocumentGeneral.getIdentityDocumentType() != null
                && fesIdentityDocumentGeneral.getIdentityDocumentType().equals(getBd("1", 337))) {
            getFesIdentityDocument(fesIdentityDocumentGeneral, verificationDocument);
        }
        if (fesIdentityDocumentGeneral.getIdentityDocumentType() != null
                && fesIdentityDocumentGeneral.getIdentityDocumentType().equals(getBd("2", 337))) {
            getFesRightOfResidenceDocument(fesIdentityDocumentGeneral, verificationDocument);
        }
        addAddress(null, null, fesEio, addressType, clientAddress);

    }

    public FesParticipant addParticipant(FesCategory fesCategory, BaseDictionary participantType) {
        FesParticipant fesParticipant = new FesParticipant();
        fesParticipant.setCategoryId(fesCategory);
        fesParticipant.setParticipantType(participantType);
        fesParticipant = fesParticipantRepository.save(fesParticipant);
        return fesParticipant;
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

    public void addAddress(FesCategory fesCategory, FesBeneficiary fesBeneficiary, FesEio fesEio, BaseDictionary addressType, Address clientAddress) {
        FesAddress fesAddress = new FesAddress();
        fesAddress.setCategoryId(fesCategory);
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
            fesAddress.setAddressText(clientAddress.getAddressLine());
        }
        fesAddressRepository.save(fesAddress);
    }

    public void findEioAddressAndAdd(FesEio fesEio, Client client, String addressTypeCode, String fesAddressTypeCode) {
        Address clientAddress = findMainLegalAddress(client.getAddressList(), addressTypeCode);

        var addressOfRegType = getBd(fesAddressTypeCode, 331);

        addAddress(null, null, fesEio, addressOfRegType, clientAddress);
    }

    public void findForeignAddressAndAdd(FesCategory fesCategory, Client client, String addressTypeCode, String fesAddressTypeCode) {
        Address clientAddress = findMainLegalAddress(client.getAddressList(), addressTypeCode);

        var addressOfRegType = getBd(fesAddressTypeCode, 331);

        addAddress(fesCategory, null, null, addressOfRegType, clientAddress);
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

    public BaseDictionary getBd(String code, Integer type) {
        return baseDictionaryRepository.getBaseDictionary(code, type);
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
        if(extractPartOfName(client.getFullName(), 0) != null) {
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
        fesParticipantIndividual.setOrgnip(client.getOgrn());
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
            fesIdentityDocumentGeneral.setDocumentSeries(extractDocParts(verificationDocument.getDocNumber(), 0));
            fesIdentityDocumentGeneral.setDocumentNum(extractDocParts(verificationDocument.getDocNumber(), 1));
            fesIdentityDocumentGeneral.setIdentityDocumentType(getIdentityDocType(verificationDocument.getType()));
        }
        fesIdentityDocumentGeneral = fesIdentityDocumentGeneralRepository.save(fesIdentityDocumentGeneral);
        return fesIdentityDocumentGeneral;
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
                    return getBd("1", 337);
                }
            }
            return getBd("2", 337);
        }
        return null;
    }

    @Transactional
    public FesCategory getFesCategory(BaseDictionary caseType, BaseDictionary caseCategory, BaseDictionary caseObjectType, BaseDictionary caseStatus, SysUser responsibleUser, BaseDictionary caseCondition, Optional<BaseDictionary> rejectType, FesCaseSaveDto fesCaseSaveDto) {
        Case aCase = createCase(caseType, caseCategory, caseObjectType, caseStatus, responsibleUser, caseCondition);
        FesCategory fesCategory = createFesCategory(aCase, caseCategory);
        FesRefusalCaseDetails fesRefusalCaseDetails = createFesRefusalCaseDetails(fesCategory, rejectType);
        fesCategory.setFesRefusalCaseDetails(new ArrayList<>(List.of(fesRefusalCaseDetails)));
        FesCasesStatus fesCasesStatus = createFesCasesStatus(fesCategory, caseStatus, caseCondition);
        createFesMainPageNew(fesCasesStatus, aCase);
        createFesMainPageOtherSections(responsibleUser, fesCasesStatus, fesCaseSaveDto);
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

    private FesRefusalCaseDetails createFesRefusalCaseDetails(FesCategory fesCategory, Optional<BaseDictionary> rejectType) {
        FesRefusalCaseDetails fesRefusalCaseDetails = new FesRefusalCaseDetails();
        fesRefusalCaseDetails.setCategoryId(fesCategory);
        fesRefusalCaseDetails.setRejectType(rejectType.orElseThrow());
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

    private void createFesMainPageNew(FesCasesStatus fesCasesStatus, Case aCase) {
        FesMainPageNew fesMainPageNew = new FesMainPageNew();
        fesMainPageNew.setCasesStatusId(fesCasesStatus);
        fesMainPageNew.setCaseDate(aCase.getCreationdate());
        fesMainPageNewRepository.save(fesMainPageNew);
    }

    private void createFesMainPageOtherSections(SysUser responsibleUser, FesCasesStatus fesCasesStatus, FesCaseSaveDto fesCaseSaveDto) {
        FesMainPageOtherSections fesMainPageOtherSections = new FesMainPageOtherSections();
        fesMainPageOtherSections.setResponsibleUser(responsibleUser);
        fesMainPageOtherSections.setCasesStatusId(fesCasesStatus);
        fesMainPageOtherSections.setComment(fesCaseSaveDto.getComment());
        fesMainPageOtherSectionsRepository.save(fesMainPageOtherSections);
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
        aCase.setName(NAME);
        aCase.setSubname(SUBNAME);
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

}