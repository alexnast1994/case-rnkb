package com.cognive.projects.casernkb.delegate.fes.ContractCancellation.ManualCaseCreation;

import com.cognive.projects.casernkb.model.fes.FesCaseSaveDto;
import com.cognive.projects.casernkb.service.FesService;
import com.prime.db.rnkb.model.fes.FesCategory;
import com.prime.db.rnkb.repository.SysUserRepository;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_14;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_18;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_305;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_307;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_309;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_38;

@Service
@RequiredArgsConstructor
public class SaveCaseToDbDelegate implements JavaDelegate {

    private final SysUserRepository sysUserRepository;
    private final FesService fesService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        var fesCaseSaveDto = (FesCaseSaveDto) delegateExecution.getVariable("fesCaseSaveDto");
        var rejectTypeCode = fesCaseSaveDto.getFesCategory().getFesRefusalCaseDetails().get(0).getRejectType().getCode();
        if (rejectTypeCode == null) {
            throw new IllegalArgumentException("RejectTypeId cannot be null!");
        }

        var caseObjectType = fesService.getBd(DICTIONARY_14, "1");
        var caseStatus = fesService.getBd(DICTIONARY_38, "2");
        var caseType = fesService.getBd(DICTIONARY_18, "12");
        var caseCategory = fesService.getBd(DICTIONARY_309, "4");
        var caseCondition = fesService.getBd(DICTIONARY_305, "2");
        var responsibleUser = sysUserRepository.findById(fesCaseSaveDto.getSysUser().getId()).orElse(null);
        var rejectType = fesService.getBd(DICTIONARY_307, rejectTypeCode);

        FesCategory fesCategory = fesService.getFesCategory(caseType, caseCategory, caseObjectType, caseStatus, responsibleUser, caseCondition, rejectType, fesCaseSaveDto);

        delegateExecution.setVariable("fesCategoryId", fesCategory.getId());
        delegateExecution.setVariable("rejectTypeCode", rejectTypeCode);
    }
}
