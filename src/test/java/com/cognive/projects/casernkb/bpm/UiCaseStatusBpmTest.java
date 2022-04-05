package com.cognive.projects.casernkb.bpm;

import com.prime.db.rnkb.model.Payment;
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
import static org.camunda.bpm.extension.mockito.CamundaMockito.registerCallActivityMock;
import static org.camunda.bpm.extension.mockito.CamundaMockito.registerJavaDelegateMock;
import static org.camunda.bpm.extension.mockito.DelegateExpressions.autoMock;

@Deployment(resources = {
        "bpmn/cases/uiCaseStatus.bpmn"
})
public class UiCaseStatusBpmTest {
    @Rule
    public ProcessEngineRule processEngineRule = new ProcessEngineRule();

    @Test
    public void Should_no_response() {
        autoMock("bpmn/cases/uiCaseStatus.bpmn");

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

        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("uiCaseStatus", processParams);

        assertThat(processInstance)
                .hasPassed("Event_noWork");
    }

    @Test
    public void Should_response() {
        autoMock("bpmn/cases/uiCaseStatus.bpmn");

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

        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("uiCaseStatus", processParams);

        assertThat(processInstance)
                .hasPassed("Event_noWork");
    }
}
