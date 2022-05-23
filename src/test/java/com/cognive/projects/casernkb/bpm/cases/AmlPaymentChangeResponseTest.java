package com.cognive.projects.casernkb.bpm.cases;

import com.cognive.projects.casernkb.model.PaymentDto;
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
import org.camunda.spin.json.SpinJsonNode;
import org.camunda.spin.plugin.variable.SpinValues;
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
    "bpmn/cases/amlPaymentChangeResponse.bpmn"
})
public class AmlPaymentChangeResponseTest {
    @Rule
    public ProcessEngineRule processEngineRule = new ProcessEngineRule();

    private String getPayloadJson(PaymentDto paymentOld, PaymentDto paymentNew) {
        return "{\"payload\":{\"amlPaymentChangeResponse\":{\"OldPayment\":" + SpinJsonNode.JSON(paymentOld).toString()
                + ",\"NewPayment\":" + SpinJsonNode.JSON(paymentNew).toString() + "}}}";
    }

    @Test
    @SneakyThrows
    public void Should_not_touch_any() {
        autoMock("bpmn/cases/amlPaymentChangeResponse.bpmn");

        PaymentDto pOld = new PaymentDto();
        PaymentDto pNew = new PaymentDto();

        pOld.setId(123L);
        pNew.setId(124L);

        Payment payment = new Payment();
        payment.setCheckFlag(2);

        Map<String, Object> selectResult = new HashMap<>();
        selectResult.put("payment", payment);

        final FluentJavaDelegateMock selectOneDelegate = registerJavaDelegateMock("selectOneDelegate");
        selectOneDelegate.onExecutionSetVariables(selectResult);

        Map<String, Object> processParams = new HashMap<>();
        processParams.put("payload", getPayloadJson(pOld, pNew));

        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("amlPaymentChangeResponse", processParams);

        assertThat(processInstance)
                .hasPassed("Activity_diffObjects", "Activity_setFlags", "Event_noChanges")
                .hasNotPassed("Event_noCases", "Event_caseSaved")
                .variables()
                    .doesNotContainKey("payment");
    }

    @Test
    public void Should_not_touch_payment() {
        autoMock("bpmn/cases/amlPaymentChangeResponse.bpmn");

        PaymentDto pOld = new PaymentDto();
        PaymentDto pNew = new PaymentDto();

        pOld.setId(123L);
        pNew.setId(124L);

        Payment payment = new Payment();
        payment.setCheckFlag(2);

        Map<String, Object> selectResult = new HashMap<>();
        selectResult.put("payment", payment);

        pOld.setPurpose("1");
        pNew.setPurpose("2");

        final FluentJavaDelegateMock selectOneDelegate = registerJavaDelegateMock("selectOneDelegate");
        selectOneDelegate.onExecutionSetVariables(selectResult);

        final FluentJavaDelegateMock objectDiffDelegate = registerJavaDelegateMock("objectDiff");
        objectDiffDelegate.onExecutionSetVariable("diffChanges", true);

        Map<String, Object> processParams = new HashMap<>();
        processParams.put("payload", getPayloadJson(pOld, pNew));

        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("amlPaymentChangeResponse", processParams);

        Condition<Object> isNull = new Condition<>(p -> ((Payment)p).getCheckFlag() != null, "isNull");
        assertThat(processInstance)
                .hasPassed("Activity_selectCase", "Event_noCases")
                .hasNotPassed("Activity_savePayment", "Activity_clearFlag")
                .variables().hasEntrySatisfying("payment", isNull);
    }

    @Test
    public void Should_clear_payment() {
        autoMock("bpmn/cases/amlPaymentChangeResponse.bpmn");

        PaymentDto pOld = new PaymentDto();
        PaymentDto pNew = new PaymentDto();

        pOld.setId(123L);
        pNew.setId(124L);

        Payment payment = new Payment();
        payment.setCheckFlag(2);

        Map<String, Object> selectResult = new HashMap<>();
        selectResult.put("payment", payment);

        pOld.setPurpose("1");
        pNew.setPurpose("2");

        Map<String, Object> objectDiffResult = new HashMap<>();
        objectDiffResult.put("diffChanges", true);
        objectDiffResult.put("diffPaths", Arrays.asList("SOURCESYSTEMS", "DATEIN"));

        final FluentJavaDelegateMock selectOneDelegate = registerJavaDelegateMock("selectOneDelegate");
        selectOneDelegate.onExecutionSetVariables(selectResult);

        final FluentJavaDelegateMock objectDiffDelegate = registerJavaDelegateMock("objectDiff");
        objectDiffDelegate.onExecutionSetVariables(objectDiffResult);

        Map<String, Object> processParams = new HashMap<>();
        processParams.put("payload", getPayloadJson(pOld, pNew));

        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("amlPaymentChangeResponse", processParams);

        Condition<Object> isNull = new Condition<>(p -> ((Payment)p).getCheckFlag() == null, "isNull");
        assertThat(processInstance)
                .hasPassed("Activity_selectCase", "Activity_clearFlag")
                .variables().hasEntrySatisfying("payment", isNull);
    }

    @Test
    public void Should_save_case() {
        autoMock("bpmn/cases/amlPaymentChangeResponse.bpmn");

        BaseDictionary caseType3 = new BaseDictionary();
        BaseDictionary caseStatus9 = new BaseDictionary();
        BaseDictionary caseStatus1 = new BaseDictionary();

        caseType3.setCode("3");
        caseStatus9.setCode("9");
        caseStatus1.setCode("1");

        PaymentDto pOld = new PaymentDto();
        PaymentDto pNew = new PaymentDto();

        pOld.setId(123L);
        pNew.setId(124L);

        Payment payment = new Payment();
        payment.setCheckFlag(2);

        pOld.setPurpose("1");
        pNew.setPurpose("2");

        Case caseData = new Case();
        caseData.setCaseType(caseType3);
        caseData.setStatus(caseStatus1);

        Map<String, Object> selectResult = new HashMap<>();
        selectResult.put("payment", payment);
        selectResult.put("caseData", caseData);

        Map<String, Object> objectDiffResult = new HashMap<>();
        objectDiffResult.put("diffChanges", true);
        objectDiffResult.put("diffPaths", Arrays.asList("SOURCESYSTEMS", "DATEIN"));

        final FluentJavaDelegateMock selectOneDelegate = registerJavaDelegateMock("selectOneDelegate");
        selectOneDelegate.onExecutionSetVariables(selectResult);

        final FluentJavaDelegateMock objectDiffDelegate = registerJavaDelegateMock("objectDiff");
        objectDiffDelegate.onExecutionSetVariables(objectDiffResult);

        final BaseDictRepo baseDictionaryRepository = registerMockInstance(BaseDictRepo.class);
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCode(16, "9")).thenReturn(caseStatus9);

        Map<String, Object> processParams = new HashMap<>();
        processParams.put("payload", getPayloadJson(pOld, pNew));

        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("amlPaymentChangeResponse", processParams);

        Condition<Object> isNull = new Condition<>(p -> ((Payment)p).getCheckFlag() == null, "isNull");
        Condition<Object> isStatus = new Condition<>(p -> ((Case)p).getStatus().getCode().equals("9"), "is9");
        assertThat(processInstance)
                .hasPassed("Activity_selectCase", "Activity_clearFlag", "Activity_saveCase")
                .variables()
                    .hasEntrySatisfying("payment", isNull)
                    .hasEntrySatisfying("caseData", isStatus);
    }
}
