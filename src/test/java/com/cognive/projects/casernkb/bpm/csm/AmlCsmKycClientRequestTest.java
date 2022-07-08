package com.cognive.projects.casernkb.bpm.csm;

import com.cognive.projects.casernkb.bpm.common.ResourceHelper;
import com.cognive.projects.casernkb.repo.BaseDictRepo;
import com.cognive.projects.casernkb.repo.CaseRepo;
import com.prime.db.rnkb.model.BaseDictionary;
import com.prime.db.rnkb.model.Case;
import com.prime.db.rnkb.model.Client;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.extension.mockito.mock.FluentJavaDelegateMock;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.assertThat;
import static org.camunda.bpm.extension.mockito.CamundaMockito.registerJavaDelegateMock;
import static org.camunda.bpm.extension.mockito.CamundaMockito.registerMockInstance;
import static org.camunda.bpm.extension.mockito.DelegateExpressions.autoMock;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Deployment(resources = {
        "bpmn/csm/amlCsmKycClientRequest.bpmn"
})
public class AmlCsmKycClientRequestTest {

    @Rule
    public ProcessEngineRule processEngineRule = new ProcessEngineRule();

    String getPayload() {
        return ResourceHelper.readString("csm_kyc_client_request.json");
    }

    @Test
    public void Should_response() {
        autoMock("bpmn/csm/amlCsmKycClientRequest.bpmn");

        final var sourceStatus7 = new BaseDictionary();
        sourceStatus7.setCode("7");

        final var client = new Client();
        final var cs = new Case();
        cs.setStatus(new BaseDictionary().setCode("1"));

        final var selectResult = new HashMap<String, Object>() {{
            put("client", client);
        }};

        final FluentJavaDelegateMock selectOneDelegate = registerJavaDelegateMock("selectOneDelegate");
        selectOneDelegate.onExecutionSetVariables(selectResult);

        final var caseRepoMock = registerMockInstance(CaseRepo.class);
        when(caseRepoMock.getLatestCaseByClientIdAndExIdAndNumAndTypeList(any(), any(), any(), any()))
                .thenReturn(List.of(cs));

        final var baseDictionaryRepository = registerMockInstance(BaseDictRepo.class);
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCode(45, "7"))
                .thenReturn(sourceStatus7);

        final var processParams = new HashMap<String, Object>();
        final var jsonData = Variables
                .objectValue(getPayload())
                .serializationDataFormat(MediaType.APPLICATION_JSON_VALUE)
                .create();

        processParams.put("payload", jsonData);
        processParams.put("processName", "clientProcess");

        final var runtimeService = processEngineRule.getRuntimeService();
        final var processInstance = runtimeService.startProcessInstanceByKey("amlCsmKycClientRequest", processParams);

        assertThat(processInstance)
                .hasPassed("Activity_Case_Saving", "Activity_List1_Saving", "Activity_List2_Saving", "Activity_Save_Lists")
                .variables();
    }

}
