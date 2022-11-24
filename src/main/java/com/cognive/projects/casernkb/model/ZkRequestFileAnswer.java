package com.cognive.projects.casernkb.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ZkRequestFileAnswer {
    private Long requestId;
    private String minioUrl;
    private LocalDateTime dateCreate;
}
