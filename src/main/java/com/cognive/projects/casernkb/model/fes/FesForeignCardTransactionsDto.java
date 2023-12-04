package com.cognive.projects.casernkb.model.fes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.prime.db.rnkb.model.BaseDictionary;
import lombok.Data;

import java.io.Serializable;

/**
 * DTO for {@link com.prime.db.rnkb.model.fes.FesForeignCardTransactions}
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FesForeignCardTransactionsDto implements Serializable {
    Long id;
    Long categoryId;
    String territoryCode;
    String cardNum;
    String cardHolder;
    BaseDictionary authorizedOfficerOperationFeature;
    String foreignBankName;
    String swift;
}