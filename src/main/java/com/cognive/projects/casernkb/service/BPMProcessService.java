package com.cognive.projects.casernkb.service;

import org.camunda.bpm.engine.repository.ProcessDefinition;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public interface BPMProcessService {

    /**
     * Get process instance
     */
    ProcessDefinition getProcessInstance(String processId);


    /**
     * Get deploy of process
     */

    String getProcessInstanceId(String processKey, String orderCheckoutId) throws IOException;

    String startProcess(String key, HashMap<String, Object> variables);

    /**
     * start process
     */

    String startProcessReturnInstanceId(String key, HashMap<String, Object> variables);

    /**
     * start process by instance identifier
     */

    String changeTaskName(String taskId, String taskName);

    /**
     * change process task name
     */

    InputStream getProcessDiagram(String id);


}
