package com.cognive.projects.casernkb.delegate.fes.ContractCancellation.AutoCaseCreation;

import com.cognive.projects.casernkb.service.FesService;
import com.prime.db.rnkb.model.BaseDictionary;
import com.prime.db.rnkb.model.BaseDocs;
import com.prime.db.rnkb.model.Case;
import com.prime.db.rnkb.model.CaseRules;
import com.prime.db.rnkb.model.Payment;
import com.prime.db.rnkb.model.PrecMaterials;
import com.prime.db.rnkb.model.fes.FesAdditionalOperation;
import com.prime.db.rnkb.model.fes.FesBankInformation;
import com.prime.db.rnkb.model.fes.FesCategory;
import com.prime.db.rnkb.model.fes.FesDataPrefill;
import com.prime.db.rnkb.model.fes.FesGeneralInformation;
import com.prime.db.rnkb.model.fes.FesMoneyTransfers;
import com.prime.db.rnkb.model.fes.FesOperationInformation;
import com.prime.db.rnkb.model.fes.FesOperationsDetails;
import com.prime.db.rnkb.model.fes.FesOperationsReason;
import com.prime.db.rnkb.model.fes.FesPreciousMetalData;
import com.prime.db.rnkb.model.fes.FesRefusalCaseDetails;
import com.prime.db.rnkb.model.fes.FesRefusalReason;
import com.prime.db.rnkb.model.fes.FesServiceInformation;
import com.prime.db.rnkb.model.fes.FesSuspiciousActivityIdentifier;
import com.prime.db.rnkb.model.fes.FesUnusualOperationFeature;
import com.prime.db.rnkb.repository.PrecMaterialsRepository;
import com.prime.db.rnkb.repository.SysUserRepository;
import com.prime.db.rnkb.repository.fes.FesAdditionalOperationRepository;
import com.prime.db.rnkb.repository.fes.FesBankInformationRepository;
import com.prime.db.rnkb.repository.fes.FesDataPrefillRepository;
import com.prime.db.rnkb.repository.fes.FesGeneralInformationRepository;
import com.prime.db.rnkb.repository.fes.FesOperationInformationRepository;
import com.prime.db.rnkb.repository.fes.FesOperationsDetailsRepository;
import com.prime.db.rnkb.repository.fes.FesOperationsReasonRepository;
import com.prime.db.rnkb.repository.fes.FesPreciousMetalDataRepository;
import com.prime.db.rnkb.repository.fes.FesRefusalCaseDetailsRepository;
import com.prime.db.rnkb.repository.fes.FesRefusalReasonRepository;
import com.prime.db.rnkb.repository.fes.FesServiceInformationRepository;
import com.prime.db.rnkb.repository.fes.FesSuspiciousActivityIdentifierRepository;
import com.prime.db.rnkb.repository.fes.FesUnusualOperationFeatureRepository;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.cognive.projects.casernkb.constant.FesConstants.DEFAULT_BRANCHNUM;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_307;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_310;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_312;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_318;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_320;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_322;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_333;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_86;
import static com.cognive.projects.casernkb.constant.FesConstants.PARENT_BANK_BIC;

@Component
@RequiredArgsConstructor
public class FesAutoSaveGeneralInformationDelegate implements JavaDelegate {

    private final FesDataPrefillRepository fesDataPrefillRepository;
    private final FesBankInformationRepository fesBankInformationRepository;
    private final FesServiceInformationRepository fesServiceInformationRepository;
    private final FesGeneralInformationRepository fesGeneralInformationRepository;
    private final FesRefusalCaseDetailsRepository fesRefusalCaseDetailsRepository;
    private final FesOperationInformationRepository fesOperationInformationRepository;
    private final FesSuspiciousActivityIdentifierRepository fesSuspiciousActivityIdentifierRepository;
    private final FesUnusualOperationFeatureRepository fesUnusualOperationFeatureRepository;
    private final FesService fesService;
    private final FesRefusalReasonRepository fesRefusalReasonRepository;
    private final SysUserRepository sysUserRepository;
    private final FesOperationsDetailsRepository fesOperationsDetailsRepository;
    private final FesOperationsReasonRepository fesOperationsReasonRepository;
    private final FesAdditionalOperationRepository fesAdditionalOperationRepository;
    private final PrecMaterialsRepository precMaterialsRepository;
    private final FesPreciousMetalDataRepository fesPreciousMetalDataRepository;

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        var fesCategory = (FesCategory) execution.getVariable("fesCategory");
        var rejectTypeCode = (String) execution.getVariable("rejectType");
        var baseRejectCode = (String) execution.getVariable("baseRejectCode");
        var aCase = (Case) execution.getVariable("case");
        var isOperationRejection = (boolean) execution.getVariable("isOperationRejection");
        var isOperation = (boolean) execution.getVariable("isOperation");
        var responsibleUser = isOperation ?
                sysUserRepository.findById((Long) execution.getVariable("responsibleUser")).orElse(null) : null;

        var recordType = fesService.getBd(DICTIONARY_86, "1");
        var groundOfRefusal = isOperationRejection ? fesService.getBd(DICTIONARY_318, "07") :
                rejectTypeCode.equals("2") ?
                        fesService.getBd(DICTIONARY_318, "03") :
                        fesService.getBd(DICTIONARY_318, "09");
        var rejectType = fesService.getBd(DICTIONARY_307, rejectTypeCode);

        FesDataPrefill fesDataPrefill = fesDataPrefillRepository.findAll().get(0);

        FesBankInformation fesBankInformation = new FesBankInformation();
        fesBankInformation.setCategoryId(fesCategory);
        fesBankInformation.setReportingAttribute(false);
        fesBankInformation.setBankRegNum(fesDataPrefill.getBankRegNum());
        fesBankInformation.setBankBic(fesDataPrefill.getBankBic());
        fesBankInformation.setBankOcato(fesDataPrefill.getBankOcato());
        fesBankInformationRepository.save(fesBankInformation);

        FesServiceInformation fesServiceInformation = new FesServiceInformation();
        if (rejectTypeCode.equals("2") || rejectTypeCode.equals("3")) {
            fesServiceInformation.setInformationType(fesService.getBd(DICTIONARY_312, "01"));
        }
        fesServiceInformation.setCategoryId(fesCategory);
        fesServiceInformation.setFormatVersion(fesDataPrefill.getFormatVersion());
        fesServiceInformation.setSoftVersion(fesDataPrefill.getSoftVersion());
        fesServiceInformation.setDate(LocalDateTime.now());
        if (isOperation) {
            if (responsibleUser != null) {
                fesServiceInformation.setOfficerMail(responsibleUser.getEmail());
                fesServiceInformation.setOfficerPosition(responsibleUser.getPosition());
                fesServiceInformation.setOfficerLastname(responsibleUser.getLastname());
                fesServiceInformation.setOfficerFirstname(responsibleUser.getFirstname());
                fesServiceInformation.setOfficerMiddlename(responsibleUser.getMiddlename());
                fesServiceInformation.setOfficerPhone(responsibleUser.getMobilephone());
                fesServiceInformation.setOfficerFullName(responsibleUser.getName());
            }
            fesServiceInformation.setInformationType(fesService.getBd(DICTIONARY_312, "1"));
        }
        fesServiceInformationRepository.save(fesServiceInformation);

        FesGeneralInformation fesGeneralInformation = new FesGeneralInformation();
        fesGeneralInformation.setCategoryId(fesCategory);
        fesGeneralInformation.setRecordType(recordType);
        fesGeneralInformation.setNum(fesService.generateNum(fesDataPrefill.getBankRegNum(), DEFAULT_BRANCHNUM, fesCategory));
        if (isOperationRejection) {
            fesGeneralInformation.setComment((String) execution.getVariable("conclusion"));
        }
        fesGeneralInformationRepository.save(fesGeneralInformation);

        if (!isOperation) {
            FesRefusalCaseDetails fesRefusalCaseDetails = new FesRefusalCaseDetails();
            fesRefusalCaseDetails.setCategoryId(fesCategory);
            fesRefusalCaseDetails.setRefusalDate(LocalDateTime.now());
            fesRefusalCaseDetails.setGroundOfRefusal(isOperationRejection ?
                    fesService.getBd(DICTIONARY_318, baseRejectCode) :
                    groundOfRefusal);
            fesRefusalCaseDetails.setRejectType(rejectType);
            fesRefusalCaseDetailsRepository.save(fesRefusalCaseDetails);
        }

        if (isOperationRejection || isOperation) {
            Payment payment = (Payment) execution.getVariable("payment");

            if (isOperation) {
                FesOperationsDetails fesOperationsDetails = new FesOperationsDetails();
                fesOperationsDetails.setCategoryId(fesCategory);
                fesOperationsDetails.setPaySystemName1(payment.getPaymentSystem());
                fesOperationsDetailsRepository.save(fesOperationsDetails);

                BaseDocs baseDocs = payment.getBaseDocsList() != null && !payment.getBaseDocsList().isEmpty() ? payment.getBaseDocsList().get(0) : null;
                FesOperationsReason fesOperationsReason = new FesOperationsReason();
                fesOperationsReason.setCategoryId(fesCategory);
                if (baseDocs != null) {
//                    fesOperationsReason.setDocType(baseDocs.getDocCode());
                    fesOperationsReason.setDocOtherName(baseDocs.getDocTypeOtherName());
                    fesOperationsReason.setDocDate(baseDocs.getDocDate());
                    fesOperationsReason.setDocNum(baseDocs.getDocNum());
                    fesOperationsReason.setSummary(baseDocs.getShortDocContent());
                }
                fesOperationsReasonRepository.save(fesOperationsReason);

                List<String> fesAdditionalOperationList = (List<String>) execution.getVariable("additionalOperationType");
                if (fesAdditionalOperationList != null && !fesAdditionalOperationList.isEmpty()) {
                    List<FesAdditionalOperation> fesAdditionalOperations = fesAdditionalOperationList.stream()
                                            .map(code -> getFesAdditionalOperation(fesService.getBd(DICTIONARY_310, code), fesCategory))
                                            .collect(Collectors.toList());
                    fesAdditionalOperationRepository.saveAll(fesAdditionalOperations);
                }

                List<PrecMaterials> precMaterialsList = precMaterialsRepository.findByPaymentId(payment);
                if (precMaterialsList != null && !precMaterialsList.isEmpty()) {
                    List<FesPreciousMetalData> fesPreciousMetalDataList = precMaterialsList.stream()
                            .map(precMaterials -> getFesPreciousMetalData(precMaterials, fesCategory))
                            .collect(Collectors.toList());
                    fesPreciousMetalDataRepository.saveAll(fesPreciousMetalDataList);
                }

                FesMoneyTransfers fesMoneyTransfers = new FesMoneyTransfers();
                fesMoneyTransfers.setCategoryId(fesCategory);
                fesMoneyTransfers.setTransferType(fesService.getBd(DICTIONARY_322, "1"));
                fesMoneyTransfers.setMoneyTransferOperatorType(getMoneyTransferOperatorType(payment));
                fesMoneyTransfers.setPayerAccountNum(payment.getPayerAccountNumber());
                if (payment.getBankPayerId() != null) {
                    fesMoneyTransfers.setPayerBankAccount(payment.getBankPayerId().getCorrespondentAccount());
                    fesMoneyTransfers.setPayerBankBic(payment.getBankPayerId().getBic());
                    fesMoneyTransfers.setPayerBankName(payment.getBankPayerId().getName());
                }
                fesMoneyTransfers.setPayeeAccountNum(payment.getPayeeAccountNumber());
                if (payment.getBankPayeeId() != null) {
                    fesMoneyTransfers.setPayeeBankAccount(payment.getBankPayeeId().getCorrespondentAccount());
                    fesMoneyTransfers.setPayeeBankBic(payment.getBankPayeeId().getBic());
                    fesMoneyTransfers.setPayeeBankName(payment.getBankPayeeId().getName());
                }
                fesMoneyTransfers.setTransferStatus(getTransferStatus(payment));
            }

            FesOperationInformation fesOperationInformation = new FesOperationInformation();
            fesOperationInformation.setCategoryId(fesCategory);
            if (isOperation) {
                fesOperationInformation.setOperationType(fesService.getBd(DICTIONARY_310, (String) execution.getVariable("operationType")));
            }
            fesOperationInformation.setOperationFeature(payment.getOperationMarker());
            fesOperationInformation.setCurrency(payment.getCurrency());
            fesOperationInformation.setAmount(payment.getAmount());
            fesOperationInformation.setAmountNationalCurrency(payment.getAmountNationalCurrency());
            if (payment.getCurrency() != null &&
                    (!Objects.equals(payment.getCurrency().getCode(), "810")
                            || !Objects.equals(payment.getCurrency().getCode(), "643"))) {
                fesOperationInformation.setCurrencyTransactionAttribute("VO");
            }
            fesOperationInformationRepository.save(fesOperationInformation);

            FesSuspiciousActivityIdentifier fesSuspiciousActivityIdentifier = new FesSuspiciousActivityIdentifier();
            fesSuspiciousActivityIdentifier.setCategoryId(fesCategory);
            fesSuspiciousActivityIdentifier.setSuspiciousActivityIdentifier("TO-DO");
            fesSuspiciousActivityIdentifierRepository.save(fesSuspiciousActivityIdentifier);

            if (isOperationRejection) {
                List<String> codeUnusualOpList = (List<String>) execution.getVariable("codeUnusualOp");
                List<FesUnusualOperationFeature> unusualOperationFeatures =
                        (codeUnusualOpList != null && !codeUnusualOpList.isEmpty()) ?
                                codeUnusualOpList.stream()
                                        .map(code -> getFesUnusualOperationFeature(fesService.getBd(DICTIONARY_310, code), fesCategory))
                                        .collect(Collectors.toList()) :
                                payment.getCaseOperationList().stream()
                                        .map(caseOperation -> caseOperation.getCaseId().getCaseRules())
                                        .flatMap(List::stream)
                                        .filter(distinctByKey(CaseRules::getCode))
                                        .map(caseRule -> getFesUnusualOperationFeature(caseRule.getCode(), fesCategory))
                                        .collect(Collectors.toList());
                fesUnusualOperationFeatureRepository.saveAll(unusualOperationFeatures);

                List<String> causeRejectList = (List<String>) execution.getVariable("causeReject");
                if (causeRejectList != null && !causeRejectList.isEmpty()) {
                    List<FesRefusalReason> fesRefusalReasons = causeRejectList.stream()
                            .map(s -> addFesRefusalReason(s, fesCategory)).collect(Collectors.toList());
                    fesRefusalReasonRepository.saveAll(fesRefusalReasons);
                }
            }

        }

        execution.setVariable("caseId", aCase.getId());
    }

    private BaseDictionary getTransferStatus(Payment payment) {
        if (payment.getPaymentSourceStatus() != null) {
            return Objects.equals(payment.getPaymentSourceStatus().getCode(), "1") ?
                    fesService.getBd(DICTIONARY_333, "1"):
                    fesService.getBd(DICTIONARY_333, "0");
        }
        return null;
    }

    private FesRefusalReason addFesRefusalReason(String causeRejectCode, FesCategory fesCategory) {
        FesRefusalReason fesRefusalReason = new FesRefusalReason();
        fesRefusalReason.setCategoryId(fesCategory);
        fesRefusalReason.setRefusalReason(fesService.getBd(DICTIONARY_320, causeRejectCode));
        return fesRefusalReason;
    }

    private static FesUnusualOperationFeature getFesUnusualOperationFeature(BaseDictionary code, FesCategory fesCategory) {
        FesUnusualOperationFeature fesUnusualOperationFeature = new FesUnusualOperationFeature();
        fesUnusualOperationFeature.setCategoryId(fesCategory);
        fesUnusualOperationFeature.setUnusualOperationType(code);
        return fesUnusualOperationFeature;
    }

    private FesAdditionalOperation getFesAdditionalOperation(BaseDictionary code, FesCategory fesCategory) {
        FesAdditionalOperation fesAdditionalOperation = new FesAdditionalOperation();
        fesAdditionalOperation.setCategoryId(fesCategory);
        fesAdditionalOperation.setAdditionalOperationType(code);
        return fesAdditionalOperation;
    }

    private FesPreciousMetalData getFesPreciousMetalData(PrecMaterials precMaterials, FesCategory fesCategory) {
        FesPreciousMetalData fesPreciousMetalData = new FesPreciousMetalData();
        fesPreciousMetalData.setCategoryId(fesCategory);
        if (precMaterials != null) {
            fesPreciousMetalData.setPreciousMetal(precMaterials.getMaterialCode());
            if (precMaterials.getMaterialCode() != null && Objects.equals(precMaterials.getMaterialCode().getCode(), "C99")) {
                fesPreciousMetalData.setPreciousMetalOthername(precMaterials.getMaterialName());
            }
        }
        return fesPreciousMetalData;
    }

    private BaseDictionary getMoneyTransferOperatorType(Payment payment) {
        String payerBankBic = (payment != null && payment.getBankPayerId() != null) ? payment.getBankPayerId().getBic() : null;
        String payeeBankBic = (payment != null && payment.getBankPayeeId() != null) ? payment.getBankPayeeId().getBic() : null;

        String dictionaryValue;

        if (Objects.equals(payeeBankBic, PARENT_BANK_BIC) && Objects.equals(payerBankBic, PARENT_BANK_BIC)) {
            dictionaryValue = "4";
        } else if (!Objects.equals(payeeBankBic, PARENT_BANK_BIC) && !Objects.equals(payerBankBic, PARENT_BANK_BIC)) {
            dictionaryValue = "3";
        } else if (Objects.equals(payeeBankBic, PARENT_BANK_BIC) && !Objects.equals(payerBankBic, PARENT_BANK_BIC)) {
            dictionaryValue = "2";
        } else {
            dictionaryValue = "1";
        }
        return fesService.getBd(DICTIONARY_322, dictionaryValue);
    }

    private <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }
}
