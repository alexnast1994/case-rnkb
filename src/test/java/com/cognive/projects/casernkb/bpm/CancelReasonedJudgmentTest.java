package com.cognive.projects.casernkb.bpm;

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
        "bpmn/cancelReasonedJudgment.bpmn"
})
public class CancelReasonedJudgmentTest {
    @Rule
    public ProcessEngineRule processEngineRule = new ProcessEngineRule();

    @Test
    @SneakyThrows
    public void Should_work() {
        autoMock("bpmn/cancelReasonedJudgment.bpmn");

        BaseDictionary bd2 = new BaseDictionary();
        BaseDictionary bd22 = new BaseDictionary();
        BaseDictionary bd3 = new BaseDictionary();
        BaseDictionary bd4 = new BaseDictionary();

        bd2.setCode("2");
        bd2.setId(1L);
        bd22.setCode("2");
        bd22.setId(2L);
        bd3.setCode("3");
        bd4.setCode("4");

        BaseDictionary caseType1 = new BaseDictionary();
        BaseDictionary caseStatus2 = new BaseDictionary();
        caseType1.setCode("1");
        caseStatus2.setCode("2");

//        Если тип кейса Онлайн контроль, то есть Case.CASETYPE = Code 3 Значение из справочника 18 Тип кейса , то новый статус должен быть Case.STATUS = Code 2 131 Статус кейса Онлайн контроль
//
//        Если тип кейса Постконтроль СО, то есть Case.CASETYPE = Code 4  Значение из справочника 18 Тип кейса , то новый статус должен быть Case.STATUS = Code 2 140 Статус кейса Постконтроль СО

        Case case1 = new Case();
        Case case2 = new Case();

        case1.setCaseType(bd3);
        case2.setCaseType(bd4);

        CaseReasonedJudgment caseReasonedJudgment1 = new CaseReasonedJudgment();
        CaseReasonedJudgment caseReasonedJudgment2 = new CaseReasonedJudgment();

        caseReasonedJudgment1.setCaseId(case1);
        caseReasonedJudgment2.setCaseId(case2);

        ReasonedJudgment rj = new ReasonedJudgment();
        Client client = new Client();
        client.setId(123L);

        rj.setClientId(client);
        rj.setCaseReasonedJudgmentsList(Arrays.asList(caseReasonedJudgment1, caseReasonedJudgment2));

        Map<String, Object> selectResult = new HashMap<>();
        selectResult.put("reasonedJudgment", rj);

        final FluentJavaDelegateMock selectOneDelegate = registerJavaDelegateMock("selectOneDelegate");
        selectOneDelegate.onExecutionSetVariables(selectResult);

        final BaseDictRepo baseDictionaryRepository = registerMockInstance(BaseDictRepo.class);
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCode(131, "2")).thenReturn(bd2);
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCode(140, "2")).thenReturn(bd22);

        Map<String, Object> processParams = new HashMap<>();
        processParams.put("reasonedJudgmentId", 4L);

        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("cancelReasonedJudgment", processParams);

        Condition<Object> isCases = new Condition<>(p -> {
            List<Case> checkCase = (List<Case>)p;
            return checkCase.size() == 2 &&
                    checkCase.get(0).getStatus().getCode().equals("2") &&
                    checkCase.get(0).getStatus().getId().equals(1L) &&
                    checkCase.get(1).getStatus().getCode().equals("2") &&
                    checkCase.get(1).getStatus().getId().equals(2L);
        }, "isCases");

        assertThat(processInstance)
                .hasPassed("Activity_changeStatus", "Activity_saveCases", "Activity_cleanClientTriggerCheck", "Activity_cleanClientTrigger", "Event_end")
                .variables()
                .hasEntrySatisfying("cases", isCases)
                .containsEntry("clientId", 123L)
        ;

    }
}
