package com.cognive.projects.casernkb.bpm.cases;

import com.cognive.projects.casernkb.repo.BaseDictRepo;
import com.prime.db.rnkb.model.BaseDictionary;
import com.prime.db.rnkb.model.Case;
import com.prime.db.rnkb.model.CaseOperation;
import com.prime.db.rnkb.model.Payment;
import org.assertj.core.api.Condition;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.extension.mockito.mock.FluentJavaDelegateMock;
import org.junit.Rule;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.assertThat;
import static org.camunda.bpm.extension.mockito.CamundaMockito.*;
import static org.camunda.bpm.extension.mockito.DelegateExpressions.autoMock;
import static org.mockito.Mockito.when;

@Deployment(resources = {
        "bpmn/cases/amlCaseStatusResponse.bpmn"
})
public class AmlCaseStatusResponseTest {
    @Rule
    public ProcessEngineRule processEngineRule = new ProcessEngineRule();

    private String getPayloadJson(Long caseId, String caseType, String caseStatus) {
        return "{\"payload\":{\"amlCaseStatusResponse\":{\"caseId\":" + caseId + ",\"caseType\":\"" + caseType + "\",\"caseStatus\":\"" + caseStatus + "\"}}}";
    }

    @Test
    public void Should_no_response() {
        autoMock("bpmn/cases/amlCaseStatusResponse.bpmn");

        BaseDictionary caseType1 = new BaseDictionary();
        BaseDictionary caseStatus2 = new BaseDictionary();
        caseType1.setCode("1");
        caseStatus2.setCode("2");

        Case caseData = new Case();

        Map<String, Object> selectResult = new HashMap<>();
        selectResult.put("caseData", caseData);

        final FluentJavaDelegateMock selectOneDelegate = registerJavaDelegateMock("selectOneDelegate");
        selectOneDelegate.onExecutionSetVariables(selectResult);

        final BaseDictRepo baseDictionaryRepository = registerMockInstance(BaseDictRepo.class);
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCode(131, "2")).thenReturn(caseStatus2);
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCode(16, "1")).thenReturn(caseType1);

        Map<String, Object> processParams = new HashMap<>();
        processParams.put("payload", getPayloadJson(123L, "1", "2"));

        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("amlCaseStatusResponse", processParams);

        Condition<Object> isCase = new Condition<>(p -> {
            Case checkCase = (Case)p;
            return checkCase.getCaseType().getCode().equals("1") &&
                    checkCase.getStatus().getCode().equals("2");
        }, "isCaseRules size 2");

        assertThat(processInstance)
                .hasPassed("Activity_saveCase", "Event_noResponse")
                .hasNotPassed("Activity_statusPayment")
                .variables()
                .hasEntrySatisfying("caseData", isCase)
        ;
    }

    @Test
    public void Should_no_response_save_payment() {
        autoMock("bpmn/cases/amlCaseStatusResponse.bpmn");

        BaseDictionary caseType4 = new BaseDictionary();
        BaseDictionary caseStatus2 = new BaseDictionary();
        BaseDictionary paymentStatus7 = new BaseDictionary();

        // on control
        // res = 7

        caseType4.setCode("4");
        caseStatus2.setCode("2");
        paymentStatus7.setCode("7");

        Case caseData = new Case();
        CaseOperation caseOperation = new CaseOperation();
        Payment payment = new Payment();

        caseOperation.setPaymentId(payment);
        payment.setPaymentSourceStatus(paymentStatus7);
        caseData.setCaseOperationList(Collections.singletonList(caseOperation));

        Map<String, Object> selectResult = new HashMap<>();
        selectResult.put("caseData", caseData);

        final FluentJavaDelegateMock selectOneDelegate = registerJavaDelegateMock("selectOneDelegate");
        selectOneDelegate.onExecutionSetVariables(selectResult);

        final BaseDictRepo baseDictionaryRepository = registerMockInstance(BaseDictRepo.class);
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCode(174, "2")).thenReturn(caseStatus2);
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCode(16, "4")).thenReturn(caseType4);
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCode(45, "7")).thenReturn(paymentStatus7);

        Map<String, Object> processParams = new HashMap<>();
        processParams.put("payload", getPayloadJson(123L, "4", "2"));

        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("amlCaseStatusResponse", processParams);

        Condition<Object> isCase = new Condition<>(p -> {
            Case checkCase = (Case)p;
            return checkCase.getCaseType().getCode().equals("4") &&
                    checkCase.getStatus().getCode().equals("2") &&
                    !checkCase.getCaseOperationList().isEmpty() &&
                    checkCase.getCaseOperationList().get(0).getPaymentId().getPaymentSourceStatus().getCode().equals("7")
                    ;
        }, "isCaseRules size 2");

        assertThat(processInstance)
                .hasPassed("Activity_saveCase", "Event_noResponse", "Activity_statusPayment")
                .variables()
                .hasEntrySatisfying("caseData", isCase)
        ;
    }

    @Test
    public void Should_response() {
        autoMock("bpmn/cases/amlCaseStatusResponse.bpmn");

        BaseDictionary caseType2 = new BaseDictionary();
        BaseDictionary caseStatus7 = new BaseDictionary();
        BaseDictionary paymentStatus9 = new BaseDictionary();
        BaseDictionary paymentStatus4 = new BaseDictionary();

        // on control
        // res = 7

        caseType2.setCode("2");
        caseStatus7.setCode("7");
        paymentStatus9.setCode("9");
        paymentStatus4.setCode("4");

        Case caseData = new Case();
        CaseOperation caseOperation = new CaseOperation();
        Payment payment = new Payment();

        caseOperation.setPaymentId(payment);
        payment.setPaymentSourceStatus(paymentStatus4);
        caseData.setCaseOperationList(Collections.singletonList(caseOperation));

        Map<String, Object> selectResult = new HashMap<>();
        selectResult.put("caseData", caseData);

        final FluentJavaDelegateMock selectOneDelegate = registerJavaDelegateMock("selectOneDelegate");
        selectOneDelegate.onExecutionSetVariables(selectResult);

        processEngineRule.manageDeployment(registerCallActivityMock("caseResponse")
                .deploy(processEngineRule)
        );

        final BaseDictRepo baseDictionaryRepository = registerMockInstance(BaseDictRepo.class);
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCode(131, "7")).thenReturn(caseStatus7);
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCode(16, "2")).thenReturn(caseType2);
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCode(45, "9")).thenReturn(paymentStatus9);

        Map<String, Object> processParams = new HashMap<>();
        processParams.put("payload", getPayloadJson(123L, "2", "7"));

        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("amlCaseStatusResponse", processParams);

        Condition<Object> isCase = new Condition<>(p -> {
            Case checkCase = (Case)p;
            return checkCase.getCaseType().getCode().equals("2") &&
                    checkCase.getStatus().getCode().equals("7") &&
                    !checkCase.getCaseOperationList().isEmpty() &&
                    checkCase.getCaseOperationList().get(0).getPaymentId().getPaymentSourceStatus().getCode().equals("9")
                    ;
        }, "isCaseRules size 2");

        assertThat(processInstance)
                .hasPassed("Activity_saveCase", "Activity_statusPayment", "Activity_response")
                .variables()
                .hasEntrySatisfying("caseData", isCase)
        ;
    }
}
