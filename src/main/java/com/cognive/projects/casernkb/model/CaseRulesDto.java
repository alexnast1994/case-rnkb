package com.cognive.projects.casernkb.model;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class CaseRulesDto implements Serializable {
    private Long paymentId;
    private String caseType;
    private List<String> rules = new ArrayList<>();
}
