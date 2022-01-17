package com.cognive.projects.casernkb.enums;

public enum CamundaVariables {
    // Case management
    PIPELINE_JSON("operation"),
    ;

    private final String name;

    CamundaVariables(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
