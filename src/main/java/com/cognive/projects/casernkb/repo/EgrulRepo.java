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

    @Query(value = "select eo.nameOkved as name, eo.codeOkved as code from ListEgrulInfoUl e left join ListEgrulInfoOkvedType eo on e.id = eo.infoUlId.id where e.innUl like :inn and eo.id = e.infoOkvedTypeId")
    OkvedProj getEgrulOkvedByInn(String inn);

}
