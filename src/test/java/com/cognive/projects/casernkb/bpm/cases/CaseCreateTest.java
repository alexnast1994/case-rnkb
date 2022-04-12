package com.cognive.projects.casernkb.bpm.cases;

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
import org.junit.Rule;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.assertThat;
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
    public void Should_not_touch_any() {
        autoMock("bpmn/cases/caseCreate.bpmn");

        BaseDictionary bd3 = new BaseDictionary();
        BaseDictionary bd4 = new BaseDictionary();
        BaseDictionary bd5 = new BaseDictionary();

        bd3.setCode("3");
        bd4.setCode("4");
        bd5.setCode("5");

        Payment payment = new Payment();
        payment.setName("Test");
        payment.setAmountNationalCurrency(BigDecimal.valueOf(500));

        final BaseDictRepo baseDictionaryRepository = registerMockInstance(BaseDictRepo.class);
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCode(272, "3")).thenReturn(bd3);
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCode(272, "4")).thenReturn(bd4);
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCode(272, "5")).thenReturn(bd5);
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCode(18, "4")).thenReturn(bd4);

        Map<String, Object> processParameters = new HashMap<>();
        processParameters.put("acceptedRules", Arrays.asList("5", "4", "3"));
        processParameters.put("payment", payment);
        processParameters.put("caseType", "4");

        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("caseCreate", processParameters);

        Condition<Object> isCase = new Condition<>(p -> {
            Case c = (Case)p;
            return !c.getCaseOperationList().isEmpty()
                    && c.getCaseOperationList().get(0).getAmount().equals(BigDecimal.valueOf(500))
                    && c.getCaseType().getCode().equals("4")
                    && c.getCaseRules().size() == 3
                    && c.getCaseRules().get(1).getRuleId().getCode().equals("4");
        }, "isCase");
        assertThat(processInstance)
                .variables()
                .hasEntrySatisfying("caseData", isCase);
    }
}
