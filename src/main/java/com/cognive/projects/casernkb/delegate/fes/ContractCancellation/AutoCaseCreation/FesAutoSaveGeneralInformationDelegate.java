package com.cognive.projects.casernkb.delegate.fes.ContractCancellation.AutoCaseCreation;

import com.cognive.projects.casernkb.service.FesService;
import com.prime.db.rnkb.model.BaseDictionary;
import com.prime.db.rnkb.model.Case;
import com.prime.db.rnkb.model.CaseRules;
import com.prime.db.rnkb.model.Payment;
import com.prime.db.rnkb.model.fes.FesBankInformation;
import com.prime.db.rnkb.model.fes.FesCategory;
import com.prime.db.rnkb.model.fes.FesDataPrefill;
import com.prime.db.rnkb.model.fes.FesGeneralInformation;
import com.prime.db.rnkb.model.fes.FesOperationInformation;
import com.prime.db.rnkb.model.fes.FesRefusalCaseDetails;
import com.prime.db.rnkb.model.fes.FesRefusalReason;
import com.prime.db.rnkb.model.fes.FesServiceInformation;
import com.prime.db.rnkb.model.fes.FesSuspiciousActivityIdentifier;
import com.prime.db.rnkb.model.fes.FesUnusualOperationFeature;
import com.prime.db.rnkb.repository.fes.FesBankInformationRepository;
import com.prime.db.rnkb.repository.fes.FesDataPrefillRepository;
import com.prime.db.rnkb.repository.fes.FesGeneralInformationRepository;
import com.prime.db.rnkb.repository.fes.FesOperationInformationRepository;
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

import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_307;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_310;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_312;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_318;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_320;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_86;

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

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        var fesCategory = (FesCategory) execution.getVariable("fesCategory");
        var rejectTypeCode = (String) execution.getVariable("rejectType");
        var baseRejectCode = (String) execution.getVariable("baseRejectCode");
        var aCase = (Case) execution.getVariable("case");
        var isOperationRejection = (boolean) execution.getVariable("isOperationRejection");

        var recordType = fesService.getBd(DICTIONARY_86, "1");
        var groundOfRefusal = isOperationRejection ? fesService.getBd(DICTIONARY_318, "07"):
                rejectTypeCode.equals("2") ?
                fesService.getBd(DICTIONARY_318, "03"):
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
        fesServiceInformationRepository.save(fesServiceInformation);

        FesGeneralInformation fesGeneralInformation = new FesGeneralInformation();
        fesGeneralInformation.setCategoryId(fesCategory);
        fesGeneralInformation.setRecordType(recordType);
        if (isOperationRejection) {
            fesGeneralInformation.setComment((String) execution.getVariable("conclusion"));
        }
        fesGeneralInformationRepository.save(fesGeneralInformation);

        FesRefusalCaseDetails fesRefusalCaseDetails = new FesRefusalCaseDetails();
        fesRefusalCaseDetails.setCategoryId(fesCategory);
        fesRefusalCaseDetails.setRefusalDate(LocalDateTime.now());
        fesRefusalCaseDetails.setGroundOfRefusal(isOperationRejection ?
                fesService.getBd(DICTIONARY_318, baseRejectCode):
                groundOfRefusal);
        fesRefusalCaseDetails.setRejectType(rejectType);
        fesRefusalCaseDetailsRepository.save(fesRefusalCaseDetails);

        if (isOperationRejection) {
            Payment payment = (Payment) execution.getVariable("payment");

            FesOperationInformation fesOperationInformation = new FesOperationInformation();
            fesOperationInformation.setCategoryId(fesCategory);
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

        execution.setVariable("caseId", aCase.getId());
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

    private <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }
}
