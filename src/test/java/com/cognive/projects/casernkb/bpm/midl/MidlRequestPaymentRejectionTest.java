package com.cognive.projects.casernkb.bpm.midl;

import com.cognive.projects.casernkb.repo.BaseDictRepo;
import com.prime.db.rnkb.model.BaseDictionary;
import com.prime.db.rnkb.model.Case;
import com.prime.db.rnkb.model.CaseClient;
import com.prime.db.rnkb.model.Client;
import com.prime.db.rnkb.model.commucation.midl.ChangingTimingTask;
import com.prime.db.rnkb.model.commucation.midl.Task;
import com.prime.db.rnkb.model.commucation.midl.ZkTaskCases;
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
import static org.camunda.bpm.extension.mockito.CamundaMockito.registerJavaDelegateMock;
import static org.camunda.bpm.extension.mockito.CamundaMockito.registerMockInstance;
import static org.camunda.bpm.extension.mockito.DelegateExpressions.autoMock;
import static org.mockito.Mockito.when;

@Deployment(resources = {
        "bpmn/midl/midlRequestPaymentRejection.bpmn"
})
public class MidlRequestPaymentRejectionTest {
    @Rule
    public ProcessEngineRule processEngineRule = new ProcessEngineRule();

    @Test
    public void Should_save() {
        autoMock("bpmn/midl/midlRequestPaymentRejection.bpmn");

        Case caseData = new Case();

        Client client = new Client();
        client.setId(4L);

        CaseClient caseClient = new CaseClient();
        caseClient.setCaseId(caseData);
        caseClient.setClientId(client);

        caseData.setCaseClientList(List.of(caseClient));

        caseData.setId(123L);

        BaseDictionary bd1 = new BaseDictionary();
        BaseDictionary bd3 = new BaseDictionary();
        BaseDictionary bd9 = new BaseDictionary();

        bd1.setCode("1");
        bd3.setCode("3");
        bd9.setCode("9");

        Map<String, Object> selectResult = new HashMap<>();
        selectResult.put("caseData", caseData);

        final FluentJavaDelegateMock selectOneDelegate = registerJavaDelegateMock("selectOneDelegate");
        selectOneDelegate.onExecutionSetVariables(selectResult);

        final BaseDictRepo baseDictionaryRepository = registerMockInstance(BaseDictRepo.class);
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCode(184, "1")).thenReturn(bd1);
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCode(185, "3")).thenReturn(bd3);
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCode(186, "9")).thenReturn(bd9);

        Map<String, Object> processParams = new HashMap<>();
        processParams.put("caseId", 123L);
        processParams.put("requestId", 125L);

        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("midlRequestPaymentRejection", processParams);

        Condition<Object> isTask = new Condition<>(p -> {
            Task t = (Task)p;
            return t.getStatusId().getCode().equals("1")
                    && t.getTypeOfTask().getCode().equals("3")
                    && t.getTaskType().getCode().equals("9");
        }, "isTask");

        Condition<Object> isTimingTask = new Condition<>(p -> {
            ChangingTimingTask t = (ChangingTimingTask)p;
            return t.getIssueId() != null;
        }, "isTimingTask");

        Condition<Object> isTaskCases = new Condition<>(p -> {
            ZkTaskCases t = (ZkTaskCases) p;
            return t.getCaseId().getId() == caseData.getId();
        }, "isTaskCases");

        assertThat(processInstance)
                .hasPassed("Activity_selectCase", "Activity_fillTask", "Activity_saveTask",
                        "Activity_fillTiming", "Activity_saveTiming", "Activity_fillTaskCases",
                        "Activity_saveTaskCases", "Activity_fillTiming")
                .variables()
                .hasEntrySatisfying("timingTask", isTimingTask)
                .hasEntrySatisfying("taskCases", isTaskCases)
                .hasEntrySatisfying("task", isTask);
    }
}
