package com.cognive.projects.casernkb.repo;

import com.prime.db.rnkb.model.OperationAggGeneral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperationsAggGeneralsRepo extends JpaRepository<OperationAggGeneral, Long> {

    @Query(value = "select o.paymentId.id from OperationAggGeneral o where o.fieldId.id in :ids")
    List<Long> getPaymentIds(List<Long> ids);

}
