package com.cognive.projects.casernkb.model.zk_request;

import com.cognive.projects.casernkb.config.jackson.serialization.LocalDateTimeSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Request {
    @JsonProperty("RequestId")
    private String RequestId;
    @JsonProperty("RequestTema")
    private String RequestTema;
    @JsonProperty("RequestText")
    private String RequestText;
    @JsonProperty("DateOfFormation")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime DateOfFormation;
    @JsonProperty("DateOfResponse")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime DateOfResponse;
    @JsonProperty("ClientId")
    private String ClientId;
    @JsonProperty("FullName")
    private String FullName;
    @JsonProperty("Attachment")
    private List<Attachment> attachment;
}
