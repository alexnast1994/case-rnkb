package com.cognive.projects.casernkb.model.fes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.prime.db.rnkb.model.BaseDictionary;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.prime.db.rnkb.model.fes.FesFreezingAppliedMeasures}
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FesFreezingAppliedMeasuresDto implements Serializable {
    Long id;
    Long categoryIdId;
    LocalDateTime freezingDate;
    LocalDateTime freezingTime;
    BaseDictionary freezingMoneyType;
    BigDecimal accountAmount;
    BigDecimal accountAmountNationalCurrency;
    BaseDictionary currency;
    BaseDictionary securitiesType;
    BigDecimal securitiesNominalPrice;
    BigDecimal securitiesMarketPrice;
    BaseDictionary reasonType;
    String personCode;
    String accountNum;
}