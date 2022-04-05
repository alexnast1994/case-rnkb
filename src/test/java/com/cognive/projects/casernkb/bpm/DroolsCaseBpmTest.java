package com.cognive.projects.casernkb.bpm;

import com.cognive.projects.casernkb.repo.BaseDictRepo;
import com.prime.db.rnkb.model.*;
import lombok.SneakyThrows;
import org.assertj.core.api.Condition;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.extension.mockito.mock.FluentJavaDelegateMock;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.Disabled;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.assertThat;
import static org.camunda.bpm.extension.mockito.CamundaMockito.*;
import static org.camunda.bpm.extension.mockito.DelegateExpressions.autoMock;
import static org.mockito.Mockito.when;

@Deployment(resources = {
        "bpmn/cases/droolsCase.bpmn"
})
public class DroolsCaseBpmTest {
    @Rule
    public ProcessEngineRule processEngineRule = new ProcessEngineRule();

    private static final String payloadJsonAccepted = "{\"rules\":[\"01\",\"74\"],\"paymentId\":123}";
    private static final String payloadJsonAccepted2 = "{\"rules\":[\"01\",\"74\", \"75\"],\"paymentId\":123}";
    private static final String payloadJsonNoRules = "{\"rules\":[],\"paymentId\":1}";

    @Test
    @SneakyThrows
    public void Should_nothing() {
        autoMock("bpmn/cases/droolsCase.bpmn");

        Payment payment = new Payment();

        Map<String, Object> selectResult = new HashMap<>();
        selectResult.put("payment", payment);

        final FluentJavaDelegateMock selectOneDelegate = registerJavaDelegateMock("selectOneDelegate");
        selectOneDelegate.onExecutionSetVariables(selectResult);

        processEngineRule.manageDeployment(registerCallActivityMock("caseCreate")
                .onExecutionAddVariable("test", "test")
                .deploy(processEngineRule)
        );

        Map<String, Object> processParams = new HashMap<>();
        processParams.put("payload", payloadJsonNoRules);

        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("droolsCase", processParams);

        assertThat(processInstance)
                .hasPassed("Event_noRules");
    }

    @Test
    @SneakyThrows
    public void Should_createCase() {
        autoMock("bpmn/cases/droolsCase.bpmn");

        Payment payment = new Payment();

        Map<String, Object> selectResult = new HashMap<>();
        selectResult.put("payment", payment);

        final FluentJavaDelegateMock selectOneDelegate = registerJavaDelegateMock("selectOneDelegate");
        selectOneDelegate.onExecutionSetVariables(selectResult);

        processEngineRule.manageDeployment(registerCallActivityMock("caseCreate")
                .deploy(processEngineRule)
        );

        Map<String, Object> processParams = new HashMap<>();
        processParams.put("payload", payloadJsonAccepted);

        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("droolsCase", processParams);

        assertThat(processInstance)
                .hasPassed("Event_createCase", "Activity_callCreateCase");
    }

    @Test
    @SneakyThrows
    public void Should_check_codes() {
        autoMock("bpmn/cases/droolsCase.bpmn");

        BaseDictionary crCode1 = new BaseDictionary();
        BaseDictionary crCode2 = new BaseDictionary();
        BaseDictionary caseStatus1 = new BaseDictionary();
        BaseDictionary caseType1 = new BaseDictionary();

        CaseRules cr1 = new CaseRules();
        CaseRules cr2 = new CaseRules();

        crCode1.setCode("01");
        crCode2.setCode("74");
        caseStatus1.setCode("1");
        caseType1.setCode("1");

        cr1.setCode(crCode1);
        cr2.setCode(crCode2);

        Case caseData = new Case();
        caseData.setCaseRules(Arrays.asList(cr1, cr2));
        caseData.setStatus(caseStatus1);
        caseData.setCaseType(caseType1);

        CaseOperation caseOperation = new CaseOperation();
        caseOperation.setCaseId(caseData);

        Payment payment = new Payment();
        payment.setCaseOperationList(Collections.singletonList(caseOperation));

        Map<String, Object> selectResult = new HashMap<>();
        selectResult.put("payment", payment);

        final FluentJavaDelegateMock selectOneDelegate = registerJavaDelegateMock("selectOneDelegate");
        selectOneDelegate.onExecutionSetVariables(selectResult);

        processEngineRule.manageDeployment(registerCallActivityMock("caseCreate")
            .deploy(processEngineRule)
        );

        Map<String, Object> processParams = new HashMap<>();
        processParams.put("payload", payloadJsonAccepted);

        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("droolsCase", processParams);

        assertThat(processInstance)
                .hasPassed("Activity_checkCodes", "Event_noNewCodes");
    }


    @Test
    public void Should_save_new_rules() {
        autoMock("bpmn/cases/droolsCase.bpmn");

        BaseDictionary crCode1 = new BaseDictionary();
        BaseDictionary crCode74 = new BaseDictionary();
        BaseDictionary caseStatus1 = new BaseDictionary();
        BaseDictionary caseType1 = new BaseDictionary();

        CaseRules cr1 = new CaseRules();

        crCode1.setCode("01");
        crCode74.setCode("74");
        caseStatus1.setCode("1");
        caseType1.setCode("1");

        cr1.setRuleId(crCode1);

        Case caseData = new Case();
        caseData.setCaseRules(Collections.singletonList(cr1));
        caseData.setStatus(caseStatus1);
        caseData.setCaseType(caseType1);

        CaseOperation caseOperation = new CaseOperation();
        caseOperation.setCaseId(caseData);

        Payment payment = new Payment();
        payment.setCaseOperationList(Collections.singletonList(caseOperation));

        Map<String, Object> selectResult = new HashMap<>();
        selectResult.put("payment", payment);

        final FluentJavaDelegateMock selectOneDelegate = registerJavaDelegateMock("selectOneDelegate");
        selectOneDelegate.onExecutionSetVariables(selectResult);

        processEngineRule.manageDeployment(registerCallActivityMock("caseCreate")
                .deploy(processEngineRule)
        );

        final BaseDictRepo baseDictionaryRepository = registerMockInstance(BaseDictRepo.class);
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCode(18, "74")).thenReturn(crCode74);

        Map<String, Object> processParams = new HashMap<>();
        processParams.put("payload", payloadJsonAccepted);

        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("droolsCase", processParams);

        Condition<Object> isCodes = new Condition<>(p -> {
                Case checkCase = (Case)p;
                return checkCase.getCaseRules().size() == 2 &&
                        checkCase.getCaseRules().get(1).getRuleId().getCode().equals("74");
            }, "isCaseRules size 2");

        assertThat(processInstance)
                .hasPassed("Activity_checkCodes", "Activity_saveRules")
                .variables()
                .hasEntrySatisfying("caseData", isCodes);
    }

    @Test
    public void Should_save_comment() {
        autoMock("bpmn/cases/droolsCase.bpmn");

        BaseDictionary crCode1 = new BaseDictionary();
        BaseDictionary crCode74 = new BaseDictionary();
        BaseDictionary crCode75 = new BaseDictionary();
        BaseDictionary caseStatus4 = new BaseDictionary();
        BaseDictionary caseType1 = new BaseDictionary();

        CaseRules cr1 = new CaseRules();

        crCode1.setCode("01");
        crCode74.setCode("74");
        crCode75.setCode("75");
        caseStatus4.setCode("4");
        caseType1.setCode("1");

        cr1.setRuleId(crCode1);

        Case caseData = new Case();
        caseData.setCaseRules(Collections.singletonList(cr1));
        caseData.setStatus(caseStatus4);
        caseData.setCaseType(caseType1);

        CaseOperation caseOperation = new CaseOperation();
        caseOperation.setCaseId(caseData);

        Payment payment = new Payment();
        payment.setCaseOperationList(Collections.singletonList(caseOperation));

        Map<String, Object> selectResult = new HashMap<>();
        selectResult.put("payment", payment);

        final FluentJavaDelegateMock selectOneDelegate = registerJavaDelegateMock("selectOneDelegate");
        selectOneDelegate.onExecutionSetVariables(selectResult);

        processEngineRule.manageDeployment(registerCallActivityMock("caseCreate")
                .deploy(processEngineRule)
        );

        final BaseDictRepo baseDictionaryRepository = registerMockInstance(BaseDictRepo.class);
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCode(18, "74")).thenReturn(crCode74);
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCode(18, "75")).thenReturn(crCode75);

        Map<String, Object> processParams = new HashMap<>();
        processParams.put("payload", payloadJsonAccepted2);

        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("droolsCase", processParams);

        Condition<Object> isComment = new Condition<>(p -> {
            Case checkCase = (Case)p;
            return checkCase.getCaseCommentList().size() == 1 &&
                    checkCase.getCaseCommentList().get(0).getComment().equals("74;75");
        }, "isCaseRules size 2");

        assertThat(processInstance)
                .hasPassed("Activity_checkCodes", "Activity_saveComment")
                .variables()
                .hasEntrySatisfying("caseData", isComment);
    }
}
