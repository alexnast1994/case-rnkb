package com.cognive.projects.casernkb.repo;

import com.cognive.projects.casernkb.model.projection.Egrip;
import com.cognive.projects.casernkb.model.projection.OkvedProj;
import com.prime.db.rnkb.model.egrip.ListEgripInfoIndividual;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EgripRepo extends JpaRepository<ListEgripInfoIndividual, Long> {

    @Query(value = "select ef.firstName as firstName, " +
            "ef.lastName as lastName, " +
            "ef.middleName as middleName, " +
            "er.dateOgrnip as dateOgrnip " +
            "from ListEgripFl ef " +
            "inner join ListEgripInfoIndividual e on e.flId.id = ef.id " +
            "inner join ListEgripRegip er on e.regipId.id = er.id " +
            "where e.innFl like :inn")
    Egrip getEgripByInn(String inn);


    @Query(value = "SELECT a.ID, " +
            "a.INN_FL, " +
            "okved.CODE_OKVED as code, " +
            "okved.NAME_OKVED as name " +
            "FROM LST_EGRIP_INFO_INDIVIDUAL a " +
            "JOIN LST_EGRIP_OKVED okved ON a.OKVED_ID = okved.ID WHERE a.INN_FL LIKE :inn", nativeQuery = true)
    OkvedProj getEgripOkvedByInn(String inn);

}
