package com.cognive.projects.casernkb.model.fes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * DTO for {@link com.prime.db.rnkb.model.SysUser}
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SysUserDto implements Serializable {
    Long id;
    String name;
    String mobilephone;
    String email;
}