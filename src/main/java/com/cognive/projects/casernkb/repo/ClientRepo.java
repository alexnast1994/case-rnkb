package com.cognive.projects.casernkb.repo;

import com.prime.db.rnkb.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepo extends JpaRepository<Client, Long> {

    @Query(value = "select c from Client c where c.inn like :inn")
    Client getCounterpartyClientByInn(String inn);

    @Query(value = "select c from Client c where c.exClientId = :exClientId")
    Client findClientByExid(String exClientId);

}
