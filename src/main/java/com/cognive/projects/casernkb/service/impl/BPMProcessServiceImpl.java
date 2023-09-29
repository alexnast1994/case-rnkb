package com.cognive.projects.casernkb.service.impl;

import com.cognive.projects.casernkb.config.property.KafkaServerProperties;
import com.cognive.projects.casernkb.service.BPMProcessService;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.MessageCorrelationBuilder;
import org.camunda.bpm.engine.runtime.ProcessInstanceWithVariables;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.task.TaskQuery;
import org.camunda.bpm.engine.variable.VariableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BPMProcessServiceImpl implements BPMProcessService {
    RuntimeService runtimeService;
    RepositoryService repositoryService;
    TaskService taskService;
    HistoryService historyService;

    private final KafkaServerProperties kafkaServerProperties;

    @Autowired
    public BPMProcessServiceImpl(RuntimeService runtimeService,
                                 RepositoryService repositoryService,
                                 TaskService taskService,
                                 HistoryService historyService,
                                 KafkaServerProperties kafkaServerProperties) {
        this.runtimeService = runtimeService;
        this.repositoryService = repositoryService;
        this.taskService = taskService;
        this.historyService = historyService;
        this.kafkaServerProperties = kafkaServerProperties;
    }

    @Override
    public String startProcess(String processId, Map<String, Object> variables) {
        fillTopicMapping(processId, variables);
        return runtimeService.startProcessInstanceByKey(processId, variables).getProcessDefinitionId();
    }

    @Override
    public String startProcess(String processId, String businessKey, Map<String, Object> variables) {
        fillTopicMapping(processId, variables);
        return runtimeService.startProcessInstanceByKey(processId, businessKey, variables).getProcessInstanceId();
    }

    @Override
    public String startProcessReturnVariable(String processId, String businessKey, Map<String, Object> variables) {
        fillTopicMapping(processId, variables);
        return runtimeService.createProcessInstanceByKey(processId)
                .businessKey(businessKey)
                .setVariables(variables)
                .executeWithVariablesInReturn().getVariables().toString();
    }

    @Override
    public Map<String, Object> startProcessReturnVariables(String processId, String businessKey, Map<String, Object> variables, List<String> returnVariables) {
        fillTopicMapping(processId, variables);
        Map<String, Object> resultVariables = new HashMap<>();

        ProcessInstanceWithVariables processInstanceWithVariables = runtimeService.createProcessInstanceByKey(processId)
                .businessKey(businessKey)
                .setVariables(variables)
                .executeWithVariablesInReturn();

        VariableMap variablesMap = processInstanceWithVariables.getVariables();

        for (String returnVariable : returnVariables) {
            if (variablesMap.containsKey(returnVariable)) {
                resultVariables.put(returnVariable, variablesMap.getValueTyped(returnVariable).getValue());
            }
        }

        return resultVariables;
    }

    private void fillTopicMapping(String processId, Map<String, Object> variables) {
        kafkaServerProperties.getPropertiesList().forEach(p -> {
            if (processId.equalsIgnoreCase(p.getProcessName())) {
                variables.put("topics", p.getResponseTopicMapping());
            }
        });
    }

    @Override
    public String startProcessReturnInstanceId(String key, HashMap<String, Object> variables) {
        return runtimeService.startProcessInstanceByKey(key, variables).getProcessInstanceId();
    }

    @Override
    public String changeTaskName(String taskId, String taskName) {
        TaskQuery taskQuery = taskService.createTaskQuery().taskId(taskId);
        if (taskQuery.count() == 0) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "404 task (" + taskId + ") not found");
        }

        //Get current time
        Calendar calendar = Calendar.getInstance();
        //Get task information
        Task task = taskQuery.singleResult();

        if (task.getDueDate().after(calendar.getTime())) {
            task.setName(taskName);
            taskService.saveTask(task);
            return "Task name was changed";
        } else {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Can't change name, if current time more than due date time");
        }
    }

    @Override
    public InputStream getProcessDiagram(String id) {
        return repositoryService.getProcessModel(id);
    }

    @Override
    public void message(String businessKey, String name, String data) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("payload", data);

        MessageCorrelationBuilder builder = runtimeService.createMessageCorrelation(name);
        builder.setVariables(variables)
                .processInstanceBusinessKey(businessKey)
                .correlate();
    }
}
