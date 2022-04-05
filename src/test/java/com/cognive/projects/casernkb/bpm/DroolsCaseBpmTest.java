package com.cognive.projects.casernkb.bpm;

import com.prime.db.rnkb.model.*;
import lombok.SneakyThrows;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.extension.mockito.mock.FluentJavaDelegateMock;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.Disabled;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.assertThat;
import static org.camunda.bpm.extension.mockito.CamundaMockito.registerCallActivityMock;
import static org.camunda.bpm.extension.mockito.CamundaMockito.registerJavaDelegateMock;
import static org.camunda.bpm.extension.mockito.DelegateExpressions.autoMock;

@Deployment(resources = {
        "bpmn/cases/droolsCase.bpmn"
})
public class DroolsCaseBpmTest {
    @Rule
    public ProcessEngineRule processEngineRule = new ProcessEngineRule();

    private static final String payloadJsonAccepted = "{\"rules\":[\"01\",\"74\"],\"paymentId\":123}";
    private static final String payloadJsonNoRules = "{\"rules\":[],\"paymentId\":1}";

    @Test
    @SneakyThrows
    public void Should_nothing() {
        autoMock("bpmn/cases/droolsCase.bpmn");

        Payment payment = new Payment();

        Map<String, Object> selectResult = new HashMap<>();
        selectResult.put("payment", payment);

        final FluentJavaDelegateMock selectOneDelegate = registerJavaDelegateMock("selectOneDelegate");
        selectOneDelegate.onExecutionSetVariables(selectResult);

        processEngineRule.manageDeployment(registerCallActivityMock("caseCreate")
                .onExecutionAddVariable("test", "test")
                .deploy(processEngineRule)
        );

        Map<String, Object> processParams = new HashMap<>();
        processParams.put("payload", payloadJsonNoRules);

        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("droolsCase", processParams);

        assertThat(processInstance)
                .hasPassed("Event_noRules");
    }

    @Test
    @SneakyThrows
    public void Should_createCase() {
        autoMock("bpmn/cases/droolsCase.bpmn");

        Payment payment = new Payment();

        Map<String, Object> selectResult = new HashMap<>();
        selectResult.put("payment", payment);

        final FluentJavaDelegateMock selectOneDelegate = registerJavaDelegateMock("selectOneDelegate");
        selectOneDelegate.onExecutionSetVariables(selectResult);

        processEngineRule.manageDeployment(registerCallActivityMock("caseCreate")
                .deploy(processEngineRule)
        );

        Map<String, Object> processParams = new HashMap<>();
        processParams.put("payload", payloadJsonAccepted);

        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("droolsCase", processParams);

        assertThat(processInstance)
                .hasPassed("Event_createCase", "Activity_callCreateCase");
    }

    @Test
    @Disabled
    @SneakyThrows
    public void Should_check_codes() {
        autoMock("bpmn/cases/droolsCase.bpmn");

        BaseDictionary crCode1 = new BaseDictionary();
        BaseDictionary crCode2 = new BaseDictionary();

        CaseRules cr1 = new CaseRules();
        CaseRules cr2 = new CaseRules();

        crCode1.setCode("01");
        crCode2.setCode("74");

        cr1.setCode(crCode1);
        cr2.setCode(crCode2);

        Case caseData = new Case();
        caseData.setСaseRules(Arrays.asList(cr1, cr2));

        CaseOperation caseOperation = new CaseOperation();
        caseOperation.setCaseId(caseData);

        Payment payment = new Payment();
        payment.setCaseOperationList(Collections.singletonList(caseOperation));

        Map<String, Object> selectResult = new HashMap<>();
        selectResult.put("payment", payment);

        final FluentJavaDelegateMock selectOneDelegate = registerJavaDelegateMock("selectOneDelegate");
        selectOneDelegate.onExecutionSetVariables(selectResult);

        processEngineRule.manageDeployment(registerCallActivityMock("caseCreate")
                .deploy(processEngineRule)
        );

        Map<String, Object> processParams = new HashMap<>();
        processParams.put("payload", payloadJsonAccepted);

        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("droolsCase", processParams);

        assertThat(processInstance)
                .hasPassed("Activity_checkCodes", "Event_noNewCodes");
    }
}
