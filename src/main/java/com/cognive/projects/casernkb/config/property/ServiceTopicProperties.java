package com.cognive.projects.casernkb.config.property;

import lombok.Data;

import java.util.Map;

@Data
public class ServiceTopicProperties {
    private String headerName;
    private String processName;
    private Map<String, String> mapping;
    private Map<String, String> responseTopicMapping;
}
