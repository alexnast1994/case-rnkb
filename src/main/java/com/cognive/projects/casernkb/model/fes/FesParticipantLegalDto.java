package com.cognive.projects.casernkb.model.fes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.prime.db.rnkb.model.fes.FesParticipantLegal}
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FesParticipantLegalDto implements Serializable {
    long id;
    long eioId;
    String participantLegalName;
    Long branchFeatureId;
    String inn;
    String kpp;
    String ogrn;
    LocalDateTime registrationDate;
}