package com.cognive.projects.casernkb.delegate.fes.ContractCancellation.AutoCaseCreation;

import com.cognive.projects.casernkb.service.FesService;
import com.prime.db.rnkb.model.fes.FesBankInformation;
import com.prime.db.rnkb.model.fes.FesCategory;
import com.prime.db.rnkb.model.fes.FesDataPrefill;
import com.prime.db.rnkb.model.fes.FesGeneralInformation;
import com.prime.db.rnkb.model.fes.FesRefusalCaseDetails;
import com.prime.db.rnkb.model.fes.FesServiceInformation;
import com.prime.db.rnkb.repository.fes.FesBankInformationRepository;
import com.prime.db.rnkb.repository.fes.FesDataPrefillRepository;
import com.prime.db.rnkb.repository.fes.FesGeneralInformationRepository;
import com.prime.db.rnkb.repository.fes.FesRefusalCaseDetailsRepository;
import com.prime.db.rnkb.repository.fes.FesServiceInformationRepository;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_307;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_312;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_318;
import static com.cognive.projects.casernkb.constant.FesConstants.DICTIONARY_86;

@Component
@RequiredArgsConstructor
public class FesAutoSaveGeneralInformationDelegate implements JavaDelegate {

    private final FesDataPrefillRepository fesDataPrefillRepository;
    private final FesBankInformationRepository fesBankInformationRepository;
    private final FesServiceInformationRepository fesServiceInformationRepository;
    private final FesGeneralInformationRepository fesGeneralInformationRepository;
    private final FesRefusalCaseDetailsRepository fesRefusalCaseDetailsRepository;
    private final FesService fesService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        var fesCategory = (FesCategory) execution.getVariable("fesCategory");
        var rejectTypeCode = (String) execution.getVariable("rejectType");

        var recordType = fesService.getBd(DICTIONARY_86, "1");
        var groundOfRefusal = rejectTypeCode.equals("2") ?
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
        fesGeneralInformationRepository.save(fesGeneralInformation);

        FesRefusalCaseDetails fesRefusalCaseDetails = new FesRefusalCaseDetails();
        fesRefusalCaseDetails.setCategoryId(fesCategory);
        fesRefusalCaseDetails.setRefusalDate(LocalDateTime.now());
        fesRefusalCaseDetails.setGroundOfRefusal(groundOfRefusal);
        fesRefusalCaseDetails.setRejectType(rejectType);
        fesRefusalCaseDetailsRepository.save(fesRefusalCaseDetails);
    }
}
