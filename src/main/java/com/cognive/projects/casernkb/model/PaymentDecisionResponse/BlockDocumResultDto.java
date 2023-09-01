package com.cognive.projects.casernkb.model.PaymentDecisionResponse;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BlockDocumResultDto implements Serializable {
    @JsonProperty("SourceSystem")
    private String sourceSystem;
    @JsonProperty("SourceId")
    private String sourceId;
    @JsonProperty("PaymentReference")
    private String paymentReference;
}
