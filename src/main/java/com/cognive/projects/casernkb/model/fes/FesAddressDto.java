package com.cognive.projects.casernkb.model.fes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * DTO for {@link com.prime.db.rnkb.model.fes.FesAddress}
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FesAddressDto implements Serializable {
    Long id;
    Long categoryId;
    long participantId;
    long eioId;
    long beneficiaryId;
    Long addressTypeId;
    String postal;
    Long countryCodeId;
    Long okatoId;
    String district;
    String township;
    String street;
    String house;
    String corpus;
    String room;
    String addressText;
}