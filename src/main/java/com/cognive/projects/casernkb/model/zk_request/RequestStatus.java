package com.cognive.projects.casernkb.model.zk_request;

import com.cognive.projects.casernkb.config.jackson.serialization.LocalDateTimeSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RequestStatus {
    @JsonProperty("IdRequest")
    private Long IdRequest;
    @JsonProperty("DateOfResponse")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime DateOfResponse;
    @JsonProperty("Status")
    private Boolean Status;
    @JsonProperty("ClientId")
    private String ClientId;
}
