package com.cognive.projects.casernkb.model.KycDecisionResponse;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    private Integer sourceSystemId;
    @JsonProperty("SourceSystem")
    private String sourceSystem;
    @JsonProperty("RequestType")
    private String requestType;
    @JsonProperty("ObjectType")
    private String objectType;
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
