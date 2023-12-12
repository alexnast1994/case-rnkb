package com.cognive.projects.casernkb.delegate.fes.ContractCancellation.AutoCaseCreation;

import com.cognive.projects.casernkb.service.FesService;
import com.prime.db.rnkb.model.Case;
import com.prime.db.rnkb.model.Payment;
import com.prime.db.rnkb.model.fes.FesCasesStatus;
import com.prime.db.rnkb.model.fes.FesCategory;
import com.prime.db.rnkb.model.fes.FesMainPageNew;
import com.prime.db.rnkb.repository.CaseRepository;
import com.prime.db.rnkb.repository.SysUserRepository;
import com.prime.db.rnkb.repository.fes.FesCasesStatusRepository;
import com.prime.db.rnkb.repository.fes.FesCategoryRepository;
import com.prime.db.rnkb.repository.fes.FesMainPageNewRepository;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_14;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_18;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_305;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_309;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_38;
import static com.cognive.projects.casernkb.constant.FesConstants.NAME;
import static com.cognive.projects.casernkb.constant.FesConstants.SUBNAME_CANCEL_CONTRACT;
import static com.cognive.projects.casernkb.constant.FesConstants.SUBNAME_CONTRACT_REJECTION;
import static com.cognive.projects.casernkb.constant.FesConstants.SUBNAME_OPERATION;
import static com.cognive.projects.casernkb.constant.FesConstants.SUBNAME_OPERATION_REJECTION;

@Component
@RequiredArgsConstructor
public class FesCreateMainTablesDelegate implements JavaDelegate {

    private final CaseRepository caseRepository;
    private final FesCategoryRepository fesCategoryRepository;
    private final FesMainPageNewRepository fesMainPageNewRepository;
    private final FesCasesStatusRepository fesCasesStatusRepository;
    private final FesService fesService;
    private final SysUserRepository sysUserRepository;

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        var isOperationRejection = (boolean) execution.getVariable("isOperationRejection");
        var isOperation = (boolean) execution.getVariable("isOperation");
        var rejectTypeCode = execution.getVariable("rejectType");

        var caseType = isOperation ? fesService.getBd(DICTIONARY_18, "9") :
                fesService.getBd(DICTIONARY_18, "12");
        var caseObjectType = isOperationRejection || isOperation ?
                fesService.getBd(DICTIONARY_14, "2"):
                fesService.getBd(DICTIONARY_14, "1");
        var caseObjectSubType = isOperation ? fesService.getBd(DICTIONARY_309, "1") :
                fesService.getBd(DICTIONARY_309, "4");
        var status = fesService.getBd(DICTIONARY_38, "1");
        var caseStatus = isOperation ? fesService.getBd(DICTIONARY_305, "2") :
                fesService.getBd(DICTIONARY_305, "1");
        var responsibleUser = isOperation ?
                sysUserRepository.findById((Long) execution.getVariable("responsibleUser")).orElse(null) : null;

        Case aCase = new Case();
        aCase.setName(NAME);
        aCase.setSubname(isOperation ?
                SUBNAME_OPERATION :
                isOperationRejection ?
                SUBNAME_OPERATION_REJECTION : rejectTypeCode.equals("2") ?
                SUBNAME_CONTRACT_REJECTION : SUBNAME_CANCEL_CONTRACT);
        aCase.setCaseType(caseType);
        aCase.setCaseObjectType(caseObjectType);
        aCase.setCaseObjectSubType(caseObjectSubType);
        aCase.setStatus(status);
        aCase.setCaseStatus(caseStatus);
        aCase.setCreationdate(LocalDateTime.now());
        aCase.setResponsibleUser(responsibleUser);
        aCase = caseRepository.save(aCase);

        FesCategory fesCategory = new FesCategory();
        fesCategory.setCaseId(aCase);
        fesCategory.setCategory(caseObjectSubType);
        fesCategory = fesCategoryRepository.save(fesCategory);

        FesCasesStatus fesCasesStatus = new FesCasesStatus();
        fesCasesStatus.setCategoryId(fesCategory);
        fesCasesStatus.setCaseStatus(status);
        fesCasesStatus.setCaseCondition(caseStatus);
        fesCasesStatus = fesCasesStatusRepository.save(fesCasesStatus);

        FesMainPageNew fesMainPageNew = new FesMainPageNew();
        fesMainPageNew.setCasesStatusId(fesCasesStatus);
        fesMainPageNew.setCaseDate(aCase.getCreationdate());
        if (isOperationRejection || isOperation) {
            Payment payment = (Payment) execution.getVariable("payment");
            fillMainPageNew(fesMainPageNew, payment);
            if (isOperation) {
                fesMainPageNew.setOperationStatus(payment.getPaymentSourceStatus());
                fesMainPageNew.setPaymentReference(payment.getPaymentReference() != null ?
                        payment.getPaymentReference() : payment.getExId());
            }
        }
        fesMainPageNewRepository.save(fesMainPageNew);

//        if (isOperation) {
//            FesMainPageOtherSections fesMainPageOtherSections = new FesMainPageOtherSections();
//            fesMainPageOtherSections.setCasesStatusId(fesCasesStatus);
//            fesMainPageOtherSections.setResponsibleUser(responsibleUser);
//            fesMainPageOtherSectionsRepository.save(fesMainPageOtherSections);
//
//            FesMainPageUserDecision fesMainPageUserDecision = new FesMainPageUserDecision();
//            fesMainPageUserDecision.setCategoryId(fesCategory);
//            fesMainPageUserDecision.setChangingDate(LocalDateTime.now());
//            fesMainPageUserDecision.setCaseStatus(status);
//            fesMainPageUserDecision.setCaseCondition(caseStatus);
//            fesMainPageUserDecision.setResponsibleUser(responsibleUser);
//        }

        execution.setVariable("fesCategory", fesCategory);
        execution.setVariable("fesCategoryId", fesCategory.getId());
        execution.setVariable("caseId", aCase.getId());
        execution.setVariable("case", aCase);

    }

    private void fillMainPageNew(FesMainPageNew fesMainPageNew, Payment payment) {
        if (payment != null) {
            fesMainPageNew.setOperationDate(payment.getDateOper());
            fesMainPageNew.setPayerType(payment.getPayerType());
            fesMainPageNew.setPayeeType(payment.getPayeeType());
            fesMainPageNew.setPurpose(payment.getPurpose());
            fesMainPageNew.setPayerName(payment.getPayerName());
            fesMainPageNew.setPayerInn(payment.getPayerInn());
            fesMainPageNew.setPayeeName(payment.getPayeeName());
            fesMainPageNew.setPayeeInn(payment.getPayeeInn());
        }
    }
}
