package com.cognive.projects.casernkb.repo;

import com.cognive.projects.casernkb.model.projection.Egrip;
import com.cognive.projects.casernkb.model.projection.OkvedProj;
import com.prime.db.rnkb.model.egrip.ListEgripInfoIndividual;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface EgripRepo extends JpaRepository<ListEgripInfoIndividual, Long> {

    @Query(value = "SELECT " +
            "    name.LAST_NAME || ' ' || name.FIRST_NAME || ' ' || name.MIDDLE_NAME AS fullName " +
            "FROM LST_EGRIP_INFO_INDIVIDUAL a " +
            "JOIN LST_EGRIP_FL name ON a.FL_ID = name.ID " +
            "WHERE a.INN_FL = :inn ", nativeQuery = true)
    String getEgripByInn(String inn);

    @Query(value = "SELECT " +
            "    reg.DATE_OGRNIP as OGRNDATE " +
            "FROM LST_EGRIP_INFO_INDIVIDUAL a " +
            "JOIN LST_EGRIP_REGIP reg ON a.REGIP_ID = reg.ID " +
            "WHERE a.INN_FL = :inn ", nativeQuery = true)
    LocalDateTime getDateOgrnip(String inn);


    @Query(value = "SELECT a.ID, " +
            "a.INN_FL, " +
            "okved.CODE_OKVED as code, " +
            "okved.NAME_OKVED as name " +
            "FROM LST_EGRIP_INFO_INDIVIDUAL a " +
            "JOIN LST_EGRIP_OKVED okved ON a.OKVED_ID = okved.ID WHERE a.INN_FL LIKE :inn", nativeQuery = true)
    OkvedProj getEgripOkvedByInn(String inn);

}
