package com.cognive.projects.casernkb.bpm.judgment;

import com.cognive.projects.casernkb.repo.BaseDictRepo;
import com.prime.db.rnkb.model.*;
import org.assertj.core.api.Condition;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.extension.mockito.mock.FluentJavaDelegateMock;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.Disabled;

import java.util.HashMap;
import java.util.Map;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.assertThat;
import static org.camunda.bpm.extension.mockito.CamundaMockito.registerJavaDelegateMock;
import static org.camunda.bpm.extension.mockito.CamundaMockito.registerMockInstance;
import static org.camunda.bpm.extension.mockito.DelegateExpressions.autoMock;
import static org.mockito.Mockito.when;

@Deployment(resources = {
        "bpmn/judgment/amlAutoReasonedJudgment.bpmn"
})
@Disabled
// TODO:
public class AmlAutoReasonedJudgmentTest {
    @Rule
    public ProcessEngineRule processEngineRule = new ProcessEngineRule();

    private String getPayloadJson(Long caseId, Long clientId) {
        return "{\"payload\":{\"amlAutoReasonedJudgment\":{\"caseIds\":[" + caseId + "],\"clientId\":" + clientId + ",\"startDate\": \"2022-06-02T00:00:00\"," +
                "\"offDate\": \"2022-06-03T00:00:00\"}}}";
    }

    // TODO
    @Test
    public void Should_work() {
        autoMock("bpmn/judgment/amlAutoReasonedJudgment.bpmn");

        BaseDictionary bd1 = new BaseDictionary();
        BaseDictionary bd2 = new BaseDictionary();
        BaseDictionary bd3 = new BaseDictionary();
        BaseDictionary bd4 = new BaseDictionary();
        BaseDictionary bd5 = new BaseDictionary();
        bd1.setCode("1");
        bd2.setCode("2");
        bd3.setCode("3");
        bd4.setCode("4");
        bd5.setCode("5");

        Case caseData = new Case();
        caseData.setCaseType(bd4);
        Client client = new Client();

        ClientRbs clientRbs = new ClientRbs();
        ClientRbsBlock rbsBlock = new ClientRbsBlock(); // TODO implements Serializable
        rbsBlock.setClientRbs(clientRbs);

        clientRbs.setStatus(bd3);

        Map<String, Object> selectResult = new HashMap<>();
        selectResult.put("caseBase", caseData);
        selectResult.put("clientBase", client);
        selectResult.put("rbsBlockBase", rbsBlock);

        final FluentJavaDelegateMock selectOneDelegate = registerJavaDelegateMock("selectOneDelegate");
        selectOneDelegate.onExecutionSetVariables(selectResult);

        final BaseDictRepo baseDictionaryRepository = registerMockInstance(BaseDictRepo.class);
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCode(213, "5")).thenReturn(bd5);
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCode(266, "1")).thenReturn(bd1);
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCode(269, "3")).thenReturn(bd3);
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCode(181, "2")).thenReturn(bd2);

        Map<String, Object> processParams = new HashMap<>();
        processParams.put("payload", getPayloadJson(123L, 543L));

        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("amlAutoReasonedJudgment", processParams);

        assertThat(processInstance)
                .hasPassed("Activity_payload", "Event_End")
        ;
    }
}