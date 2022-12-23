package com.cognive.projects.casernkb.model.zk_request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestData {
    @JsonProperty("Requests")
    private List<Request> Requests;
}
