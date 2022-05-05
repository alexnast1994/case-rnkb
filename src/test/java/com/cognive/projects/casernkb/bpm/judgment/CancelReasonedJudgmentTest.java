package com.cognive.projects.casernkb.bpm.judgment;

import com.cognive.projects.casernkb.repo.BaseDictRepo;
import com.prime.db.rnkb.model.*;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.assertThat;
import static org.camunda.bpm.extension.mockito.CamundaMockito.*;
import static org.camunda.bpm.extension.mockito.DelegateExpressions.autoMock;
import static org.mockito.Mockito.when;

@Deployment(resources = {
        "bpmn/judgment/cancelReasonedJudgment.bpmn"
})
public class CancelReasonedJudgmentTest {
    @Rule
    public ProcessEngineRule processEngineRule = new ProcessEngineRule();

    @Test
    @SneakyThrows
    public void Should_work() {
        autoMock("bpmn/judgment/cancelReasonedJudgment.bpmn");

        BaseDictionary bd2 = new BaseDictionary();
        BaseDictionary bd22 = new BaseDictionary();
        BaseDictionary bd3 = new BaseDictionary();
        BaseDictionary bd4 = new BaseDictionary();

        BaseDictionary bdCaseType3 = new BaseDictionary();
        BaseDictionary bdCaseType4 = new BaseDictionary();

        bdCaseType3.setCode("3");
        bdCaseType4.setCode("4");

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

        SysUser user1 = new SysUser();
        SysUser user2 = new SysUser();

        user1.setId(1L);
        user1.setName("user1");

        user2.setId(1L);
        user2.setName("user1");

        CaseUser caseUser11 = new CaseUser();
        CaseUser caseUser12 = new CaseUser();

        caseUser11.setStatus(bd2);
        caseUser11.setDecisionDate(LocalDateTime.of(LocalDate.of(2022, 10, 1), LocalTime.of(10, 10, 20)));
        caseUser11.setResponsible(user1);

        caseUser12.setStatus(bd2);
        caseUser12.setDecisionDate(LocalDateTime.of(LocalDate.of(2022, 10, 1), LocalTime.of(14, 10, 20)));
        caseUser12.setResponsible(user2);

        Case case1 = new Case();
        Case case2 = new Case();

        case1.setCaseType(bdCaseType3);
        case1.setCaseUserList(Arrays.asList(caseUser11, caseUser12));

        case2.setCaseType(bdCaseType4);

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
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCode(178, "2")).thenReturn(bd2);
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCode(179, "2")).thenReturn(bd22);

        Map<String, Object> processParams = new HashMap<>();
        processParams.put("reasonedJudgmentId", 4L);

        processEngineRule.manageDeployment(registerCallActivityMock("judgmentCleanTrigger")
                .deploy(processEngineRule)
        );

        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("cancelReasonedJudgment", processParams);

        Condition<Object> isCases = new Condition<>(p -> {
            List<Case> checkCase = (List<Case>)p;
            return checkCase.size() == 2 &&
                    checkCase.get(0).getCaseType().getCode().equals("3") &&
                    checkCase.get(0).getStatus().getCode().equals("2") &&
                    checkCase.get(0).getCaseStatus().getCode().equals("2") &&
                    checkCase.get(1).getCaseType().getCode().equals("4") &&
                    checkCase.get(1).getStatus().getCode().equals("2") &&
                    checkCase.get(1).getCaseStatus().getCode().equals("2");
        }, "isCases");

        Condition<Object> isCaseUsers = new Condition<>(p -> {
            List<CaseUser> checkCase = (List<CaseUser>)p;
            return checkCase.size() == 2 &&
                    checkCase.get(0).getStatus().getCode().equals("2") &&
                    checkCase.get(0).getResponsible().getName().equals(user2.getName()) &&
                    checkCase.get(1).getStatus().getCode().equals("2") &&
                    checkCase.get(1).getResponsible() == null
            ;
        }, "isCases");

        assertThat(processInstance)
                .hasPassed("Activity_changeStatus", "Activity_saveCases", "Activity_saveCaseUsers", "Activity_cleanTriggers", "Event_end")
                .variables()
                .hasEntrySatisfying("cases", isCases)
                .hasEntrySatisfying("caseUsers", isCaseUsers)
                .containsEntry("clientId", 123L)
        ;

    }
}
