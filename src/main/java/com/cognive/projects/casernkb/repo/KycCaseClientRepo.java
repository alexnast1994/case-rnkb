package com.cognive.projects.casernkb.repo;

import com.prime.db.rnkb.model.kyc.KycCaseClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface KycCaseClientRepo extends JpaRepository<KycCaseClient, Long> {

    @Query(value = "SELECT * FROM KYC_CASE_CLIENT k WHERE k.CASE_ID = :caseId ", nativeQuery = true)
    KycCaseClient getOneByCaseId(Long caseId);
}
