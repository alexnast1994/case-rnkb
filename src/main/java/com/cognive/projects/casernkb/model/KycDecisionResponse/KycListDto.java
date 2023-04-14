package com.cognive.projects.casernkb.model.KycDecisionResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class KycListDto implements Serializable {

    @JsonProperty("Id")
    private String id;

    @JsonProperty("ListName")
    private String listName;

    @JsonProperty("CheckStatus")
    private String checkStatus;

    @JsonProperty("LevelBlocking")
    private String levelBlocking;
}
