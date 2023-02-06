package com.cognive.projects.casernkb.repo;

import com.prime.db.rnkb.model.PaymentParticipantList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentParticipantListRepo extends JpaRepository<PaymentParticipantList, Long> {

}
