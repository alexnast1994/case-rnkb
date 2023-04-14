package com.cognive.projects.casernkb.model.KycDecisionResponse;

import com.cognive.projects.casernkb.config.jackson.serialization.LocalDateTimeSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class AmlRequestDto implements Serializable {

    @JsonProperty("Id")
    private String id;
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS"
    )
    @JsonProperty("TimeStamp")
    private Date timeStamp;
    @JsonProperty("SourceSystemId")
    private String sourceSystemId;
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
    private ResponseDataDto responseDataDto;

    public void setTimeStampFromLocalDateTime(LocalDateTime timeStamp) {
        if(timeStamp == null) return;
        this.timeStamp = java.util.Date.from(timeStamp.atZone(ZoneId.systemDefault())
                .toInstant());
    }
}
