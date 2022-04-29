package com.cognive.projects.casernkb.bpm.cases;

import com.cognive.projects.casernkb.RepositoryMockedTest;
import com.cognive.projects.casernkb.repo.BaseDictRepo;
import com.prime.db.rnkb.model.Payment;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Condition;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.assertThat;
import static org.camunda.bpm.extension.mockito.CamundaMockito.autoMock;

@Slf4j
@Deployment(resources = {
        "bpmn/cases/testMock.bpmn"
})
public class MockByJsonTest {
    @Mock
    BaseDictRepo baseDictRepo;

    @Rule
    public ProcessEngineRule processEngineRule = new ProcessEngineRule();

//    @Before
//    public void init() {
//              mockFromResource("mock_data.json");
////        autoMock("bpmn/cases/testMock.bpmn");
//    }

    @Test
    public void should_works() {

        RepositoryMockedTest.withResource("mock_data.json");
        autoMock("bpmn/cases/testMock.bpmn");

        RuntimeService runtimeService = processEngineRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("testMock");

        BaseDictRepo baseDictRepo = Mockito.mock(BaseDictRepo.class);


        assertThat(processInstance)
                .variables()
                .containsEntry("b1", "Code 1")
                .containsEntry("b2", "Code 2")
                ;
    }
}
