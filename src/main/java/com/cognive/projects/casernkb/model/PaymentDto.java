package com.cognive.projects.casernkb.model;

import lombok.Getter;
import lombok.Setter;
import spinjar.com.fasterxml.jackson.annotation.JsonAlias;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class PaymentDto implements Serializable {
    @JsonAlias("ID")
    private Long id;
    @JsonAlias("AMOUNT")
    private BigDecimal amount;
    @JsonAlias("AMOUNTACCOUNTCURRENCY")
    private BigDecimal amountAccountCurrency;
    @JsonAlias("AMOUNTNATIONALCURRENCY")
    private BigDecimal amountNationalCurrency;
    @JsonAlias("BALANCE")
    private BigDecimal balance;
    @JsonAlias("BATCHNUMBER")
    private String batchNumber;
    @JsonAlias("BRANCHID")
    private Long branchId;
    @JsonAlias("CURRENCY")
    private Long currency;
    @JsonAlias("DATEIN")
    private String dateIn;
    @JsonAlias("DATEINSERT")
    private String dateInsert;
    @JsonAlias("DATE_TRANSACTION_ACT")
    private String dateTransactionAct;
    @JsonAlias("DOCCREATEDATE")
    private String docCreateDate;
    @JsonAlias("DOCEDITDATE")
    private String docEditDate;
    @JsonAlias("AMOUNT")
    private String dtAccountNumber;
    @JsonAlias("IDENTITYDOCISSUEDATE")
    private String identityDocIssueDate;
    @JsonAlias("KTACCOUNTNUMBER")
    private String ktAccountNumber;
    @JsonAlias("PAYEEACCOUNTNUMBER")
    private String payeeAccountNumber;
    @JsonAlias("PAYEEINN")
    private String payeeInn;
    @JsonAlias("PAYEENAME")
    private String payeeName;
    @JsonAlias("PAYERACCOUNTNUMBER")
    private String payerAccountNumber;
    @JsonAlias("PAYERCLIENTID")
    private String payerClientId;
    @JsonAlias("PAYERINN")
    private String payerInn;
    @JsonAlias("PAYERNAME")
    private String payerName;
    @JsonAlias("PAYMENTREFERENCE")
    private String paymentReference;
    @JsonAlias("PAYMENTROOTID")
    private String paymentRootId;
    @JsonAlias("PAYMENTSOURCESTATUS")
    private String paymentSourceStatus;
    @JsonAlias("PURPOSE")
    private String purpose;
    @JsonAlias("SELLCURCODE")
    private String sellCurCode;
    @JsonAlias("EXID")
    private String exId;
    @JsonAlias("SOURCESYSTEMS")
    private String sourceSystems;
    @JsonAlias("STATUSUPDATEDATE")
    private String statusUpdateDate;
    @JsonAlias("TYPE")
    private String type;
    @JsonAlias("USERID")
    private String userId;
}
