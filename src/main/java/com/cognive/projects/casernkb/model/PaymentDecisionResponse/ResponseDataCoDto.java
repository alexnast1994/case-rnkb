package com.cognive.projects.casernkb.model.PaymentDecisionResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ResponseDataCoDto implements Serializable {

    @JsonProperty("PaymentCheckResult")
    List<PaymentCheckResultDto> paymentCheckResultDtos;

    @JsonProperty("BlockDocumResult")
    List<BlockDocumResultDto> blockDocumResultDtos;
}
