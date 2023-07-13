package com.cognive.projects.casernkb.repo;

import com.cognive.projects.casernkb.model.projection.OkvedProj;
import com.prime.db.rnkb.model.Okved;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OkvedRepo extends JpaRepository<Okved, Long> {

    @Query(value = "select o.code as code, o.name as name from Okved o where o.clientId.inn like :inn and o.isMain = true")
    OkvedProj getOkvedByInn(String inn);



}
