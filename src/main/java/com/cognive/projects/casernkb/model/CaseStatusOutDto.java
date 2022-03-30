package com.cognive.projects.casernkb.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CaseStatusOutDto {
    private String Id;// автоматически
    private String TimeStamp; // автоматически
    private String SourceSystemId; // "1000"
    private String SourceSystem; //	"AML"
    private String RequestType; //  "PaymentCheck"
    private String ObjectType;
    private String Operation;
    private String ObjectSubType;
    private String ObjectDesc;
    private String ObjCreateDate;
    private String User;
    private String Version;
    private String ReplyId;
    private String ErrorCode;
    private String ErrorDescription;
    private String PaymentCheckResult;
    //private String SourceSystem; //	Payment.SOURCESYSTEMS
    private String SourceId; // Payment.EXID
    private String CheckStatus; // Payment.PAYMENTSOURCESTATUS Проставляется код из справочника  45 Статус операции/платежа
    private String RejectType;
    private String RejectDescription;
    private String DecisionByUser;
    private String DecisionDate; // Case.DECISIONDATE
    private String BlockDocumResult;
    //private String SourceSystem; // Payment.SOURCESYSTEMS
}
