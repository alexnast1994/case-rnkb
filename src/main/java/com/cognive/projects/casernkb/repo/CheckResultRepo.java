package com.cognive.projects.casernkb.repo;

import com.prime.db.rnkb.model.ClientCheckResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CheckResultRepo extends JpaRepository<ClientCheckResult, Long> {

    @Query(value = "SELECT * FROM CLIENT_CHECK_RESULT c WHERE c.CLIENT_ID = :clientId AND c.MODULE_TYPE = :moduleType AND c.MODULE_RULE = :modelRule AND c.END_DATE is NULL AND c.START_DATE is not null and c.IS_MATCH = 1", nativeQuery = true)
    List<ClientCheckResult> existCheckResult(Long clientId, Long moduleType, Long modelRule);

    @Modifying
    @Transactional
    @Query(value = "update ClientCheckResult c set c.checkDate = :checkDate, c.endDate = :decisionDate where c.id = :id")
    void updateCheckResult(LocalDateTime checkDate, LocalDateTime decisionDate, Long id);

}
