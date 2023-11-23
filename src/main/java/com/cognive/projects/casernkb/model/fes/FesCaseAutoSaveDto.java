package com.cognive.projects.casernkb.model.fes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FesCaseAutoSaveDto implements Serializable {

    private long clientId;
    private long caseId;
    private String rejectType;
}
