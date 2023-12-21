package com.cognive.projects.casernkb.model.fes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.prime.db.rnkb.model.BaseDictionary;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link com.prime.db.rnkb.model.fes.FesOperationInformation}
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FesOperationInformationDto implements Serializable {
    Long id;
    Long categoryId;
    BaseDictionary operationType;
    BaseDictionary operationFeature;
    BaseDictionary currency;
    BigDecimal amount;
    BigDecimal amountNationalCurrency;
    String currencyTransactionAttribute;
}