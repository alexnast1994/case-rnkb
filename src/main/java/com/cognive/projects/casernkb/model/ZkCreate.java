package com.cognive.projects.casernkb.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class ZkCreate implements Serializable {
    private Long requestId;
    @NotNull(message = "header can not be empty!")
    private Long header;
    private List<Long> requestedInformation = new ArrayList<>();
    @NotNull
    @NotEmpty(message = "conclusion can not be empty!")
    private List<Long> conclusion = new ArrayList<>();
    private List<Long> additionally = new ArrayList<>();
    private List<Long> operations = new ArrayList<>();
    @NotNull(message = "client can not be empty!")
    private Long client;
    @NotNull(message = "requestType can not be empty!")
    private Integer requestType;
    private String lastDate;
}
