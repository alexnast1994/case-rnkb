package com.cognive.projects.casernkb.model.fes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.prime.db.rnkb.model.BaseDictionary;
import lombok.Data;

import java.io.Serializable;

/**
 * DTO for {@link com.prime.db.rnkb.model.fes.FesMoneyPlace}
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FesMoneyPlaceDto implements Serializable {
    Long id;
    Long categoryId;
    BaseDictionary moneyPlaceType;
    String terminalNum;
    String bankBic;
    String bankName;
}