package com.cognive.projects.casernkb.bpm.cases;

import com.cognive.projects.casernkb.bpm.TestUtils;
import com.cognive.projects.casernkb.model.CaseRulesDto;
import com.prime.db.rnkb.model.Payment;
import com.prime.db.rnkb.model.PipelineResponsePayment;
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
import java.util.List;
import java.util.Map;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.assertThat;
import static org.camunda.bpm.extension.mockito.CamundaMockito.registerCallActivityMock;
import static org.camunda.bpm.extension.mockito.CamundaMockito.registerJavaDelegateMock;
import static org.camunda.bpm.extension.mockito.DelegateExpressions.autoMock;

@Deployment(resources = {
        "bpmn/cases/amlPaymentResponseBatch.bpmn"
})
public class AmlPaymentResponseBatchTest {
    @Rule
    public ProcessEngineRule processEngineRule = new ProcessEngineRule();

    private String getPayloadJson() {
        return "{\"payload\":{\"amlPaymentResponseBatch\":{}}}";
    }

    private PipelineResponsePayment getPaymentRule(Long paymentId, String rule) {

        Payment pp = new Payment();
        pp.setId(paymentId);

        PipelineResponsePayment p = new PipelineResponsePayment();

        p.setUId(TestUtils.getBaseDictionary(rule));
        p.setPaymentId(pp);
        return p;
    }

    @Test
    @SneakyThrows
    public void Should_not_touch_any() {
        autoMock("bpmn/cases/amlPaymentResponseBatch.bpmn");

        processEngineRule.manageDeployment(registerCallActivityMock("paymentResponse")
                .deploy(processEngineRule)
        );

        Map<String, Object> processParams = new HashMap<>();
        processParams.put("payload", getPayloadJson());

        Map<String, Object> selectResultMany = new HashMap<>();
        selectResultMany.put("pipelineData", List.of(
                getPaymentRule(123L, "4"),
                getPaymentRule(123L, "5"),
                getPaymentRule(124L, "1")
        ));

        final FluentJavaDelegateMock selectDelegate = registerJavaDelegateMock("selectDelegate");
        selectDelegate.onExecutionSetVariables(selectResultMany);

        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("amlPaymentResponseBatch", processParams);

        Condition<Object> isPayments = new Condition<>(p -> {
            List<CaseRulesDto> payments = (List<CaseRulesDto>)p;
            return payments.size() == 2 &&
                    payments.get(0).getRules().size() == 2 &&
                    payments.get(0).getPaymentId().getId().equals(123L) &&
                    payments.get(0).getRules().get(0).getCode().equals("4") &&
                    payments.get(0).getRules().get(1).getCode().equals("5") &&

                    payments.get(1).getRules().size() == 1 &&
                    payments.get(1).getPaymentId().getId().equals(124L) &&
                    payments.get(1).getRules().get(0).getCode().equals("1");
        }, "isCaseRules size 2");

        assertThat(processInstance)
                .hasPassed("Activity_selectPipelineResult", "Activity_parsePipelineData", "Activity_process21", "Event_end")
                .variables()
                .hasEntrySatisfying("payments", isPayments);
    }
}