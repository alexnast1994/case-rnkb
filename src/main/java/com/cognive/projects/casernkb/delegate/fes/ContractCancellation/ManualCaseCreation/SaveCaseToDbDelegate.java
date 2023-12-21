package com.cognive.projects.casernkb.delegate.fes.ContractCancellation.ManualCaseCreation;

import com.cognive.projects.casernkb.model.fes.FesCaseSaveDto;
import com.cognive.projects.casernkb.service.FesService;
import com.prime.db.rnkb.model.fes.FesCategory;
import com.prime.db.rnkb.repository.SysUserRepository;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_307;

@Service
@RequiredArgsConstructor
public class SaveCaseToDbDelegate implements JavaDelegate {

    private final SysUserRepository sysUserRepository;
    private final FesService fesService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        var fesCaseSaveDto = (FesCaseSaveDto) delegateExecution.getVariable("fesCaseSaveDto");

        var fesCategoryCode = fesCaseSaveDto.getFesCategory().getCategory().getCode();
        var rejectTypeCode = fesCaseSaveDto.getFesCategory().getFesRefusalCaseDetails().get(0).getRejectType().getCode();

        var caseObjectType = fesService.getCaseObjectType(fesCategoryCode, rejectTypeCode);
        var caseStatus = fesService.getCaseStatus();
        var caseType = fesService.getCaseType(fesCategoryCode);
        var caseCategory = fesService.getCaseCategory(fesCategoryCode);
        var caseCondition = fesService.getCaseCondition();
        var responsibleUser = sysUserRepository.findById(fesCaseSaveDto.getSysUser().getId()).orElse(null);
        var rejectType = fesService.getBd(DICTIONARY_307, rejectTypeCode);

        FesCategory fesCategory = fesService.getFesCategory(caseType, caseCategory, caseObjectType, caseStatus, responsibleUser, caseCondition, rejectType, fesCaseSaveDto);

        delegateExecution.setVariable("fesCategoryId", fesCategory.getId());
        delegateExecution.setVariable("rejectTypeCode", rejectTypeCode);
        delegateExecution.setVariable("fesCategoryCode", fesCategoryCode);
    }
}
