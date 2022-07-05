package com.cognive.projects.casernkb.bpm.judgment;

import com.cognive.projects.casernkb.repo.BaseDictRepo;
import com.prime.db.rnkb.model.BaseDictionary;
import com.prime.db.rnkb.model.Case;
import com.prime.db.rnkb.model.Client;
import com.prime.db.rnkb.model.ClientRbs;
import com.prime.db.rnkb.model.ClientRbsBlock;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.extension.mockito.mock.FluentJavaDelegateMock;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.Disabled;

import java.time.LocalDateTime;
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
public class AmlAutoReasonedJudgmentTest {

    @Rule
    public ProcessEngineRule processEngineRule = new ProcessEngineRule();

    private String getPayloadJson(Long caseId, Long clientId) {
        return "{\"payload\":{\"amlAutoReasonedJudgment\":{\"caseIds\":[" + caseId + "],\"clientId\":" + clientId + ",\"startDate\": \"2022-06-02T00:00:00\"," +
                "\"offDate\": \"2022-06-03T00:00:00\",\"typeRj\": \"3\"}}}";
    }

    @Test
    public void Should_work() {
        autoMock("bpmn/judgment/amlAutoReasonedJudgment.bpmn");

        BaseDictionary bd1 = createBaseDictionary("1");
        BaseDictionary bd2 = createBaseDictionary("2");
        BaseDictionary bd3 = createBaseDictionary("3");
        BaseDictionary bd4 = createBaseDictionary("4");
        BaseDictionary bd5 = createBaseDictionary("5");
        BaseDictionary bdTypeRj = createBaseDictionary("3");

        Case caseData = new Case();
        caseData.setCaseType(bd4);
        Client client = new Client();
        client.setClientOfBankFromDate(LocalDateTime.now());

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
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCode(277, "3")).thenReturn(bdTypeRj);

        Map<String, Object> processParams = new HashMap<>();
        processParams.put("payload", getPayloadJson(123L, 543L));

        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("amlAutoReasonedJudgment", processParams);

        assertThat(processInstance)
                .hasPassed("Activity_payload", "Event_End")
        ;
    }

    private BaseDictionary createBaseDictionary(String code) {
        BaseDictionary baseDictionary = new BaseDictionary();
        baseDictionary.setCode(code);
        return baseDictionary;
    }

}