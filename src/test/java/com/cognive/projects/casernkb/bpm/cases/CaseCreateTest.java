package com.cognive.projects.casernkb.bpm.cases;

import camundajar.impl.scala.util.parsing.json.JSON;
import com.cognive.projects.casernkb.bpm.TestUtils;
import com.cognive.projects.casernkb.repo.BaseDictRepo;
import com.prime.db.rnkb.model.*;
import lombok.SneakyThrows;
import org.assertj.core.api.Condition;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.ObjectValue;
import org.camunda.bpm.extension.mockito.mock.FluentJavaDelegateMock;
import org.camunda.spin.Spin;
import org.camunda.spin.plugin.variable.SpinValues;
import org.camunda.spin.plugin.variable.value.JsonValue;
import org.junit.Rule;
import org.junit.Test;

import java.math.BigDecimal;
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
        "bpmn/cases/caseCreate.bpmn"
})
public class CaseCreateTest {
    @Rule
    public ProcessEngineRule processEngineRule = new ProcessEngineRule();

    @Test
    @SneakyThrows
    public void Should_create_case() {
        autoMock("bpmn/cases/caseCreate.bpmn");

        Payment payment = new Payment();
        payment.setName("Test");
        payment.setAmountNationalCurrency(BigDecimal.valueOf(500));

        final BaseDictRepo baseDictionaryRepository = registerMockInstance(BaseDictRepo.class);
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCode(18, "4")).thenReturn(TestUtils.getBaseDictionary("4"));



        Map<String, Object> processParameters = new HashMap<>();
        processParameters.put("acceptedRules",
                Arrays.asList(TestUtils.getBaseDictionary("5"),
                TestUtils.getBaseDictionary("4"),
                TestUtils.getBaseDictionary("3")));
        processParameters.put("payment", payment);
        processParameters.put("caseType", "4");

        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("caseCreate", processParameters);

        Condition<Object> isCase = new Condition<>(p -> {
            Case c = (Case)p;
            return c.getCaseType().getCode().equals("4");
        }, "isCase");


        Condition<Object> isCaseRelation = new Condition<>(p -> {
            List<Object> list = (List<Object>)p;
            return list.size() == 4 &&
                    ((CaseRules)list.get(0)).getRuleId().getCode().equals("5") &&
                    ((CaseRules)list.get(1)).getRuleId().getCode().equals("4") &&
                    ((CaseRules)list.get(2)).getRuleId().getCode().equals("3") &&
                    ((CaseOperation)list.get(3)).getAmount().equals(BigDecimal.valueOf(500))
                    ;
        }, "isCaseRules size 2");

        assertThat(processInstance)
                .hasPassed("Activity_createCase", "Activity_saveCase", "Activity_caseRelations", "Activity_saveCaseRelations")
                .variables()
                .hasEntrySatisfying("caseData", isCase)
                .hasEntrySatisfying("caseRelationList", isCaseRelation);
    }

    @Test
    @SneakyThrows
    public void Should_create_case_with_user() {
        autoMock("bpmn/cases/caseCreate.bpmn");

        Payment payment = new Payment();
        payment.setName("Test");
        payment.setAmountNationalCurrency(BigDecimal.valueOf(500));

        SysUser sysUser = new SysUser();
        sysUser.setId(5L);
        sysUser.setName("Test");

        final BaseDictRepo baseDictionaryRepository = registerMockInstance(BaseDictRepo.class);
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCode(18, "4")).thenReturn(TestUtils.getBaseDictionary("4"));
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCode(75, "6001")).thenReturn(TestUtils.getBaseDictionary("6001"));

        Map<String, Object> selectResult = new HashMap<>();
        selectResult.put("user", sysUser);

        final FluentJavaDelegateMock selectOneDelegate = registerJavaDelegateMock("selectOneDelegate");
        selectOneDelegate.onExecutionSetVariables(selectResult);

        Map<String, Object> processParameters = new HashMap<>();
        processParameters.put("acceptedRules",
                Arrays.asList(TestUtils.getBaseDictionary("5"),
                        TestUtils.getBaseDictionary("4"),
                        TestUtils.getBaseDictionary("3")));
        processParameters.put("payment", payment);
        processParameters.put("caseType", "4");
        processParameters.put("comment", "<comment>");
        processParameters.put("userId", 5L);

        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("caseCreate", processParameters);

        Condition<Object> isCase = new Condition<>(p -> {
            Case c = (Case)p;
            return c.getCaseType().getCode().equals("4");
        }, "isCase");


        Condition<Object> isCaseRelation = new Condition<>(p -> {
            List<Object> list = (List<Object>)p;
            return list.size() == 5 &&
                    ((CaseRules)list.get(0)).getRuleId().getCode().equals("5") &&
                    ((CaseRules)list.get(1)).getRuleId().getCode().equals("4") &&
                    ((CaseRules)list.get(2)).getRuleId().getCode().equals("3") &&
                    ((CaseRules)list.get(2)).getCode().getCode().equals("6001") &&
                    ((CaseOperation)list.get(3)).getAmount().equals(BigDecimal.valueOf(500)) &&
                    ((CaseComment)list.get(4)).getComment().equals("<comment>") &&
                    ((CaseComment)list.get(4)).getAuthor().getName().equals("Test")
                    ;
        }, "isCaseRules size 2");

        assertThat(processInstance)
                .hasPassed("Activity_createCase", "Activity_saveCase", "Activity_caseRelations", "Activity_saveCaseRelations",
                        "Activity_selectUser")
                .variables()
                .hasEntrySatisfying("caseData", isCase)
                .hasEntrySatisfying("caseRelationList", isCaseRelation);
    }
}
