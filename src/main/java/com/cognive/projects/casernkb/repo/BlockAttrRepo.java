package com.cognive.projects.casernkb.repo;

import com.prime.db.rnkb.model.af.AfBlockAttributesList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BlockAttrRepo extends JpaRepository<AfBlockAttributesList, Long> {

    @Query(value = "select b from AfBlockAttributesList b where b.clientId.id = :clientId and b.modifyDate >= :startDate and b.modifyDate <= :offDate")
    List<AfBlockAttributesList> getBlocks(Long clientId, LocalDateTime startDate, LocalDateTime offDate);

}
