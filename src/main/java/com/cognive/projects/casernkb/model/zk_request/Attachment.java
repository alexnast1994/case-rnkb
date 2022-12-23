package com.cognive.projects.casernkb.model.zk_request;

import com.cognive.projects.casernkb.config.jackson.serialization.LocalDateTimeSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Attachment {
    @JsonProperty("FileReference")
    private String FileReference;
    @JsonProperty("NameOfAttachment")
    private String NameOfAttachment;
    @JsonProperty("UploadDate")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime UploadDate;
}
