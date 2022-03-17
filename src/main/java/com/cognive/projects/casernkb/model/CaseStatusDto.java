package com.cognive.projects.casernkb.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CaseStatusDto {
    private boolean mainTagsChanged = false;
    private boolean dataChanged = false;
}
