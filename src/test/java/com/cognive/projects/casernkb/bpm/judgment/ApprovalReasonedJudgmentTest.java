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
        "bpmn/judgment/approvalReasonedJudgment.bpmn"
})
public class ApprovalReasonedJudgmentTest {
    @Rule
    public ProcessEngineRule processEngineRule = new ProcessEngineRule();

    private static final BaseDictionary baseDictionary2;
    private static final BaseDictionary baseDictionary4;
    private static final BaseDictionary baseDictionary6;
    private static final BaseDictionary baseDictionary7;

    static {
        baseDictionary2 = new BaseDictionary();
        baseDictionary4 = new BaseDictionary();
        baseDictionary6 = new BaseDictionary();
        baseDictionary7 = new BaseDictionary();

        baseDictionary2.setCode("2");
        baseDictionary4.setCode("4");
        baseDictionary6.setCode("6");
        baseDictionary7.setCode("7");
    }

    private ReasonedJudgment getRj() {
        BaseDictionary bd3 = new BaseDictionary();
        BaseDictionary bd4 = new BaseDictionary();

        BaseDictionary bdCaseType3 = new BaseDictionary();
        BaseDictionary bdCaseType4 = new BaseDictionary();

        bdCaseType3.setCode("3");
        bdCaseType4.setCode("4");

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

        caseUser11.setStatus(baseDictionary2);
        caseUser11.setDecisionDate(LocalDateTime.of(LocalDate.of(2022, 10, 1), LocalTime.of(10, 10, 20)));
        caseUser11.setResponsible(user1);

        caseUser12.setStatus(baseDictionary2);
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

        return rj;
    }

    private BaseDictionary getBaseDictionary(String code) {
        BaseDictionary bd = new BaseDictionary();
        bd.setId(123L);
        bd.setCode(code);
        return bd;
    }

    @Test
    @SneakyThrows
    public void Should_type_02_control_1() {
        autoMock("bpmn/judgment/approvalReasonedJudgment.bpmn");

        ReasonedJudgment rj = getRj();

        BaseDictionary bd1 = new BaseDictionary();

        rj.setTypeRj(getBaseDictionary("02"));
        rj.setTypeOfControl(getBaseDictionary("1"));

        Map<String, Object> selectResult = new HashMap<>();
        selectResult.put("reasonedJudgment", rj);

        final FluentJavaDelegateMock selectOneDelegate = registerJavaDelegateMock("selectOneDelegate");
        selectOneDelegate.onExecutionSetVariables(selectResult);

        final BaseDictRepo baseDictionaryRepository = registerMockInstance(BaseDictRepo.class);
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCode(131, "7")).thenReturn(baseDictionary7);
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCode(178, "4")).thenReturn(baseDictionary4);

        Map<String, Object> processParams = new HashMap<>();
        processParams.put("reasonedJudgmentId", 4L);

        processEngineRule.manageDeployment(registerCallActivityMock("judgmentCleanTrigger")
                .deploy(processEngineRule)
        );

        processEngineRule.manageDeployment(registerCallActivityMock("createOes4077")
                .deploy(processEngineRule)
        );

        processEngineRule.manageDeployment(registerCallActivityMock("calculationRiskRating")
                .deploy(processEngineRule)
        );

        processEngineRule.manageDeployment(registerCallActivityMock("rejectionPaymentReasonedJudgment")
                .deploy(processEngineRule)
        );

        processEngineRule.manageDeployment(registerCallActivityMock("caseResponse")
                .deploy(processEngineRule)
        );

        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("approvalReasonedJudgment", processParams);

        Condition<Object> isCases = new Condition<>(p -> {
            List<Case> checkCase = (List<Case>)p;
            return checkCase.size() == 2 &&
                    checkCase.get(0).getStatus().getCode().equals("7") &&
                    checkCase.get(0).getCaseStatus().getCode().equals("4") &&
                    checkCase.get(1).getStatus().getCode().equals("7") &&
                    checkCase.get(1).getCaseStatus().getCode().equals("4");
        }, "isCases");

        Condition<Object> isCaseUsers = new Condition<>(p -> {
            List<CaseUser> checkCase = (List<CaseUser>)p;
            return checkCase.size() == 2 &&
                    checkCase.get(0).getStatus().getCode().equals("7") &&
                    checkCase.get(1).getStatus().getCode().equals("7")
            ;
        }, "isCases");

        assertThat(processInstance)
                .hasPassed("Activity_changeStatus", "Activity_saveCases", "Activity_saveCaseUsers",
                        "Activity_cleanTriggers", "Activity_processOes4077", "Activity_calculationRiskRating",
                        "Activity_loopCase", "Activity_process13", "Activity_process3", "Event_end")
                .variables()
                .hasEntrySatisfying("cases", isCases)
                .hasEntrySatisfying("caseUsers", isCaseUsers)
                .containsEntry("clientId", 123L)
        ;
    }

    @Test
    @SneakyThrows
    public void Should_type_02_control_2() {
        autoMock("bpmn/judgment/approvalReasonedJudgment.bpmn");

        ReasonedJudgment rj = getRj();

        BaseDictionary bd1 = new BaseDictionary();

        rj.setTypeRj(getBaseDictionary("02"));
        rj.setTypeOfControl(getBaseDictionary("2"));

        Map<String, Object> selectResult = new HashMap<>();
        selectResult.put("reasonedJudgment", rj);

        final FluentJavaDelegateMock selectOneDelegate = registerJavaDelegateMock("selectOneDelegate");
        selectOneDelegate.onExecutionSetVariables(selectResult);

        final BaseDictRepo baseDictionaryRepository = registerMockInstance(BaseDictRepo.class);
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCode(140, "6")).thenReturn(baseDictionary6);
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCode(179, "4")).thenReturn(baseDictionary4);

        Map<String, Object> processParams = new HashMap<>();
        processParams.put("reasonedJudgmentId", 4L);

        processEngineRule.manageDeployment(registerCallActivityMock("judgmentCleanTrigger")
                .deploy(processEngineRule)
        );

        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("approvalReasonedJudgment", processParams);

        Condition<Object> isCases = new Condition<>(p -> {
            List<Case> checkCase = (List<Case>)p;
            return checkCase.size() == 2 &&
                    checkCase.get(0).getStatus().getCode().equals("6") &&
                    checkCase.get(0).getCaseStatus().getCode().equals("4") &&
                    checkCase.get(1).getStatus().getCode().equals("6") &&
                    checkCase.get(1).getCaseStatus().getCode().equals("4");
        }, "isCases");

        Condition<Object> isCaseUsers = new Condition<>(p -> {
            List<CaseUser> checkCase = (List<CaseUser>)p;
            return checkCase.size() == 2 &&
                    checkCase.get(0).getStatus().getCode().equals("6") &&
                    checkCase.get(1).getStatus().getCode().equals("6")
                    ;
        }, "isCases");

        assertThat(processInstance)
                .hasPassed("Activity_changeStatus", "Activity_saveCases", "Activity_saveCaseUsers",
                        "Activity_cleanTriggers", "Event_end", "Event_endNoProcess")
                .variables()
                .hasEntrySatisfying("cases", isCases)
                .hasEntrySatisfying("caseUsers", isCaseUsers)
                .containsEntry("clientId", 123L)
        ;
    }

    @Test
    @SneakyThrows
    public void Should_type_01_control_2() {
        autoMock("bpmn/judgment/approvalReasonedJudgment.bpmn");

        ReasonedJudgment rj = getRj();

        BaseDictionary bd1 = new BaseDictionary();

        rj.setTypeRj(getBaseDictionary("01"));
        rj.setTypeOfControl(getBaseDictionary("2"));

        Map<String, Object> selectResult = new HashMap<>();
        selectResult.put("reasonedJudgment", rj);

        final FluentJavaDelegateMock selectOneDelegate = registerJavaDelegateMock("selectOneDelegate");
        selectOneDelegate.onExecutionSetVariables(selectResult);

        final BaseDictRepo baseDictionaryRepository = registerMockInstance(BaseDictRepo.class);
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCode(140, "7")).thenReturn(baseDictionary7);
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCode(179, "4")).thenReturn(baseDictionary4);

        Map<String, Object> processParams = new HashMap<>();
        processParams.put("reasonedJudgmentId", 4L);

        processEngineRule.manageDeployment(registerCallActivityMock("judgmentCleanTrigger")
                .deploy(processEngineRule)
        );

        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("approvalReasonedJudgment", processParams);

        Condition<Object> isCases = new Condition<>(p -> {
            List<Case> checkCase = (List<Case>)p;
            return checkCase.size() == 2 &&
                    checkCase.get(0).getStatus().getCode().equals("7") &&
                    checkCase.get(0).getCaseStatus().getCode().equals("4") &&
                    checkCase.get(1).getStatus().getCode().equals("7") &&
                    checkCase.get(1).getCaseStatus().getCode().equals("4");
        }, "isCases");

        Condition<Object> isCaseUsers = new Condition<>(p -> {
            List<CaseUser> checkCase = (List<CaseUser>)p;
            return checkCase.size() == 2 &&
                    checkCase.get(0).getStatus().getCode().equals("7") &&
                    checkCase.get(1).getStatus().getCode().equals("7")
                    ;
        }, "isCases");

        assertThat(processInstance)
                .hasPassed("Activity_changeStatus", "Activity_saveCases", "Activity_saveCaseUsers",
                        "Activity_cleanTriggers", "Event_end", "Event_endNoProcess")
                .variables()
                .hasEntrySatisfying("cases", isCases)
                .hasEntrySatisfying("caseUsers", isCaseUsers)
                .containsEntry("clientId", 123L)
        ;
    }
}
