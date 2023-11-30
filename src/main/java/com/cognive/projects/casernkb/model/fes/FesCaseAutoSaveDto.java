package com.cognive.projects.casernkb.model.fes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FesCaseAutoSaveDto implements Serializable {

    private long clientId;
    private long caseId;
    private String rejectType;
    private String baseRejectCode;
    private List<String> causeReject;
    private List<String> codeUnusualOp;
    private String conclusion;
}
