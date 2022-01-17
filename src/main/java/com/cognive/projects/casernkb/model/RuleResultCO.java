package com.cognive.projects.casernkb.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.prime.db.rnkb.repository.BaseDictionaryRepository;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RuleResultCO implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("UID")
    private String UID;

    @JsonProperty("PaymentId")
    private Long PaymentId;

    @JsonProperty("Comment")
    private String Comment;

    @JsonProperty("CaseType")
    private String CaseType;

}
