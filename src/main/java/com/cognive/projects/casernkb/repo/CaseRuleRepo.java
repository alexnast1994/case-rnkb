package com.cognive.projects.casernkb.repo;

import com.prime.db.rnkb.model.CaseRules;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CaseRuleRepo extends JpaRepository<CaseRules, Long> {

    @Query(value = "select c from CaseRules c where c.caseId.id in :ids")
    List<CaseRules> getCaseRules(List<Long> ids);

}
