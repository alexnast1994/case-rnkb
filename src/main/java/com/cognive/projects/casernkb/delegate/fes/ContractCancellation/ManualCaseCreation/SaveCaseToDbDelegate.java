package com.cognive.projects.casernkb.delegate.fes.ContractCancellation.ManualCaseCreation;

import com.cognive.projects.casernkb.model.fes.FesCaseSaveDto;
import com.cognive.projects.casernkb.service.FesService;
import com.prime.db.rnkb.model.BaseDictionary;
import com.prime.db.rnkb.model.fes.FesCategory;
import com.prime.db.rnkb.repository.BaseDictionaryRepository;
import com.prime.db.rnkb.repository.SysUserRepository;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SaveCaseToDbDelegate implements JavaDelegate {

    private final SysUserRepository sysUserRepository;
    private final BaseDictionaryRepository baseDictionaryRepository;
    private final FesService fesService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        var fesCaseSaveDto = (FesCaseSaveDto) delegateExecution.getVariable("fesCaseSaveDto");
        var rejectTypeId = fesCaseSaveDto.getFesCategory().getFesRefusalCaseDetails().get(0).getRejectTypeId();
        if (rejectTypeId == null) {
            throw new IllegalArgumentException("RejectTypeId cannot be null!");
        }

        var caseObjectType = baseDictionaryRepository.getBaseDictionary("1", 14);
        var caseStatus = baseDictionaryRepository.getBaseDictionary("2", 38);
        var caseType = baseDictionaryRepository.getBaseDictionary("12", 18);
        var caseCategory = baseDictionaryRepository.getBaseDictionary("4", 309);
        var caseCondition = baseDictionaryRepository.getBaseDictionary("2", 305);
        var responsibleUser = sysUserRepository.findById(fesCaseSaveDto.getSysUser().getId()).orElseThrow();
        var rejectType = baseDictionaryRepository.findById(rejectTypeId);

        FesCategory fesCategory = fesService.getFesCategory(caseType, caseCategory, caseObjectType, caseStatus, responsibleUser, caseCondition, rejectType, fesCaseSaveDto);

        delegateExecution.setVariable("fesCategoryId", fesCategory.getId());
        delegateExecution.setVariable("rejectTypeCode", rejectType.map(BaseDictionary::getCode).orElse(null));
    }
}
