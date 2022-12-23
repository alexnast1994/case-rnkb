package com.cognive.projects.casernkb.model.zk_request;

import com.cognive.projects.casernkb.config.jackson.serialization.LocalDateTimeSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AMLRequestStatus {
    @JsonProperty("Id")
    private String Id;
    @JsonProperty("TimeStamp")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime TimeStamp;
    @JsonProperty("SourceSystemId")
    private Integer SourceSystemId;
    @JsonProperty("SourceSystem")
    private String SourceSystem;
    @JsonProperty("RequestType")
    private String RequestType;
    @JsonProperty("ObjectType")
    private String ObjectType;
    @JsonProperty("ObjectSubType")
    private String ObjectSubType;
    @JsonProperty("ObjectDesc")
    private String ObjectDesc;
    @JsonProperty("ObjCreateDate")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime ObjCreateDate;
    @JsonProperty("User")
    private String User;
    @JsonProperty("Version")
    private String Version;
    @JsonProperty("RequestStatusData")
    private RequestStatusData RequestStatusData;
}
