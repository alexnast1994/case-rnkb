package com.cognive.projects.casernkb.bpm.cases;

import com.cognive.projects.casernkb.bpm.TestUtils;
import com.cognive.projects.casernkb.repo.BaseDictRepo;
import com.prime.db.rnkb.model.BaseDictionary;
import com.prime.db.rnkb.model.Case;
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
import java.util.List;
import java.util.Map;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.assertThat;
import static org.camunda.bpm.extension.mockito.CamundaMockito.*;
import static org.camunda.bpm.extension.mockito.DelegateExpressions.autoMock;
import static org.mockito.Mockito.when;

@Deployment(resources = {
        "bpmn/cases/paymentResponse.bpmn"
})
public class PaymentResponseTest {
    @Rule
    public ProcessEngineRule processEngineRule = new ProcessEngineRule();

    @Test
    public void Should_response_with_save_case() {
        autoMock("bpmn/cases/paymentResponse.bpmn");

        BaseDictionary sourceStatus10 = new BaseDictionary();
        sourceStatus10.setCode("10");

        BaseDictionary caseType2 = new BaseDictionary();
        caseType2.setCode("2");

        BaseDictionary caseRule04 = new BaseDictionary();
        caseRule04.setCode("4");
        caseRule04.setCharCode("04");

        BaseDictionary caseRule05 = new BaseDictionary();
        caseRule05.setCode("5");
        caseRule05.setCharCode("05");

        Payment payment = new Payment();
        Case ss = new Case();
        ss.setId(4);

//        Map<String, Object> selectResult = new HashMap<>();
//        selectResult.put("payment", payment);

//        final FluentJavaDelegateMock selectOneDelegate = registerJavaDelegateMock("selectOneDelegate");
//        selectOneDelegate.onExecutionSetVariables(selectResult);

        Map<String, Object> saveCaseData = new HashMap<>();
        saveCaseData.put("caseDataOut", ss);

        final FluentJavaDelegateMock saveCase = registerJavaDelegateMock("saveObjectDelegate");
        saveCase.onExecutionSetVariables(saveCaseData);

        final BaseDictRepo baseDictionaryRepository = registerMockInstance(BaseDictRepo.class);
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCode(45, "10")).thenReturn(sourceStatus10);
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCode(18, "2")).thenReturn(caseType2);
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCharCode(272, "04")).thenReturn(caseRule04);
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCharCode(272, "05")).thenReturn(caseRule05);

        Map<String, Object> processParams = new HashMap<>();
        processParams.put("payment", payment);
        processParams.put("rules", List.of("04", "05"));

        processEngineRule.manageDeployment(registerCallActivityMock("caseResponse")
                .deploy(processEngineRule)
        );

        processEngineRule.manageDeployment(registerCallActivityMock("caseCreate")
                .deploy(processEngineRule)
        );

        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("paymentResponse", processParams);

        Condition<Object> isCaseType = new Condition<>(p -> {
            Case checkCase = (Case)p;
            return checkCase.getCaseType().getCode().equals("2");
        }, "isCaseRules size 2");

        assertThat(processInstance)
            .hasPassed("Activity_createCase", "Activity_case", "Activity_response")
            .variables()
            .hasEntrySatisfying("caseData", isCaseType)
        ;
    }

    @Test
    public void Should_response() {
        autoMock("bpmn/cases/paymentResponse.bpmn");

        BaseDictionary sourceStatus7 = new BaseDictionary();
        sourceStatus7.setCode("7");

        Payment payment = new Payment();
        payment.setPaymentSourceStatus(TestUtils.getBaseDictionary("1"));
        payment.setId(123L);

        final BaseDictRepo baseDictionaryRepository = registerMockInstance(BaseDictRepo.class);
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCode(45, "7")).thenReturn(sourceStatus7);

        Map<String, Object> processParams = new HashMap<>();
        processParams.put("payment", payment);
        processParams.put("rules", Collections.emptyList());

        processEngineRule.manageDeployment(registerCallActivityMock("caseResponse")
                .deploy(processEngineRule)
        );

        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("paymentResponse", processParams);

        Condition<Object> isPaymentSource = new Condition<>(p -> {
            Payment checkPayment = (Payment) p;
            return checkPayment.getPaymentSourceStatus().getCode().equals("7");
        }, "isCaseRules size 2");

        assertThat(processInstance)
                .hasPassed("Activity_paymentStatus7", "Activity_savePayment", "Activity_response")
                .variables()
                .hasEntrySatisfying("payment", isPaymentSource)
        ;
    }
}
