package com.cognive.projects.casernkb.bpm.judgment;

import com.cognive.projects.casernkb.repo.BaseDictRepo;
import com.prime.db.rnkb.model.BaseDictionary;
import com.prime.db.rnkb.model.Case;
import com.prime.db.rnkb.model.Client;
import com.prime.db.rnkb.model.commucation.judgment.CaseReasonedJudgment;
import com.prime.db.rnkb.model.commucation.judgment.ReasonedJudgment;
import lombok.SneakyThrows;
import org.assertj.core.api.Condition;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.extension.mockito.mock.FluentJavaDelegateMock;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.assertThat;
import static org.camunda.bpm.extension.mockito.CamundaMockito.registerJavaDelegateMock;
import static org.camunda.bpm.extension.mockito.CamundaMockito.registerMockInstance;
import static org.camunda.bpm.extension.mockito.DelegateExpressions.autoMock;
import static org.mockito.Mockito.when;

@Deployment(resources = {
        "bpmn/judgment/statusReasonedJudgment.bpmn"
})
public class StatusReasonedJudgmentTest {
    @Rule
    public ProcessEngineRule processEngineRule = new ProcessEngineRule();

    @Test
    @SneakyThrows
    public void Should_work() {
        autoMock("bpmn/judgment/statusReasonedJudgment.bpmn");

        ReasonedJudgment rj = new ReasonedJudgment();

        BaseDictionary bd1 = new BaseDictionary();
        BaseDictionary bd2 = new BaseDictionary();
        BaseDictionary bd3 = new BaseDictionary();

        bd1.setCode("1");
        bd2.setCode("2");
        bd3.setCode("3");

        rj.setStatus(bd3);
        rj.setTypeRj(bd2);

//        Если REASONEDJUDGMENT.TYPERJ = Code 03 "Профиль"  Справочник № 277 Тип мотивированного суждения  ,то REASONEDJUDGMENT.STATUS = Code 03 "Утверждено" Справочник № 213 Статус мотивированного суждения
//        Если REASONEDJUDGMENT.TYPERJ =  Code 01 "Положительное" или Code 02 "Отрицательное" Справочник № 277 Тип мотивированного суждения  ,то REASONEDJUDGMENT.STATUS =  Code 01 "На рассмотрении" Справочник № 213 Статус мотивированного суждения

        Map<String, Object> selectResult = new HashMap<>();
        selectResult.put("reasonedJudgment", rj);

        final FluentJavaDelegateMock selectOneDelegate = registerJavaDelegateMock("selectOneDelegate");
        selectOneDelegate.onExecutionSetVariables(selectResult);

        final BaseDictRepo baseDictionaryRepository = registerMockInstance(BaseDictRepo.class);
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCode(213, "1")).thenReturn(bd1);

        Map<String, Object> processParams = new HashMap<>();
        processParams.put("reasonedJudgmentId", 4L);

        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("statusReasonedJudgment", processParams);

        Condition<Object> isJudgment = new Condition<>(p -> {
            ReasonedJudgment rj2 = (ReasonedJudgment)p;
            return rj2.getStatus().getCode().equals("1");
        }, "isJudgment");

        assertThat(processInstance)
                .hasPassed("Activity_changeStatus", "Activity_saveReasonedJudgment", "Event_end")
                .variables()
                .hasEntrySatisfying("reasonedJudgment", isJudgment)
        ;

    }
}
