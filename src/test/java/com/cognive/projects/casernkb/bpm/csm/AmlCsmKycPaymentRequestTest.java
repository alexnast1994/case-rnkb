package com.cognive.projects.casernkb.bpm.csm;

import com.cognive.projects.casernkb.bpm.common.ResourceHelper;
import com.cognive.projects.casernkb.repo.BaseDictRepo;
import com.prime.db.rnkb.model.BaseDictionary;
import com.prime.db.rnkb.model.Payment;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.ObjectValue;
import org.camunda.bpm.extension.mockito.mock.FluentJavaDelegateMock;
import org.junit.Rule;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.assertThat;
import static org.camunda.bpm.extension.mockito.CamundaMockito.registerJavaDelegateMock;
import static org.camunda.bpm.extension.mockito.CamundaMockito.registerMockInstance;
import static org.camunda.bpm.extension.mockito.DelegateExpressions.autoMock;
import static org.mockito.Mockito.when;

@Deployment(resources = {
        "bpmn/csm/amlCsmKycPaymentRequest.bpmn"
})
public class AmlCsmKycPaymentRequestTest {
    @Rule
    public ProcessEngineRule processEngineRule = new ProcessEngineRule();

    String getPayload() {
        return ResourceHelper.readString("csm_request.json");
    }

    @Test
    public void Should_response() {
        autoMock("bpmn/csm/amlCsmKycPaymentRequest.bpmn");

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
        ObjectValue jsonData = Variables.objectValue(getPayload()).serializationDataFormat("application/json").create();
        processParams.put("payload", jsonData);

        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("amlCsmKycPaymentRequest", processParams);

        assertThat(processInstance)
                .hasPassed("Activity_saveKyc", "Activity_saveResultPayment", "Activity_saveResultPaymentDetails")
                .variables()
        ;
    }
}
