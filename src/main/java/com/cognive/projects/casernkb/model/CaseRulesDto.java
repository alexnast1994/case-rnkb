package com.cognive.projects.casernkb.model;

import com.prime.db.rnkb.model.BaseDictionary;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class CaseRulesDto implements Serializable {
    private String paymentExId;
    private String caseType;
    private List<BaseDictionary> rules = new ArrayList<>();
}
