package com.cognive.projects.casernkb.model;

import com.prime.db.rnkb.model.BaseDictionary;
import com.prime.db.rnkb.model.Payment;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class CaseRulesDto implements Serializable {
    private Payment paymentId;
    private List<BaseDictionary> rules = new ArrayList<>();
}
