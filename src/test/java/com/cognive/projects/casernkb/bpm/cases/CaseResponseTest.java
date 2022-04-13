package com.cognive.projects.casernkb.bpm.cases;

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

import java.util.HashMap;
import java.util.Map;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.assertThat;
import static org.camunda.bpm.extension.mockito.CamundaMockito.registerJavaDelegateMock;
import static org.camunda.bpm.extension.mockito.DelegateExpressions.autoMock;

@Deployment(resources = {
        "bpmn/cases/caseResponse.bpmn"
})
public class CaseResponseTest {
    @Rule
    public ProcessEngineRule processEngineRule = new ProcessEngineRule();


    @Test
    @SneakyThrows
    public void Should_not_touch_any() {
        autoMock("bpmn/cases/caseResponse.bpmn");

        BaseDictionary bd1 = new BaseDictionary();
        BaseDictionary bd2 = new BaseDictionary();

        bd1.setCode("1");
        bd2.setCode("2");

        Payment payment = new Payment();
        payment.setSourceSystems(bd2);

        Case caseData = new Case();
        caseData.setStatus(bd1);

        Map<String, Object> processParameters = new HashMap<>();
        processParameters.put("key", "123");
        processParameters.put("payment", payment);
        processParameters.put("messageId", "message");
        processParameters.put("caseData", caseData);

        final FluentJavaDelegateMock kafkaDelegate = registerJavaDelegateMock("kafkaDelegate");

        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("caseResponse", processParameters);

        Condition<Object> isNull = new Condition<>(p -> !((String)p).isEmpty(), "isNotEmpty");
        assertThat(processInstance)
                .hasPassed("Activity_saveJson", "Activity_sendKafka")
                .variables()
                .hasEntrySatisfying("caseJson", isNull);
    }
}
