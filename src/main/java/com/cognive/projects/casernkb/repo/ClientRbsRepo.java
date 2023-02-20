package com.cognive.projects.casernkb.repo;

import com.prime.db.rnkb.model.ClientRbs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRbsRepo extends JpaRepository<ClientRbs, Long> {

    @Query(value = "select cr from ClientRbs cr where cr.client.id = :id")
    List<ClientRbs> getRbs(Long id);

}
