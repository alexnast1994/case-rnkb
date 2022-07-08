package com.cognive.projects.casernkb.bpm.pipeline;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.junit.Rule;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.assertThat;
import static org.camunda.bpm.extension.mockito.CamundaMockito.registerCallActivityMock;
import static org.camunda.bpm.extension.mockito.DelegateExpressions.autoMock;


@Deployment(resources = {
        "bpmn/pipeline/amlPipelineRequest.bpmn"
})
public class AmlPipelineRequestTest {
    @Rule
    public ProcessEngineRule processEngineRule = new ProcessEngineRule();

    @Test
    public void Should_no_activity() {
        autoMock("bpmn/pipeline/amlPipelineRequest.bpmn");

        Map<String, Object> processParams = new HashMap<>();
        processParams.put("processName", "unknown");

        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("amlPipelineRequest", processParams);

        assertThat(processInstance).hasPassed("Event_noActivity");
    }

    @Test
    public void Should_call_postBatch() {
        autoMock("bpmn/pipeline/amlPipelineRequest.bpmn");

        Map<String, Object> processParams = new HashMap<>();
        processParams.put("processName", "pipeline-payment-response");

        processEngineRule.manageDeployment(registerCallActivityMock("amlPaymentCasePostBatch")
                .deploy(processEngineRule)
        );

        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("amlPipelineRequest", processParams);

        assertThat(processInstance).hasPassed("Activity_callPaymentPostBatch");
    }
}
