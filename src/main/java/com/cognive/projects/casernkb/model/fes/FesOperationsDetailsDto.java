package com.cognive.projects.casernkb.model.fes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.prime.db.rnkb.model.BaseDictionary;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link com.prime.db.rnkb.model.fes.FesOperationsDetails}
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FesOperationsDetailsDto implements Serializable {
    Long id;
    Long categoryIdId;
    BaseDictionary espOperationFeature;
    BaseDictionary conversionCurrency;
    BigDecimal amountConversionCurrency;
    String paySystemName1;
    String paySystemName2;
    String espTime;
    String operationCharacteristic;
}