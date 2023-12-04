package com.cognive.projects.casernkb.model.fes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.prime.db.rnkb.model.fes.FesInspectionDetails}
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FesInspectionDetailsDto implements Serializable {
    Long id;
    Long categoryId;
    LocalDateTime previousInspectionDate;
    LocalDateTime currentInspectionDate;
    Long count30;
    Long count50;
    Long countCommon;
    Long countCommon1;
    Long countCommon2;
    Long countCommon3;
    Long countCommon0;
    Long countCommon01;
    Long countCommon02;
    Long countCommon03;
    Long countLegal;
    Long countLegal1;
    Long countLegal2;
    Long countLegal3;
    Long countLegal0;
    Long countLegal01;
    Long countLegal02;
    Long countLegal03;
    Long countIndividual;
    Long countIndividual1;
    Long countIndividual2;
    Long countIndividual3;
    Long countIndividual0;
    Long countIndividual01;
    Long countIndividual02;
    Long countIndividual03;
}