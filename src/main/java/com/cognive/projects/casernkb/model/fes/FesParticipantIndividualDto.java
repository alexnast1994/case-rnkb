package com.cognive.projects.casernkb.model.fes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.prime.db.rnkb.model.BaseDictionary;
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
    Long id;
    Long participantId;
    BaseDictionary physicalIdentificationFeature;
    Long eioIdId;
    Long beneficiaryId;
    String lastName;
    String firstName;
    String middleName;
    String fullName;
    String inn;
    String snils;
    String healthCardNum;
    String phoneNumber;
    String ogrnip;
    BaseDictionary privatePractitionerType;
    String privatePractitionerRegNum;
    BaseDictionary identityDocumentFeature;
    LocalDateTime birthDate;
    BaseDictionary citizenshipCountryCode;
    BaseDictionary publicFigureFeature;
    List<FesIdentityDocumentGeneralDto> fesIdentityDocumentGenerals;
}