package com.cognive.projects.casernkb.model.fes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.prime.db.rnkb.model.BaseDictionary;
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
    Long participantId;
    Long eioId;
    Long beneficiaryId;
    BaseDictionary addressType;
    String postal;
    BaseDictionary countryCode;
    BaseDictionary okato;
    String district;
    String township;
    String street;
    String house;
    String corpus;
    String room;
    String addressText;
}