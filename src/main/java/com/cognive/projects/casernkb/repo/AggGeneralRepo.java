package com.cognive.projects.casernkb.repo;

import com.prime.db.rnkb.model.ProfileAggGeneral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AggGeneralRepo extends JpaRepository<ProfileAggGeneral, Long> {

    @Query(value = "select p from ProfileAggGeneral p where p.client.id = :clientId and p.calculationDate >= :startDate and p.calculationDate <= :offDate")
    List<ProfileAggGeneral> getAggr(Long clientId, LocalDate startDate, LocalDate offDate);

}
