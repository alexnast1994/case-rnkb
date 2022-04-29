package com.cognive.projects.casernkb.bpm.response;

import com.cognive.projects.casernkb.repo.BaseDictRepo;
import com.prime.db.rnkb.model.BaseDictionary;
import com.prime.db.rnkb.model.Case;
import com.prime.db.rnkb.model.Payment;
import com.prime.db.rnkb.model.commucation.midl.Task;
import org.assertj.core.api.Condition;
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
import static org.camunda.bpm.extension.mockito.CamundaMockito.registerJavaDelegateMock;
import static org.camunda.bpm.extension.mockito.CamundaMockito.registerMockInstance;
import static org.camunda.bpm.extension.mockito.DelegateExpressions.autoMock;
import static org.mockito.Mockito.when;

@Deployment(resources = {
        "bpmn/response/reviewPp.bpmn"
})
public class ResponseReviewPpTest {
    @Rule
    public ProcessEngineRule processEngineRule = new ProcessEngineRule();

    @Test
    public void Should_save() {
        autoMock("bpmn/response/reviewPp.bpmn");

        Case caseData = new Case();

        BaseDictionary bd2 = new BaseDictionary();
        BaseDictionary bd1 = new BaseDictionary();
        BaseDictionary bd3 = new BaseDictionary();
        BaseDictionary bd5 = new BaseDictionary();
        BaseDictionary bd9 = new BaseDictionary();

        bd1.setCode("1");
        bd2.setCode("2");
        bd3.setCode("3");
        bd5.setCode("5");
        bd9.setCode("9");

        Map<String, Object> selectResult = new HashMap<>();
        selectResult.put("caseData", caseData);

        final FluentJavaDelegateMock selectOneDelegate = registerJavaDelegateMock("selectOneDelegate");
        selectOneDelegate.onExecutionSetVariables(selectResult);

        final BaseDictRepo baseDictionaryRepository = registerMockInstance(BaseDictRepo.class);
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCode(184, "1")).thenReturn(bd1);
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCode(178, "2")).thenReturn(bd2);
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCode(185, "3")).thenReturn(bd3);
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCode(131, "5")).thenReturn(bd5);
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCode(186, "9")).thenReturn(bd9);

        Map<String, Object> processParams = new HashMap<>();
        processParams.put("caseId", 123);

        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("reviewPp", processParams);

        Condition<Object> isCase = new Condition<>(p -> {
            Case c = (Case)p;
            return c.getStatus().getCode().equals("5")
                    && c.getCaseStatus().getCode().equals("2");
        }, "isNull");
        Condition<Object> isTask = new Condition<>(p -> {
            Task t = (Task)p;
            return t.getStatusId().getCode().equals("1")
                    && t.getTypeOfTask().getCode().equals("3")
                    && t.getTaskType().getCode().equals("9");
        }, "isNull");
        assertThat(processInstance)
                .hasPassed("Activity_saveCase", "Activity_saveTask")
                .variables()
                .hasEntrySatisfying("caseData", isCase)
                .hasEntrySatisfying("task", isTask);
    }
}
