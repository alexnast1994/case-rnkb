package com.cognive.projects.casernkb.delegate.fes.ContractCancellation.AutoCaseCreation;

import com.prime.db.rnkb.model.Case;
import com.prime.db.rnkb.model.fes.FesCasesStatus;
import com.prime.db.rnkb.model.fes.FesCategory;
import com.prime.db.rnkb.model.fes.FesMainPageNew;
import com.prime.db.rnkb.repository.BaseDictionaryRepository;
import com.prime.db.rnkb.repository.CaseRepository;
import com.prime.db.rnkb.repository.fes.FesCasesStatusRepository;
import com.prime.db.rnkb.repository.fes.FesCategoryRepository;
import com.prime.db.rnkb.repository.fes.FesMainPageNewRepository;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class FesCreateMainTablesDelegate implements JavaDelegate {
    private static final String NAME = "ФЭС";
    private static final String SUBNAME = "Отказ от заключения договора";
    private static final String SUBNAME_CANCEL_CONTRACT = "Расторжение договора";

    private final BaseDictionaryRepository baseDictionaryRepository;
    private final CaseRepository caseRepository;
    private final FesCategoryRepository fesCategoryRepository;
    private final FesMainPageNewRepository fesMainPageNewRepository;
    private final FesCasesStatusRepository fesCasesStatusRepository;

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        var rejectTypeCode = (String) execution.getVariable("rejectType");

        var caseType = baseDictionaryRepository.getBaseDictionary("12", 18);
        var caseObjectType = baseDictionaryRepository.getBaseDictionary("1", 14);
        var caseObjectSubType = baseDictionaryRepository.getBaseDictionary("4", 309);
        var status = baseDictionaryRepository.getBaseDictionary("1", 38);
        var caseStatus = baseDictionaryRepository.getBaseDictionary("1", 305);

        Case aCase = new Case();
        aCase.setName(NAME);
        aCase.setSubname(rejectTypeCode.equals("2") ? SUBNAME : SUBNAME_CANCEL_CONTRACT);
        aCase.setCaseType(caseType);
        aCase.setCaseObjectType(caseObjectType);
        aCase.setCaseObjectSubType(caseObjectSubType);
        aCase.setStatus(status);
        aCase.setCaseStatus(caseStatus);
        aCase.setCreationdate(LocalDateTime.now());
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
        fesMainPageNewRepository.save(fesMainPageNew);

        execution.setVariable("fesCategory", fesCategory);
        execution.setVariable("fesCategoryId", fesCategory.getId());
        execution.setVariable("caseId", aCase.getId());

    }
}
