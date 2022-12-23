package com.cognive.projects.casernkb.model.zk_request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ResponseData {
    @JsonProperty("Response")
    private List<Request> Response;
}
