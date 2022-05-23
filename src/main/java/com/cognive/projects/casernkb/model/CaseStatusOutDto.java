package com.cognive.projects.casernkb.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class CaseStatusOutDto {
    @JsonProperty("Id")
    private String Id;// автоматически
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS"
    )
    @JsonProperty("TimeStamp")
    private Date TimeStamp; // автоматически
    @JsonProperty("SourceSystemId")
    private String SourceSystemId; // "1000"
    @JsonProperty("SourceSystem")
    private String SourceSystem; //	"AML"
    @JsonProperty("RequestType")
    private String RequestType; //  "PaymentCheck"
    @JsonProperty("ObjectType")
    private String ObjectType;
    @JsonProperty("Operation")
    private String Operation;
    @JsonProperty("ObjectSubType")
    private String ObjectSubType;
    @JsonProperty("ObjectDesc")
    private String ObjectDesc;
    @JsonProperty("ObjCreateDate")
    private String ObjCreateDate;
    @JsonProperty("User")
    private String User;
    @JsonProperty("Version")
    private String Version;
    @JsonProperty("ReplyId")
    private String ReplyId;
    @JsonProperty("ErrorCode")
    private String ErrorCode;
    @JsonProperty("ErrorDescription")
    private String ErrorDescription;
    @JsonProperty("PaymentCheckResult")
    private String PaymentCheckResult;

    //private String SourceSystem; //	Payment.SOURCESYSTEMS
    @JsonProperty("SourceId")
    private String SourceId; // Payment.EXID
    @JsonProperty("CheckStatus")
    private String CheckStatus; // Payment.PAYMENTSOURCESTATUS Проставляется код из справочника  45 Статус операции/платежа
    @JsonProperty("RejectType")
    private String RejectType;
    @JsonProperty("RejectDescription")
    private String RejectDescription;
    @JsonProperty("DecisionByUser")
    private String DecisionByUser;
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS"
    )
    @JsonProperty("DecisionDate")
    private Date DecisionDate;
    @JsonProperty("BlockDocumResult")
    private String BlockDocumResult;
    //private String SourceSystem; // Payment.SOURCESYSTEMS

    public void setTimeStampFromLocalDateTime(LocalDateTime timeStamp) {
        if(timeStamp == null) return;
        this.TimeStamp = java.util.Date.from(timeStamp.atZone(ZoneId.systemDefault())
                        .toInstant());
    }

    public void setDecisionDateFromLocalDateTime(LocalDateTime decisionDate) {
        if(decisionDate == null) return;
        this.DecisionDate = java.util.Date.from(decisionDate.atZone(ZoneId.systemDefault())
                .toInstant());
    }
}
