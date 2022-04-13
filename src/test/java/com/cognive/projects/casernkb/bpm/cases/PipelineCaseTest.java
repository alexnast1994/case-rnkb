package com.cognive.projects.casernkb.bpm.cases;

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

import java.util.HashMap;
import java.util.Map;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.assertThat;
import static org.camunda.bpm.extension.mockito.CamundaMockito.*;
import static org.camunda.bpm.extension.mockito.DelegateExpressions.autoMock;
import static org.mockito.Mockito.when;

@Deployment(resources = {
        "bpmn/cases/pipelineCase.bpmn"
})
public class PipelineCaseTest {
    @Rule
    public ProcessEngineRule processEngineRule = new ProcessEngineRule();

    private static final String payloadJson = "{\"rules\":[\"04\",\"05\"],\"paymentId\":123}";
    private static final String payloadNoRulesJson = "{\"rules\":[],\"paymentId\":123}";

    @Test
    public void Should_response_with_save_case() {
        autoMock("bpmn/cases/pipelineCase.bpmn");

        BaseDictionary sourceStatus10 = new BaseDictionary();
        sourceStatus10.setCode("10");

        BaseDictionary caseType2 = new BaseDictionary();
        caseType2.setCode("2");

        Payment payment = new Payment();

        Map<String, Object> selectResult = new HashMap<>();
        selectResult.put("payment", payment);

        final FluentJavaDelegateMock selectOneDelegate = registerJavaDelegateMock("selectOneDelegate");
        selectOneDelegate.onExecutionSetVariables(selectResult);

        final BaseDictRepo baseDictionaryRepository = registerMockInstance(BaseDictRepo.class);
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCode(45, "10")).thenReturn(sourceStatus10);
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCode(18, "2")).thenReturn(caseType2);

        Map<String, Object> processParams = new HashMap<>();
        processParams.put("payload", payloadJson);

        processEngineRule.manageDeployment(registerCallActivityMock("caseResponse")
                .deploy(processEngineRule)
        );

        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("pipelineCase", processParams);

        Condition<Object> isCaseType = new Condition<>(p -> {
            Case checkCase = (Case)p;
            return checkCase.getCaseType().getCode().equals("2");
        }, "isCaseRules size 2");

        assertThat(processInstance)
            .hasPassed("Activity_createCase", "Activity_saveCase", "Activity_response")
            .variables()
            .hasEntrySatisfying("caseData", isCaseType)
        ;
    }

    @Test
    public void Should_response() {
        autoMock("bpmn/cases/pipelineCase.bpmn");

        BaseDictionary sourceStatus7 = new BaseDictionary();
        sourceStatus7.setCode("7");

        Payment payment = new Payment();

        Map<String, Object> selectResult = new HashMap<>();
        selectResult.put("payment", payment);

        final FluentJavaDelegateMock selectOneDelegate = registerJavaDelegateMock("selectOneDelegate");
        selectOneDelegate.onExecutionSetVariables(selectResult);

        final BaseDictRepo baseDictionaryRepository = registerMockInstance(BaseDictRepo.class);
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCode(45, "7")).thenReturn(sourceStatus7);

        Map<String, Object> processParams = new HashMap<>();
        processParams.put("payload", payloadNoRulesJson);

        processEngineRule.manageDeployment(registerCallActivityMock("caseResponse")
                .deploy(processEngineRule)
        );

        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("pipelineCase", processParams);

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
