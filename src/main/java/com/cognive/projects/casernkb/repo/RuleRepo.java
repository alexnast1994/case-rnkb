package com.cognive.projects.casernkb.repo;

import com.prime.db.rnkb.model.Rule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RuleRepo extends JpaRepository<Rule,Long> {

    @Query(value = "select r from Rule r where r.id = :ruleId")
    Rule getRule(Long ruleId);
    @Query(value = "select r.id from Rule r where r.code = :code")
    Long getRuleId(String code);

}
