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
public class ResponseDataDto implements Serializable {

    @JsonProperty("ClientCheckResult")
    List<ClientCheckResultDto> clientCheckResultDtos;
}
