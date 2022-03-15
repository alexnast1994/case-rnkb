package com.cognive.projects.casernkb.model;

import lombok.Data;

@Data
public class CaseStatusDto {
    private boolean isMainTagsChanged = false;
    private boolean isDataChanged = false;
}
