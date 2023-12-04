package com.cognive.projects.casernkb.model.fes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.prime.db.rnkb.model.BaseDictionary;
import lombok.Data;

import java.io.Serializable;

/**
 * DTO for {@link com.prime.db.rnkb.model.fes.FesMoneyTransfers}
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FesMoneyTransfersDto implements Serializable {
    Long id;
    Long categoryId;
    BaseDictionary transferType;
    BaseDictionary transferStatus;
    BaseDictionary moneyTransferOperatorType;
    String territoryCode;
    String payerAccountNum;
    String payerEspUuid;
    String payerBankBic;
    String payerBankName;
    String payerBankAccount;
    String payerIpAddress;
    String payerMacAddress;
    String payeeAccountNum;
    String payeeEspUuid;
    String payeeBankBic;
    String payeeBankName;
    String payeeBankAccount;
}