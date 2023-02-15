package com.cognive.projects.casernkb.service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public interface BPMProcessService {

    /**
     * Get deploy of process
     */

    //String getProcessInstanceId(String processKey, String orderCheckoutId) throws IOException;

    String startProcess(String key, Map<String, Object> variables);

    String startProcess(String processId, String businessKey, Map<String, Object> variables);

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

    /**
     * create camunda message
     */

    void message(String businessKey, String name, String data);


}
