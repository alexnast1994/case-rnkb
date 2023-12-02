package com.cognive.projects.casernkb.model.fes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.prime.db.rnkb.model.BaseDictionary;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.prime.db.rnkb.model.fes.FesMainPageNew}
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FesMainPageNewDto implements Serializable {
    Long id;
    Long casesStatusIdId;
    LocalDateTime operationDate;
    LocalDateTime caseDate;
    BaseDictionary payerType;
    BaseDictionary payerMark;
    BaseDictionary payeeMark;
    BaseDictionary payeeType;
    BaseDictionary operationStatus;
    String purpose;
    String payerName;
    String payerInn;
    String payeeName;
    String payeeInn;
    String paymentReference;
}