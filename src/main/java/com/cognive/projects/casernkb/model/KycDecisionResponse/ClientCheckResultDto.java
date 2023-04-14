package com.cognive.projects.casernkb.model.KycDecisionResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ClientCheckResultDto implements Serializable {

    @JsonProperty("SourceSystem")
    private String sourceSystem;
    @JsonProperty("SourceId")
    private String sourceId;
    @JsonProperty("StableId")
    private String stableId;
    @JsonProperty("SessionClientId")
    private String sessionClientId;
    @JsonProperty("DecisionDate")
    private String decisionDate;
    @JsonProperty("DecisionByUser")
    private String decisionByUser;
    @JsonProperty("Comment")
    private String comment;
    @JsonProperty("KYCCheckStatus")
    private boolean kycCheckStatus;
    @JsonProperty("KYCList")
    private List<KycListDto> kycListDtos;
}
