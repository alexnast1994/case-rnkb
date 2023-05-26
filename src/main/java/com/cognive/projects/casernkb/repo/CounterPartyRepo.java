package com.cognive.projects.casernkb.repo;

import com.prime.db.rnkb.model.commucation.judgment.ReasonedJudgment;
import com.prime.db.rnkb.model.commucation.judgment.RjCounterparty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CounterPartyRepo extends JpaRepository<RjCounterparty, Long> {

    @Query(value = "select rjc from RjCounterparty rjc where rjc.rjId.id = :rj")
    List<RjCounterparty> getRjCounterparty(Long rj);

}
