package com.cognive.projects.casernkb.repo;

import com.cognive.projects.casernkb.model.projection.BankProjection;
import com.prime.db.rnkb.model.Bank;
import com.prime.db.rnkb.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepo extends JpaRepository<Payment, Long> {

    @Query(value = "select p.payerInn as inn, p.bankPayerId as bank from Payment p where p.payerInn in :inn")
    List<BankProjection> getPayerBanks(List<String> inn);

    @Query(value = "select p.payeeInn as inn, p.bankPayeeId as bank from Payment p where p.payeeInn in :inn")
    List<BankProjection> getPayeeBanks(List<String> inn);

    @Query(value = "select p from Payment p where p.exId = :exId")
    Payment getPaymentByExid(String exId);

}
