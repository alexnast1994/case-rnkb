package com.cognive.projects.casernkb.repo;


import com.prime.db.rnkb.model.Case;
import com.prime.db.rnkb.model.CaseOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CaseOperationRepo extends JpaRepository<CaseOperation, Long> {

    List<CaseOperation> findAllByCaseId(Case caseId);
}
