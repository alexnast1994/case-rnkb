package com.cognive.projects.casernkb.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PipelineResponse {
    private String workflowId;
    private String mappingName;
    private String status;
    private String errorMessage;

    public boolean isDone() {
        return "DONE".compareToIgnoreCase(status) == 0;
    }

    public boolean isWorkflowId(String workflowId) {
        if(workflowId == null || this.workflowId == null) return false;
        return workflowId.compareToIgnoreCase(this.workflowId) == 0;
    }
}
