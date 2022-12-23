package com.cognive.projects.casernkb.model.zk_request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class RequestStatusData {
    @JsonProperty("RequestStatus")
    private List<RequestStatus> RequestStatus;
}
