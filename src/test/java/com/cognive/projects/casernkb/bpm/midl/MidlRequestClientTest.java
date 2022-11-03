package com.cognive.projects.casernkb.bpm.midl;

import com.cognive.projects.casernkb.repo.BaseDictRepo;
import com.prime.db.rnkb.model.BaseDictionary;
import com.prime.db.rnkb.model.Case;
import com.prime.db.rnkb.model.CaseClient;
import com.prime.db.rnkb.model.Client;
import com.prime.db.rnkb.model.commucation.midl.ChangingTimingTask;
import com.prime.db.rnkb.model.commucation.midl.Task;
import com.prime.db.rnkb.model.commucation.midl.ZkTaskCases;
import com.prime.db.rnkb.model.commucation.request.Request;
import org.assertj.core.api.Condition;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.extension.mockito.mock.FluentJavaDelegateMock;
import org.junit.Rule;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.assertThat;
import static org.camunda.bpm.extension.mockito.CamundaMockito.*;
import static org.camunda.bpm.extension.mockito.DelegateExpressions.autoMock;
import static org.mockito.Mockito.when;

@Deployment(resources = {
        "bpmn/midl/amlMidlRequestClient.bpmn"
})
public class MidlRequestClientTest {
    @Rule
    public ProcessEngineRule processEngineRule = new ProcessEngineRule();

    private String getPayloadJson(Long caseId, Long requestId, Boolean dboRequest) {
        return "{\"payload\":{\"amlMidlRequestClient\":{\"caseId\":" + caseId + ",\"requestId\":" + requestId + ",\"dboRequest\":" + dboRequest + "}}}";
    }

    @Test
    public void Should_no_response() {
        autoMock("bpmn/midl/amlMidlRequestClient.bpmn");

        Request request = new Request();
        Case caseData = new Case();

        Client client = new Client();
        client.setId(4L);

        CaseClient caseClient = new CaseClient();
        caseClient.setCaseId(caseData);
        caseClient.setClientId(client);

        caseData.setCaseClientList(List.of(caseClient));

        caseData.setId(123L);
        request.setId(125L);

        BaseDictionary bd1 = new BaseDictionary();
        BaseDictionary bd3 = new BaseDictionary();
        BaseDictionary bd7 = new BaseDictionary();

        bd1.setCode("1");
        bd3.setCode("3");
        bd7.setCode("7");

        Map<String, Object> selectResult = new HashMap<>();
        selectResult.put("caseData", caseData);
        selectResult.put("request", request);

        final FluentJavaDelegateMock selectOneDelegate = registerJavaDelegateMock("selectOneDelegate");
        selectOneDelegate.onExecutionSetVariables(selectResult);

        final BaseDictRepo baseDictionaryRepository = registerMockInstance(BaseDictRepo.class);
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCode(184, "1")).thenReturn(bd1);
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCode(185, "3")).thenReturn(bd3);
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCode(186, "7")).thenReturn(bd7);

        Map<String, Object> processParams = new HashMap<>();
        processParams.put("payload", getPayloadJson(123L, 125L, false));

        processEngineRule.manageDeployment(registerCallActivityMock("caseResponse")
                .deploy(processEngineRule)
        );

        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("amlMidlRequestClient", processParams);

        Condition<Object> isTask = new Condition<>(p -> {
            Task t = (Task)p;
            return t.getStatusId().getCode().equals("1")
                    && t.getTypeOfTask().getCode().equals("3")
                    && t.getTaskType().getCode().equals("7");
        }, "isTask");

        Condition<Object> isTimingTask = new Condition<>(p -> {
            ChangingTimingTask t = (ChangingTimingTask)p;
            return t.getIssueId() != null;
        }, "isTimingTask");

        Condition<Object> isRequest = new Condition<>(p -> {
            Request t = (Request) p;
            return t != null;
        }, "isRequest");

        Condition<Object> isTaskCases = new Condition<>(p -> {
            ZkTaskCases t = (ZkTaskCases) p;
            return t.getCaseId().getId() == caseData.getId();
        }, "isTaskCases");

        assertThat(processInstance)
                .hasPassed("Activity_selectCase", "Activity_fillTask", "Activity_saveTask",
                        "Activity_fillTiming", "Activity_saveTiming", "Activity_selectRequest", "Activity_fillRequest",
                        "Activity_saveRequest", "Activity_fillTaskCases", "Activity_saveTaskCases", "Activity_fillTiming")
                .hasNotPassed("Activity_response")
                .variables()
                .hasEntrySatisfying("timingTask", isTimingTask)
                .hasEntrySatisfying("request", isRequest)
                .hasEntrySatisfying("taskCases", isTaskCases)
                .hasEntrySatisfying("task", isTask);
    }

    @Test
    public void Should_response() {
        autoMock("bpmn/midl/amlMidlRequestClient.bpmn");

        Request request = new Request();
        Case caseData = new Case();

        Client client = new Client();
        client.setId(4L);

        CaseClient caseClient = new CaseClient();
        caseClient.setCaseId(caseData);
        caseClient.setClientId(client);

        caseData.setCaseClientList(List.of(caseClient));

        caseData.setId(123L);
        request.setId(125L);

        BaseDictionary bd1 = new BaseDictionary();
        BaseDictionary bd3 = new BaseDictionary();
        BaseDictionary bd8 = new BaseDictionary();

        bd1.setCode("1");
        bd3.setCode("3");
        bd8.setCode("8");

        Map<String, Object> selectResult = new HashMap<>();
        selectResult.put("caseData", caseData);
        selectResult.put("request", request);

        final FluentJavaDelegateMock selectOneDelegate = registerJavaDelegateMock("selectOneDelegate");
        selectOneDelegate.onExecutionSetVariables(selectResult);

        final BaseDictRepo baseDictionaryRepository = registerMockInstance(BaseDictRepo.class);
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCode(184, "1")).thenReturn(bd1);
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCode(185, "3")).thenReturn(bd3);
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCode(186, "8")).thenReturn(bd8);

        Map<String, Object> processParams = new HashMap<>();
        processParams.put("payload", getPayloadJson(123L, 125L, true));

        processEngineRule.manageDeployment(registerCallActivityMock("caseResponse")
                .deploy(processEngineRule)
        );

        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("amlMidlRequestClient", processParams);

        Condition<Object> isTask = new Condition<>(p -> {
            Task t = (Task)p;
            return t.getStatusId().getCode().equals("1")
                    && t.getTypeOfTask().getCode().equals("3")
                    && t.getTaskType().getCode().equals("8");
        }, "isTask");

        Condition<Object> isTimingTask = new Condition<>(p -> {
            ChangingTimingTask t = (ChangingTimingTask)p;
            return t.getIssueId() != null;
        }, "isTimingTask");

        Condition<Object> isRequest = new Condition<>(p -> {
            Request t = (Request) p;
            return t != null;
        }, "isRequest");

        Condition<Object> isTaskCases = new Condition<>(p -> {
            ZkTaskCases t = (ZkTaskCases) p;
            return t.getCaseId().getId() == caseData.getId();
        }, "isTaskCases");

        assertThat(processInstance)
                .hasPassed("Activity_selectCase", "Activity_fillTask", "Activity_saveTask",
                        "Activity_fillTiming", "Activity_saveTiming", "Activity_selectRequest", "Activity_fillRequest",
                        "Activity_saveRequest", "Activity_fillTaskCases", "Activity_saveTaskCases", "Activity_fillTiming",
                        "Activity_response")
                .variables()
                .hasEntrySatisfying("timingTask", isTimingTask)
                .hasEntrySatisfying("request", isRequest)
                .hasEntrySatisfying("taskCases", isTaskCases)
                .hasEntrySatisfying("task", isTask);
    }
}
