package com.cognive.projects.casernkb.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RuleModelJob {

    @JsonProperty("operation")
    private List<RuleResultCO> operation;
}
