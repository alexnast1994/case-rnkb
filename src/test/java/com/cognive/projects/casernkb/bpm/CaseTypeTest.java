package com.cognive.projects.casernkb.bpm;

import com.cognive.projects.casernkb.repo.BaseDictRepo;
import com.prime.db.rnkb.model.BaseDictionary;
import com.prime.db.rnkb.model.Case;
import com.prime.db.rnkb.model.Payment;
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
import java.util.Map;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.assertThat;
import static org.camunda.bpm.extension.mockito.CamundaMockito.registerJavaDelegateMock;
import static org.camunda.bpm.extension.mockito.CamundaMockito.registerMockInstance;
import static org.camunda.bpm.extension.mockito.DelegateExpressions.autoMock;
import static org.mockito.Mockito.when;


@Deployment(resources = {
    "bpmn/cases/caseType.bpmn"
})
public class CaseTypeTest {
    @Rule
    public ProcessEngineRule processEngineRule = new ProcessEngineRule();

    @Test
    @SneakyThrows
    public void Should_not_touch_any() {
        autoMock("bpmn/cases/caseType.bpmn");

        Payment pWork = new Payment();
        Payment pBase = new Payment();

        pWork.setCheckFlag(2);

        Map<String, Object> selectResult = new HashMap<>();
        selectResult.put("paymentWork", pWork);
        selectResult.put("paymentBase", pBase);

        final FluentJavaDelegateMock selectOneDelegate = registerJavaDelegateMock("selectOneDelegate");
        selectOneDelegate.onExecutionSetVariables(selectResult);

        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("caseType");

        Condition<Object> isNull = new Condition<>(p -> ((Payment)p).getCheckFlag() != null, "isNull");
        assertThat(processInstance)
                .variables()
                    .hasEntrySatisfying("paymentWork", isNull);
    }

    @Test
    public void Should_not_touch_payment() {
        autoMock("bpmn/cases/caseType.bpmn");

        Payment pWork = new Payment();
        Payment pBase = new Payment();

        pWork.setCheckFlag(2);
        pBase.setCheckFlag(2);

        pWork.setName("1");
        pBase.setName("2");

        Map<String, Object> selectResult = new HashMap<>();
        selectResult.put("paymentWork", pWork);
        selectResult.put("paymentBase", pBase);

        final FluentJavaDelegateMock selectOneDelegate = registerJavaDelegateMock("selectOneDelegate");
        selectOneDelegate.onExecutionSetVariables(selectResult);

        final FluentJavaDelegateMock objectDiffDelegate = registerJavaDelegateMock("objectDiff");
        objectDiffDelegate.onExecutionSetVariable("diffChanges", true);

        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("caseType");

        Condition<Object> isNull = new Condition<>(p -> ((Payment)p).getCheckFlag() != null, "isNull");
        assertThat(processInstance)
                .hasPassed("Activity_selectCase", "Event_noCases")
                .hasNotPassed("Activity_savePayment", "Activity_clearFlag")
                .variables().hasEntrySatisfying("paymentWork", isNull);
    }

    @Test
    public void Should_clear_payment() {
        autoMock("bpmn/cases/caseType.bpmn");

        Payment pWork = new Payment();
        Payment pBase = new Payment();

        pWork.setCheckFlag(2);
        pBase.setCheckFlag(2);

        pWork.setName("1");
        pBase.setName("2");

        Map<String, Object> selectResult = new HashMap<>();
        selectResult.put("paymentWork", pWork);
        selectResult.put("paymentBase", pBase);

        Map<String, Object> objectDiffResult = new HashMap<>();
        objectDiffResult.put("diffChanges", true);
        objectDiffResult.put("diffPaths", Arrays.asList("sourceSystems", "dateIn"));

        final FluentJavaDelegateMock selectOneDelegate = registerJavaDelegateMock("selectOneDelegate");
        selectOneDelegate.onExecutionSetVariables(selectResult);

        final FluentJavaDelegateMock objectDiffDelegate = registerJavaDelegateMock("objectDiff");
        objectDiffDelegate.onExecutionSetVariables(objectDiffResult);

        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("caseType");

        Condition<Object> isNull = new Condition<>(p -> ((Payment)p).getCheckFlag() == null, "isNull");
        assertThat(processInstance)
                .hasPassed("Activity_selectCase", "Activity_clearFlag")
                .variables().hasEntrySatisfying("paymentWork", isNull);
    }

    @Test
    public void Should_save_case() {
        autoMock("bpmn/cases/caseType.bpmn");

        BaseDictionary caseType3 = new BaseDictionary();
        BaseDictionary caseStatus9 = new BaseDictionary();
        BaseDictionary caseStatus1 = new BaseDictionary();

        caseType3.setCode("3");
        caseStatus9.setCode("9");
        caseStatus1.setCode("1");

        Payment pWork = new Payment();
        Payment pBase = new Payment();

        pWork.setCheckFlag(2);
        pBase.setCheckFlag(2);

        pWork.setName("1");
        pBase.setName("2");

        Case caseData = new Case();
        caseData.setCaseType(caseType3);
        caseData.setStatus(caseStatus1);

        Map<String, Object> selectResult = new HashMap<>();
        selectResult.put("paymentWork", pWork);
        selectResult.put("paymentBase", pBase);
        selectResult.put("caseData", caseData);

        Map<String, Object> objectDiffResult = new HashMap<>();
        objectDiffResult.put("diffChanges", true);
        objectDiffResult.put("diffPaths", Arrays.asList("sourceSystems", "dateIn"));

        final FluentJavaDelegateMock selectOneDelegate = registerJavaDelegateMock("selectOneDelegate");
        selectOneDelegate.onExecutionSetVariables(selectResult);

        final FluentJavaDelegateMock objectDiffDelegate = registerJavaDelegateMock("objectDiff");
        objectDiffDelegate.onExecutionSetVariables(objectDiffResult);

        final BaseDictRepo baseDictionaryRepository = registerMockInstance(BaseDictRepo.class);
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCode(16, "9")).thenReturn(caseStatus9);

        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("caseType");

        Condition<Object> isNull = new Condition<>(p -> ((Payment)p).getCheckFlag() == null, "isNull");
        Condition<Object> isStatus = new Condition<>(p -> ((Case)p).getStatus().getCode().equals("9"), "is9");
        assertThat(processInstance)
                .hasPassed("Activity_selectCase", "Activity_clearFlag", "Activity_saveCase")
                .variables()
                    .hasEntrySatisfying("paymentWork", isNull)
                    .hasEntrySatisfying("caseData", isStatus);
    }
}
