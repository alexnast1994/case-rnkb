package com.cognive.projects.casernkb.bpm.cases;

import com.cognive.projects.casernkb.model.CaseRulesDto;
import lombok.SneakyThrows;
import org.assertj.core.api.Condition;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.spin.json.SpinJsonNode;
import org.junit.Rule;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.assertThat;
import static org.camunda.bpm.extension.mockito.CamundaMockito.registerCallActivityMock;
import static org.camunda.bpm.extension.mockito.DelegateExpressions.autoMock;

@Deployment(resources = {
        "bpmn/cases/amlPaymentResponseBatch.bpmn"
})
public class AmlPaymentResponseBatchTest {
    @Rule
    public ProcessEngineRule processEngineRule = new ProcessEngineRule();

    private String getPayloadJson(List<String> paymentRuleDtoList) {
        return "{\"payload\":{\"amlPaymentResponseBatch\":{\"operation\":[" + String.join(",", paymentRuleDtoList) + "]}}}";
    }

    private String getPaymentRule(String caseType, Long paymentId, String rule) {
        return "{\"CaseType\":\"" + caseType + "\",\"PaymentId\": " + paymentId + ", \"UID\":\""+ rule + "\"}";
    }

    @Test
    @SneakyThrows
    public void Should_not_touch_any() {
        autoMock("bpmn/cases/amlPaymentResponseBatch.bpmn");

        processEngineRule.manageDeployment(registerCallActivityMock("paymentResponse")
                .deploy(processEngineRule)
        );

        Map<String, Object> processParams = new HashMap<>();
        processParams.put("payload", getPayloadJson(List.of(
                getPaymentRule("1", 123L, "4"),
                getPaymentRule("2", 123L, "5"),
                getPaymentRule("3", 124L, "1")
        )));

        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("amlPaymentResponseBatch", processParams);

        Condition<Object> isPayments = new Condition<>(p -> {
            List<CaseRulesDto> payments = (List<CaseRulesDto>)p;
            return payments.size() == 2 &&
                    payments.get(0).getRules().size() == 2 &&
                    payments.get(0).getPaymentId() == 123L &&
                    payments.get(0).getCaseType().equals("2") &&
                    payments.get(0).getRules().get(0).equals("4") &&
                    payments.get(0).getRules().get(1).equals("5") &&

                    payments.get(1).getRules().size() == 1 &&
                    payments.get(1).getPaymentId() == 124L &&
                    payments.get(1).getCaseType().equals("3") &&
                    payments.get(1).getRules().get(0).equals("1");
        }, "isCaseRules size 2");

        assertThat(processInstance)
                .hasPassed("Activity_processPaymentResponse", "Activity_parse", "Activity_loopPayment", "Event_end")
                .variables()
                .hasEntrySatisfying("payments", isPayments);
    }
}