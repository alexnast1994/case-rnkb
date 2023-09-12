package com.cognive.projects.casernkb.delegate.fes.ContractCancellation.ManualCaseCreation;

import com.cognive.projects.casernkb.model.fes.FesCaseSaveDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prime.db.rnkb.model.BaseDictionary;
import com.prime.db.rnkb.model.Case;
import com.prime.db.rnkb.model.SysUser;
import com.prime.db.rnkb.model.fes.FesCasesStatus;
import com.prime.db.rnkb.model.fes.FesCategory;
import com.prime.db.rnkb.model.fes.FesMainPageNew;
import com.prime.db.rnkb.model.fes.FesMainPageOtherSections;
import com.prime.db.rnkb.model.fes.FesMainPageUserDecision;
import com.prime.db.rnkb.model.fes.FesRefusalCaseDetails;
import com.prime.db.rnkb.repository.BaseDictionaryRepository;
import com.prime.db.rnkb.repository.CaseRepository;
import com.prime.db.rnkb.repository.SysUserRepository;
import com.prime.db.rnkb.repository.fes.FesCasesStatusRepository;
import com.prime.db.rnkb.repository.fes.FesCategoryRepository;
import com.prime.db.rnkb.repository.fes.FesMainPageNewRepository;
import com.prime.db.rnkb.repository.fes.FesMainPageOtherSectionsRepository;
import com.prime.db.rnkb.repository.fes.FesMainPageUserDecisionRepository;
import com.prime.db.rnkb.repository.fes.FesRefusalCaseDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SaveCaseToDbDelegate implements JavaDelegate {

    private static final String NAME = "ФЭС";
    private static final String SUBNAME = "Отказ от заключения договора (расторжение)";

    private final CaseRepository caseRepository;
    private final SysUserRepository sysUserRepository;
    private final FesCategoryRepository fesCategoryRepository;
    private final FesCasesStatusRepository fesCasesStatusRepository;
    private final BaseDictionaryRepository baseDictionaryRepository;
    private final FesMainPageNewRepository fesMainPageNewRepository;
    private final FesRefusalCaseDetailsRepository fesRefusalCaseDetailsRepository;
    private final FesMainPageUserDecisionRepository fesMainPageUserDecisionRepository;
    private final FesMainPageOtherSectionsRepository fesMainPageOtherSectionsRepository;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public void execute(DelegateExecution delegateExecution) throws Exception {

        var jsonData = delegateExecution.getVariable("payload");

        FesCaseSaveDto fesCaseSaveDto = objectMapper.convertValue(jsonData, FesCaseSaveDto.class);

        var caseObjectType = baseDictionaryRepository.getBaseDictionary("1", 14);
        var caseStatus = baseDictionaryRepository.getBaseDictionary("2", 38);
        var caseType = baseDictionaryRepository.getBaseDictionary("12", 18);
        var caseCategory = baseDictionaryRepository.getBaseDictionary("4", 309);
        var caseCondition = baseDictionaryRepository.getBaseDictionary("2", 305);
        var responsibleUser = sysUserRepository.findById(fesCaseSaveDto.getSysUser().getId()).orElseThrow();
        var rejectType = baseDictionaryRepository.findById(fesCaseSaveDto.getFesCategory().getFesRefusalCaseDetails().get(0).getRejectTypeId());

        Case aCase = createCase(caseType, caseCategory, caseObjectType, caseStatus, responsibleUser, caseCondition);
        FesCategory fesCategory = createFesCategory(aCase, caseCategory);
        createFesRefusalCaseDetails(fesCategory, rejectType);
        FesCasesStatus fesCasesStatus = createFesCasesStatus(fesCategory, caseStatus, caseCondition);
        createFesMainPageNew(fesCasesStatus, aCase);
        createFesMainPageOtherSections(responsibleUser, fesCasesStatus, fesCaseSaveDto);
        createFesMainPageUserDecision(responsibleUser, fesCategory, caseStatus, caseCondition, fesCaseSaveDto);

        delegateExecution.setVariable("categoryId", fesCategory.getId());
    }

    @NotNull
    private FesCategory createFesCategory(Case aCase, BaseDictionary caseCategory) {
        FesCategory fesCategory = new FesCategory();
        fesCategory.setCaseId(aCase);
        fesCategory.setCategory(caseCategory);
        fesCategory = fesCategoryRepository.save(fesCategory);
        return fesCategory;
    }

    private void createFesRefusalCaseDetails(FesCategory fesCategory, Optional<BaseDictionary> rejectType) {
        FesRefusalCaseDetails fesRefusalCaseDetails = new FesRefusalCaseDetails();
        fesRefusalCaseDetails.setCategoryId(fesCategory);
        fesRefusalCaseDetails.setRejectType(rejectType.orElseThrow());
        fesRefusalCaseDetailsRepository.save(fesRefusalCaseDetails);
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
