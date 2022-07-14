package com.cognive.projects.casernkb.bpm.cases;

import com.cognive.projects.casernkb.bpm.TestUtils;
import com.cognive.projects.casernkb.repo.BaseDictRepo;
import com.prime.db.rnkb.model.*;
import lombok.SneakyThrows;
import org.assertj.core.api.Condition;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.extension.mockito.mock.FluentJavaDelegateMock;
import org.junit.Rule;
import org.junit.Test;

import java.util.*;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.assertThat;
import static org.camunda.bpm.extension.mockito.CamundaMockito.*;
import static org.camunda.bpm.extension.mockito.DelegateExpressions.autoMock;
import static org.mockito.Mockito.when;

@Deployment(resources = {
        "bpmn/cases/amlDroolsCase.bpmn"
})
public class AmlDroolsCaseTest {
    @Rule
    public ProcessEngineRule processEngineRule = new ProcessEngineRule();

    private static final String payloadJson = "{\"payload\":{\"amlDroolsCase\":{}}}";

    private Payment getPayment(Long id) {
        Payment payment = new Payment();
        payment.setId(id);
        payment.setName("Name " + id.toString());
        return payment;
    }

    private List<DroolsResponsePayment> getDroolsResponse(Payment payment, List<String> codes) {
        List<DroolsResponsePayment> rules = new ArrayList<>();
        codes.forEach(x -> {
            DroolsResponsePayment d = new DroolsResponsePayment();
            d.setPaymentId(payment);
            d.setUId(TestUtils.getBaseDictionaryCharCode("no", x));
            rules.add(d);
        });

        if(codes.isEmpty()) {
            DroolsResponsePayment dNoRules = new DroolsResponsePayment();
            dNoRules.setPaymentId(payment);
            rules.add(dNoRules);
        }

        return rules;
    }

    @Test
    @SneakyThrows
    public void Should_nothing() {
        autoMock("bpmn/cases/amlDroolsCase.bpmn");

        Map<String, Object> selectResult = new HashMap<>();
        selectResult.put("droolsData", Collections.emptyList());

        final FluentJavaDelegateMock selectOneDelegate = registerJavaDelegateMock("selectOneDelegate");
        selectOneDelegate.onExecutionSetVariables(selectResult);

        processEngineRule.manageDeployment(registerCallActivityMock("caseCreate")
                .onExecutionAddVariable("test", "test")
                .deploy(processEngineRule)
        );

        Map<String, Object> processParams = new HashMap<>();
        processParams.put("payload", payloadJson);

        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("amlDroolsCase", processParams);

        assertThat(processInstance)
                .hasNotPassed("Activity_checkRules");
    }

    @Test
    @SneakyThrows
    public void Should_nothing_no_rules() {
        autoMock("bpmn/cases/amlDroolsCase.bpmn");

        Payment payment = new Payment();

        Map<String, Object> selectResultMany = new HashMap<>();
        selectResultMany.put("droolsData", getDroolsResponse(payment, Collections.emptyList()));

        final FluentJavaDelegateMock selectDelegate = registerJavaDelegateMock("selectDelegate");
        selectDelegate.onExecutionSetVariables(selectResultMany);

        processEngineRule.manageDeployment(registerCallActivityMock("caseCreate")
                .onExecutionAddVariable("test", "test")
                .deploy(processEngineRule)
        );

        Map<String, Object> processParams = new HashMap<>();
        processParams.put("payload", payloadJson);

        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("amlDroolsCase", processParams);

        assertThat(processInstance)
                .hasPassed("Activity_checkRules", "Event_noRules");
    }

    @Test
    @SneakyThrows
    public void Should_createCase() {
        autoMock("bpmn/cases/amlDroolsCase.bpmn");

        Payment payment = new Payment();

        Map<String, Object> selectResultMany = new HashMap<>();
        selectResultMany.put("droolsData", getDroolsResponse(payment, List.of("01", "74")));

        final FluentJavaDelegateMock selectDelegate = registerJavaDelegateMock("selectDelegate");
        selectDelegate.onExecutionSetVariables(selectResultMany);

        processEngineRule.manageDeployment(registerCallActivityMock("caseCreate")
                .deploy(processEngineRule)
        );

        Map<String, Object> processParams = new HashMap<>();
        processParams.put("payload", payloadJson);

        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("amlDroolsCase", processParams);

        assertThat(processInstance)
                .hasPassed("Activity_callCreateCase");
    }

    @Test
    @SneakyThrows
    public void Should_check_codes() {
        autoMock("bpmn/cases/amlDroolsCase.bpmn");

        CaseRules cr1 = new CaseRules();
        CaseRules cr2 = new CaseRules();

        cr1.setRuleId(TestUtils.getBaseDictionaryCharCode("1", "01"));
        cr2.setRuleId(TestUtils.getBaseDictionaryCharCode("2", "74"));

        Case caseData = new Case();
        caseData.setCaseRules(Arrays.asList(cr1, cr2));
        caseData.setStatus(TestUtils.getBaseDictionary("1"));
        caseData.setCaseType(TestUtils.getBaseDictionary("1"));

        CaseOperation caseOperation = new CaseOperation();
        caseOperation.setCaseId(caseData);

        Payment payment = new Payment();
        payment.setCaseOperationList(Collections.singletonList(caseOperation));

        Map<String, Object> selectResultMany = new HashMap<>();
        selectResultMany.put("droolsData", getDroolsResponse(payment, List.of("01", "74")));

        final FluentJavaDelegateMock selectDelegate = registerJavaDelegateMock("selectDelegate");
        selectDelegate.onExecutionSetVariables(selectResultMany);

        processEngineRule.manageDeployment(registerCallActivityMock("caseCreate")
            .deploy(processEngineRule)
        );

        Map<String, Object> processParams = new HashMap<>();
        processParams.put("payload", payloadJson);

        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("amlDroolsCase", processParams);

        assertThat(processInstance)
                .hasPassed("Activity_checkCodes", "Event_noNewCodes");
    }


    @Test
    public void Should_save_new_rules() {
        autoMock("bpmn/cases/amlDroolsCase.bpmn");

        CaseRules cr1 = new CaseRules();

        cr1.setRuleId(TestUtils.getBaseDictionaryCharCode("1", "01"));

        Case caseData = new Case();
        caseData.setCaseRules(Collections.singletonList(cr1));
        caseData.setStatus(TestUtils.getBaseDictionary("1"));
        caseData.setCaseType(TestUtils.getBaseDictionary("1"));

        CaseOperation caseOperation = new CaseOperation();
        caseOperation.setCaseId(caseData);

        Payment payment = new Payment();
        payment.setCaseOperationList(Collections.singletonList(caseOperation));

        Map<String, Object> selectResultMany = new HashMap<>();
        selectResultMany.put("droolsData", getDroolsResponse(payment, List.of("01", "64", "74")));

        final FluentJavaDelegateMock selectDelegate = registerJavaDelegateMock("selectDelegate");
        selectDelegate.onExecutionSetVariables(selectResultMany);

        processEngineRule.manageDeployment(registerCallActivityMock("caseCreate")
                .deploy(processEngineRule)
        );

        Map<String, Object> processParams = new HashMap<>();
        processParams.put("payload", payloadJson);

        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("amlDroolsCase", processParams);

        Condition<Object> isCodes = new Condition<>(p -> {
                List<CaseRules> rules = (List<CaseRules>)p;
                return rules.size() == 1 &&
                        rules.get(0).getRuleId().getCharCode().equals("64");
            }, "isCaseRules size 1");

        assertThat(processInstance)
                .hasPassed("Activity_checkCodes", "Activity_saveRules")
                .variables()
                .hasEntrySatisfying("caseRules", isCodes);
    }

    @Test
    public void Should_save_comment() {
        autoMock("bpmn/cases/amlDroolsCase.bpmn");

        CaseRules cr1 = new CaseRules();
        cr1.setRuleId(TestUtils.getBaseDictionaryCharCode("1", "01"));

        Case caseData = new Case();
        caseData.setCaseRules(Collections.singletonList(cr1));
        caseData.setStatus(TestUtils.getBaseDictionary("4"));
        caseData.setCaseType(TestUtils.getBaseDictionary("1"));

        CaseOperation caseOperation = new CaseOperation();
        caseOperation.setCaseId(caseData);

        Payment payment = new Payment();
        payment.setCaseOperationList(Collections.singletonList(caseOperation));

        Map<String, Object> selectResultMany = new HashMap<>();
        selectResultMany.put("droolsData", getDroolsResponse(payment, List.of("01", "64", "65")));

        final FluentJavaDelegateMock selectDelegate = registerJavaDelegateMock("selectDelegate");
        selectDelegate.onExecutionSetVariables(selectResultMany);

        processEngineRule.manageDeployment(registerCallActivityMock("caseCreate")
                .deploy(processEngineRule)
        );

        Map<String, Object> processParams = new HashMap<>();
        processParams.put("payload", payloadJson);

        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("amlDroolsCase", processParams);

        Condition<Object> isComment = new Condition<>(p -> {
            List<CaseComment> comments = (List<CaseComment>)p;
            return comments.size() == 1 &&
                    comments.get(0).getComment().equals("64;65");
        }, "isComment size 1");

        assertThat(processInstance)
                .hasPassed("Activity_checkCodes", "Activity_saveComments")
                .variables()
                .hasEntrySatisfying("caseComments", isComment);
    }
}
