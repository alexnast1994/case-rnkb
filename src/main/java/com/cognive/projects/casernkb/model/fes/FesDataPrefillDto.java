package com.cognive.projects.casernkb.model.fes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * DTO for {@link com.prime.db.rnkb.model.fes.FesDataPrefill}
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FesDataPrefillDto implements Serializable {
    long id;
    String bankRegNum;
    String bankBic;
    String bankOcato;
    String formatVersion;
    String softVersion;
}