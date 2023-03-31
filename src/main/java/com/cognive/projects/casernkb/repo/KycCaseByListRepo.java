package com.cognive.projects.casernkb.repo;

import com.prime.db.rnkb.model.kyc.KycCaseClientList2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KycCaseByListRepo extends JpaRepository<KycCaseClientList2, Long> {

    @Query(value = "SELECT * FROM KYC_CASE_BY_LIST k WHERE k.CASE_ID in :caseId ", nativeQuery = true)
    List<KycCaseClientList2> getByCaseId(List<Long> caseId);

}
