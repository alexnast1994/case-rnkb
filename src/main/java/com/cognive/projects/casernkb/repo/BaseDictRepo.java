package com.cognive.projects.casernkb.repo;

import com.prime.db.rnkb.model.BaseDictionary;
import com.prime.db.rnkb.model.QBaseDictionary;
import com.prime.db.rnkb.repository.IBaseDslRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BaseDictRepo  extends IBaseDslRepository<BaseDictionary, QBaseDictionary> {
    @Query("select a from BaseDictionary a where a.type.code = :baseDictionaryTypeCode and a.code = :code and a.isActive = true")
    BaseDictionary getByBaseDictionaryTypeCodeAndCode(@Param("baseDictionaryTypeCode") Integer baseDictionaryTypeCode, @Param("code") String code);

    @Query("select a from BaseDictionary a where a.type.code = :baseDictionaryTypeCode and a.charCode = :charCode and a.isActive = true")
    BaseDictionary getByBaseDictionaryTypeCodeAndCharCode(@Param("baseDictionaryTypeCode") Integer baseDictionaryTypeCode, @Param("charCode") String charCode);

    @Query("select a from BaseDictionary a where a.id = :id")
    BaseDictionary getById(Long id);
}
