package com.cognive.projects.casernkb.delegate;

import com.djn.client.ClientCheckResult;
import com.djn.common.KYCList;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


@Component
public class KYCCheckStatusList implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        List<ClientCheckResult> clientCheckResultsList = (List<ClientCheckResult>) delegateExecution.getVariable("clientCheckResults");
        List<ClientCheckResult> clientCheckResultsListOut = clientCheckResultsList.stream().filter(kyc-> kyc.getKYCCheckStatus().equals(true)).collect(Collectors.toList());
        List<KYCList> kycLists = clientCheckResultsList.get(0).getKYCList();
        List<KYCList> kycListsOut = kycLists.stream().filter(kyc-> kyc.getCheckStatus().equals("1") || kyc.getCheckStatus().equals("2")).collect(Collectors.toList());
        delegateExecution.setVariable("clientCheckResultsList",clientCheckResultsListOut);
        delegateExecution.setVariable("kyclist",kycListsOut);
        delegateExecution.setVariable("sizeOfKyc",kycListsOut.size()-1);
    }
}
