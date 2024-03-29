package com.cognive.projects.casernkb.bpm.cases;

import com.cognive.projects.casernkb.bpm.TestUtils;
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

import java.util.*;
import java.util.stream.Collectors;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.assertThat;
import static org.camunda.bpm.extension.mockito.CamundaMockito.*;
import static org.camunda.bpm.extension.mockito.DelegateExpressions.autoMock;
import static org.mockito.Mockito.when;

@Deployment(resources = {
        "bpmn/cases/amlCasePostAndNecessarily.bpmn"
})
public class AmlCasePostAndNecessarilyTest {
    @Rule
    public ProcessEngineRule processEngineRule = new ProcessEngineRule();

    private String getPayloadJson(Long paymentId, String caseType, List<String> rules, String comment) {
        return getPayloadJson(paymentId, caseType, rules, comment, null);
    }

    private String getPayloadJson(Long paymentId, String caseType, List<String> rules, String comment, Long userId) {
        String rulesString = rules.stream().map(x -> "\"" + x + "\"").collect(Collectors.joining(","));
        return "{\"payload\":{\"amlCasePostAndNecessarily\":{\"paymentId\":" + paymentId + ",\"caseType\":\"" + caseType + "\",\"rules\":[" + rulesString + "]" +
                ((comment !=null) ? ",\"comment\":\"" + comment +  "\"": "") +
                "}}" +
                ((userId != null) ? ",\"userContext\" : {\"userId\": " + userId + "}" : "") + "}";
    }

    @Test
    public void Should_save_new_codes() {
        autoMock("bpmn/cases/amlCasePostAndNecessarily.bpmn");

        Case caseData = new Case();
        CaseOperation caseOperation = new CaseOperation();
        Payment payment = new Payment();

        CaseRules r1 = new CaseRules();
        r1.setCode(TestUtils.getBaseDictionary("55"));

        caseData.setCaseRules(Collections.singletonList(r1));
        caseData.setCaseType(TestUtils.getBaseDictionary("2"));
        caseData.setStatus(TestUtils.getBaseDictionary("2"));
        caseOperation.setCaseId(caseData);
        payment.setCaseOperationList(Collections.singletonList(caseOperation));

        Map<String, Object> selectResult = new HashMap<>();
        selectResult.put("payment", payment);

        final FluentJavaDelegateMock selectOneDelegate = registerJavaDelegateMock("selectOneDelegate");
        selectOneDelegate.onExecutionSetVariables(selectResult);

        final BaseDictRepo baseDictionaryRepository = registerMockInstance(BaseDictRepo.class);
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCode(75, "44")).thenReturn(TestUtils.getBaseDictionary("44"));
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCode(75, "66")).thenReturn(TestUtils.getBaseDictionary("66"));

        Map<String, Object> processParams = new HashMap<>();
        processParams.put("payload", getPayloadJson(123L, "2", List.of("44", "55", "66"), "comment"));

        processEngineRule.manageDeployment(registerCallActivityMock("caseCreate")
                .deploy(processEngineRule)
        );

        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("amlCasePostAndNecessarily", processParams);

        Condition<Object> isCase = new Condition<>(p -> {
            Case checkCase = (Case)p;
            return checkCase.getCaseType().getCode().equals("2");
        }, "isCaseRules size 2");

        Condition<Object> isRules = new Condition<>(p -> {
            List<Object> caseRules = (List<Object>)p;
            return caseRules.size() == 3 &&
                    ((CaseRules)caseRules.get(0)).getCode().getCode().equals("44") &&
                    ((CaseRules)caseRules.get(1)).getCode().getCode().equals("66") &&
                    ((CaseComment)caseRules.get(2)).getComment().equals("comment")
                    ;
        }, "isRules");

        assertThat(processInstance)
                .hasPassed("Activity_payload", "Activity_selectPayment", "Activity_checkData", "Activity_setRules", "Activity_saveRules")
                .hasNotPassed("Activity_setPaymentFlags", "Activity_savePayment", "Activity_createCase")
                .variables()
                .hasEntrySatisfying("caseData", isCase)
                .hasEntrySatisfying("caseRelationList", isRules)
        ;
    }

    @Test
    public void Should_create_case() {
        autoMock("bpmn/cases/amlCasePostAndNecessarily.bpmn");

        BaseDictionary rule44 = TestUtils.getBaseDictionary("44");
        BaseDictionary rule55 = TestUtils.getBaseDictionary("55");
        BaseDictionary rule66 = TestUtils.getBaseDictionary("66");

        Payment payment = new Payment();

        Map<String, Object> selectResult = new HashMap<>();
        selectResult.put("payment", payment);

        final FluentJavaDelegateMock selectOneDelegate = registerJavaDelegateMock("selectOneDelegate");
        selectOneDelegate.onExecutionSetVariables(selectResult);

        final BaseDictRepo baseDictionaryRepository = registerMockInstance(BaseDictRepo.class);
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCode(272, "44")).thenReturn(rule44);
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCode(272, "55")).thenReturn(rule55);
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCode(272, "66")).thenReturn(rule66);

        Map<String, Object> processParams = new HashMap<>();
        processParams.put("payload", getPayloadJson(123L, "4", List.of("44", "55", "66"), null));

        processEngineRule.manageDeployment(registerCallActivityMock("caseCreate")
                .deploy(processEngineRule)
        );

        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("amlCasePostAndNecessarily", processParams);

        Condition<Object> isCase = new Condition<>(p -> {
            Case checkCase = (Case)p;
            return checkCase == null;
        }, "isCaseRules size 2");

        Condition<Object> isRules = new Condition<>(p -> {
            List<BaseDictionary> rules = (List<BaseDictionary>)p;
            return rules.size() == 3 &&
                    rules.get(0).getCode().equals("44") &&
                    rules.get(1).getCode().equals("55") &&
                    rules.get(2).getCode().equals("66")
                    ;
        }, "isRules");

        Condition<Object> isPayment = new Condition<>(p -> {
            Payment pp  = (Payment) p;
            return pp.getCheckFlagSO();
        }, "isCaseRules size 2");

        assertThat(processInstance)
                .hasPassed("Activity_payload", "Activity_selectPayment", "Activity_checkData", "Activity_setPaymentFlags", "Activity_savePayment", "Activity_createCase")
                .hasNotPassed("Activity_setRules", "Activity_saveRules")
                .variables()
                .hasEntrySatisfying("caseData", isCase)
                .hasEntrySatisfying("acceptedRules", isRules)
                .hasEntrySatisfying("payment", isPayment)
        ;
    }

    @Test
    public void Should_use_user_context() {
        autoMock("bpmn/cases/amlCasePostAndNecessarily.bpmn");

        BaseDictionary rule44 = TestUtils.getBaseDictionary("44");
        BaseDictionary rule55 = TestUtils.getBaseDictionary("55");
        BaseDictionary rule66 = TestUtils.getBaseDictionary("66");

        Payment payment = new Payment();
        SysUser user = new SysUser();
        user.setId(1L);

        Map<String, Object> selectResult = new HashMap<>();
        selectResult.put("payment", payment);
        selectResult.put("user", user);

        final FluentJavaDelegateMock selectOneDelegate = registerJavaDelegateMock("selectOneDelegate");
        selectOneDelegate.onExecutionSetVariables(selectResult);

        final BaseDictRepo baseDictionaryRepository = registerMockInstance(BaseDictRepo.class);
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCode(272, "44")).thenReturn(rule44);
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCode(272, "55")).thenReturn(rule55);
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCode(272, "66")).thenReturn(rule66);

        Map<String, Object> processParams = new HashMap<>();
        processParams.put("payload", getPayloadJson(123L, "4", List.of("44", "55", "66"), "comment", 55L));

        processEngineRule.manageDeployment(registerCallActivityMock("caseCreate")
                .deploy(processEngineRule)
        );

        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("amlCasePostAndNecessarily", processParams);

        Condition<Object> isCase = new Condition<>(p -> {
            Case checkCase = (Case)p;
            return checkCase == null;
        }, "isCaseRules size 2");

        Condition<Object> isRules = new Condition<>(p -> {
            List<BaseDictionary> rules = (List<BaseDictionary>)p;
            return rules.size() == 3 &&
                    rules.get(0).getCode().equals("44") &&
                    rules.get(1).getCode().equals("55") &&
                    rules.get(2).getCode().equals("66")
                    ;
        }, "isRules");

        Condition<Object> isPayment = new Condition<>(p -> {
            Payment pp  = (Payment) p;
            return pp.getCheckFlagSO();
        }, "isCaseRules size 2");

        Condition<Object> isUser = new Condition<>(Objects::nonNull, "isUser");

        assertThat(processInstance)
                .hasPassed("Activity_payload", "Activity_selectPayment", "Activity_checkData", "Activity_setPaymentFlags", "Activity_savePayment", "Activity_createCase")
                .hasNotPassed("Activity_setRules", "Activity_saveRules")
                .variables()
                .hasEntrySatisfying("caseData", isCase)
                .hasEntrySatisfying("acceptedRules", isRules)
                .hasEntrySatisfying("payment", isPayment)
                .hasEntrySatisfying("user", isUser)
        ;
    }
}
