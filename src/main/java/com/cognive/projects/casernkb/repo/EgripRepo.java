package com.cognive.projects.casernkb.repo;

import com.cognive.projects.casernkb.model.projection.Egrip;
import com.cognive.projects.casernkb.model.projection.OkvedProj;
import com.prime.db.rnkb.model.egrip.ListEgripInfoIndividual;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EgripRepo extends JpaRepository<ListEgripInfoIndividual, Long> {

    @Query(value = "select ef.firstName as firstName, ef.lastName as lastName, ef.middleName as middleName, er.dateOgrnip as dateOgrnip from ListEgripInfoIndividual e left join ListEgripFl ef on e.flId.id = ef.id left join ListEgripRegip er on e.regipId.id = er.id where e.innFl like :inn")
    Egrip getEgripByInn(String inn);

    @Query(value = "select efo.nameOkved as name, efo.codeOkved as code from ListEgripInfoIndividual e left join ListEgripOkved efo on efo.individualId.id = e.id where e.innFl like :inn and efo.id = e.okvedId.id")
    OkvedProj getEgripOkvedByInn(String inn);

}
