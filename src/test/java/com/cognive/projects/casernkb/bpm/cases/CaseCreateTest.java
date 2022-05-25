package com.cognive.projects.casernkb.bpm.cases;

import camundajar.impl.scala.util.parsing.json.JSON;
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

        bd3.setCharCode("13");
        bd3.setCode("3");
        bd4.setCharCode("14");
        bd4.setCode("4");
        bd5.setCharCode("15");
        bd5.setCode("5");

        Payment payment = new Payment();
        payment.setName("Test");
        payment.setAmountNationalCurrency(BigDecimal.valueOf(500));

        final BaseDictRepo baseDictionaryRepository = registerMockInstance(BaseDictRepo.class);
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCharCode(272, "13")).thenReturn(bd3);
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCharCode(272, "14")).thenReturn(bd4);
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCharCode(272, "15")).thenReturn(bd5);
        when(baseDictionaryRepository.getByBaseDictionaryTypeCodeAndCode(18, "4")).thenReturn(bd4);

        Map<String, Object> processParameters = new HashMap<>();
        processParameters.put("acceptedRules", Arrays.asList("15", "14", "13"));
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
}
