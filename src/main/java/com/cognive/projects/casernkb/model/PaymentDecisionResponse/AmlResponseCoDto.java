package com.cognive.projects.casernkb.model.PaymentDecisionResponse;

import com.cognive.projects.casernkb.config.jackson.serialization.LocalDateTimeSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class AmlResponseCoDto implements Serializable {

    @JsonProperty("Id")
    private String id;
    @JsonProperty("TimeStamp")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime timeStamp;
    @JsonProperty("SourceSystemId")
    private Integer sourceSystemId;
    @JsonProperty("SourceSystem")
    private String sourceSystem;
    @JsonProperty("RequestType")
    private String requestType;
    @JsonProperty("ObjectType")
    private String objectType;
    @JsonProperty("ObjectSubType")
    private String objectSubType;
    @JsonProperty("ObjectDesc")
    private String objectDesc;
    @JsonProperty("ObjCreateDate")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime objCreateDate;
    @JsonProperty("User")
    private String user;
    @JsonProperty("Version")
    private String version;
    @JsonProperty("ResponseData")
    private ResponseDataCoDto responseDataCoDto;
}
