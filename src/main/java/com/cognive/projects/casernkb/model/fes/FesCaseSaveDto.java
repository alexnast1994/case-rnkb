package com.cognive.projects.casernkb.model.fes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FesCaseSaveDto implements Serializable {

    private String comment;

    private FesDataPrefillDto fesDataPrefill;
    private SysUserDto sysUser;
    private FesCategoryDto fesCategory;
}
