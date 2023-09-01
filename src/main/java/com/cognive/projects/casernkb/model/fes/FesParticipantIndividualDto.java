package com.cognive.projects.casernkb.model.fes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link com.prime.db.rnkb.model.fes.FesParticipantIndividual}
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FesParticipantIndividualDto implements Serializable {
    long id;
    long participantId;
    Long physicalIdentificationFeatureId;
    long eioIdId;
    long beneficiaryId;
    String lastName;
    String firstName;
    String middleName;
    String fullName;
    String inn;
    String snils;
    String healthCardNum;
    String phoneNumber;
    String orgnip;
    Long privatePractitionerTypeId;
    String privatePractitionerRegNum;
    Long identityDocumentFeatureId;
    LocalDateTime birthDate;
    Long citizenshipCountryCodeId;
    Long publicFigureFeatureId;
    List<FesIdentityDocumentGeneralDto> fesIdentityDocumentGenerals;
}