package com.cognive.projects.casernkb.bpm.cases;

import com.cognive.projects.casernkb.model.CaseRulesDto;
import com.cognive.projects.casernkb.repo.BaseDictRepo;
import com.prime.db.rnkb.model.BaseDictionary;
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
import static org.camunda.bpm.extension.mockito.CamundaMockito.*;
import static org.camunda.bpm.extension.mockito.DelegateExpressions.autoMock;
import static org.mockito.Mockito.when;

@Deployment(resources = {
        "bpmn/cases/amlPaymentCasePostBatch.bpmn"
})
public class AmlPaymentCasePostBatchTest {
    @Rule
    public ProcessEngineRule processEngineRule = new ProcessEngineRule();

    private String getPayloadJson() {
        return "{\"payload\":{\"amlPaymentCasePostBatch\":{}}}";
    }

    private PipelineResponsePayment getPaymentRule(Long paymentId, String rule) {

        Payment p = new Payment();
        p.setId(paymentId);

        BaseDictionary b = new BaseDictionary();
        b.setCode(rule);

        PipelineResponsePayment pr = new PipelineResponsePayment();
        pr.setPaymentId(p);
        pr.setUId(b);

        return pr;
    }

    @Test
    @SneakyThrows
    public void Should_not_touch_any() {
        autoMock("bpmn/cases/amlPaymentCasePostBatch.bpmn");

        processEngineRule.manageDeployment(registerCallActivityMock("caseCreate")
                .deploy(processEngineRule)
        );

        Map<String, Object> processParams = new HashMap<>();
        processParams.put("payload", getPayloadJson());

        BaseDictionary bd4 = new BaseDictionary();
        BaseDictionary bd5 = new BaseDictionary();
        BaseDictionary bd1 = new BaseDictionary();
        bd4.setCharCode("4");
        bd4.setCode("4");

        bd5.setCharCode("5");
        bd5.setCode("5");

        bd1.setCharCode("1");
        bd1.setCode("34");

        final BaseDictRepo baseDictionaryRepository = registerMockInstance(BaseDictRepo.class);
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCharCode(272, "4")).thenReturn(bd4);
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCharCode(272, "5")).thenReturn(bd5);
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCharCode(272, "1")).thenReturn(bd1);

        Map<String, Object> selectResult = new HashMap<>();
        selectResult.put("pipeLineData", List.of(
                getPaymentRule(123L, "4"),
                getPaymentRule(124L, "1"),
                getPaymentRule(123L, "3")
        ));

        final FluentJavaDelegateMock selectOneDelegate = registerJavaDelegateMock("selectDelegate");
        selectOneDelegate.onExecutionSetVariables(selectResult);

        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("amlPaymentCasePostBatch", processParams);

        Condition<Object> isPayments = new Condition<>(p -> {
            List<CaseRulesDto> payments = (List<CaseRulesDto>)p;
            return payments.size() == 2 &&
                    payments.get(0).getRules().size() == 2 &&
                    payments.get(0).getPaymentId().getId().equals(123L) &&
                    payments.get(0).getRules().get(0).getCode().equals("4") &&
                    payments.get(0).getRules().get(1).getCode().equals("3") &&

                    payments.get(1).getRules().size() == 0 &&
                    payments.get(1).getPaymentId().getId().equals(124L);
        }, "isCaseRules size 2");

        assertThat(processInstance)
                .hasPassed("Activity_pipelineData", "Activity_createCase")
                .variables()
                .hasEntrySatisfying("payments", isPayments);
    }
}