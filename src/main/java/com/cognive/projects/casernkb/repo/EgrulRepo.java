package com.cognive.projects.casernkb.repo;

import com.cognive.projects.casernkb.model.projection.Egrul;
import com.cognive.projects.casernkb.model.projection.OkvedProj;
import com.prime.db.rnkb.model.egrul.ListEgrulInfoUl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EgrulRepo extends JpaRepository<ListEgrulInfoUl, Long> {

    @Query(value = "select en.fullNameUl as fullName, er.dateOgrn as dateOgrn from ListEgrulInfoUl e left join ListEgrulNameUl en on e.nameUlId = en.id left join ListEgrulRegUl er on e.regUlId = er.id where e.innUl like :inn")
    Egrul getEgrulByInn(String inn);

    @Query(value = "SELECT a.ID, " +
            "a.INN_UL, " +
            "okved.CODE_OKVED as code, " +
            "okved.NAME_OKVED as name " +
            "FROM LST_EGRUL_INFO_UL a " +
            "JOIN LST_EGRUL_INFO_OKVED_TYPE okved ON a.INFO_OKVED_TYPE_ID = okved.ID WHERE a.INN_UL LIKE :inn", nativeQuery = true)
    OkvedProj getEgrulOkvedByInn(String inn);

}
