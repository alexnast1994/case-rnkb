package com.cognive.projects.casernkb.bpm.csm;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.ObjectValue;
import org.junit.Rule;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.assertThat;
import static org.camunda.bpm.extension.mockito.CamundaMockito.registerCallActivityMock;
import static org.camunda.bpm.extension.mockito.DelegateExpressions.autoMock;

@Deployment(resources = {
        "bpmn/csm/amlCsmRequest.bpmn"
})
public class AmlCsmRequestTest {
    @Rule
    public ProcessEngineRule processEngineRule = new ProcessEngineRule();

    @Test
    public void Should_response() {
        autoMock("bpmn/csm/amlCsmRequest.bpmn");

        Map<String, Object> processParams = new HashMap<>();
        ObjectValue jsonData = Variables.objectValue("{}").serializationDataFormat("application/json").create();
        processParams.put("payload", jsonData);
        processParams.put("processName", "unknown");

        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("amlCsmRequest", processParams);

        assertThat(processInstance).hasPassed("Event_noActivity");
    }

    @Test
    public void Should_call_payment() {
        autoMock("bpmn/csm/amlCsmRequest.bpmn");

        Map<String, Object> processParams = new HashMap<>();
        ObjectValue jsonData = Variables.objectValue("{}").serializationDataFormat("application/json").create();
        processParams.put("payload", jsonData);
        processParams.put("processName", "operationProcess");

        processEngineRule.manageDeployment(registerCallActivityMock("amlCsmKycPaymentRequest")
                .deploy(processEngineRule)
        );

        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("amlCsmRequest", processParams);

        assertThat(processInstance).hasPassed("Activity_kycPayment");
    }

    @Test
    public void Should_call_client_online() {
        autoMock("bpmn/csm/amlCsmRequest.bpmn");

        Map<String, Object> processParams = new HashMap<>();
        ObjectValue jsonData = Variables.objectValue("{}").serializationDataFormat("application/json").create();
        processParams.put("payload", jsonData);
        processParams.put("processName", "clientProcess");

        processEngineRule.manageDeployment(registerCallActivityMock("amlCsmKycClientRequest")
                .deploy(processEngineRule)
        );

        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("amlCsmRequest", processParams);

        assertThat(processInstance).hasPassed("Activity_kycClient");
    }

    @Test
    public void Should_call_client_offline() {
        autoMock("bpmn/csm/amlCsmRequest.bpmn");

        Map<String, Object> processParams = new HashMap<>();
        ObjectValue jsonData = Variables.objectValue("{}").serializationDataFormat("application/json").create();
        processParams.put("payload", jsonData);
        processParams.put("processName", "offlineCreateCase");

        processEngineRule.manageDeployment(registerCallActivityMock("amlCsmKycClientRequest")
                .deploy(processEngineRule)
        );

        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("amlCsmRequest", processParams);

        assertThat(processInstance).hasPassed("Activity_kycClient");
    }
}
