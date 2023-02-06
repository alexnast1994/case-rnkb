package com.cognive.projects.casernkb.model.projection;

import com.prime.db.rnkb.model.BaseDictionary;

import java.time.LocalDateTime;

public interface CaseProjection {

    BaseDictionary getTypeList();

    LocalDateTime getCreationDate();

}
