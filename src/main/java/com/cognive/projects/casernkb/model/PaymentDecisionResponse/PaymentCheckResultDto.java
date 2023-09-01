package com.cognive.projects.casernkb.model.PaymentDecisionResponse;

import com.cognive.projects.casernkb.config.jackson.serialization.LocalDateTimeSerializer;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentCheckResultDto implements Serializable {
    @JsonProperty("SourceSystem")
    private String sourceSystem;
    @JsonProperty("SourceId")
    private String sourceId;
    @JsonProperty("PaymentReference")
    private String paymentReference;
    @JsonProperty("CheckStatus")
    private String checkStatus;
    @JsonProperty("RejectType")
    private String rejectType;
    @JsonProperty("RejectDescription")
    private String rejectDescription;
    @JsonProperty("DecisionByUser")
    private String decisionByUser;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonProperty("DecisionDate")
    private LocalDateTime decisionDate;
}
