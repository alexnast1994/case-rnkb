package com.cognive.projects.casernkb.repo;

import com.prime.db.rnkb.model.commucation.judgment.RjZkFormRequest;
import com.prime.db.rnkb.model.commucation.request.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RjFormOfRequestRepo extends JpaRepository<RjZkFormRequest, Long> {
}
