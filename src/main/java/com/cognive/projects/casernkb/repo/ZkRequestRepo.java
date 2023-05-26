package com.cognive.projects.casernkb.repo;

import com.prime.db.rnkb.model.FormOfRequest;
import com.prime.db.rnkb.model.commucation.judgment.RjZkRequestInformation;
import com.prime.db.rnkb.model.commucation.midl.ActionsMIDL;
import com.prime.db.rnkb.model.commucation.request.Request;
import com.prime.db.rnkb.model.commucation.request.RequestedInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ZkRequestRepo extends JpaRepository<Request, Long> {

    @Query(value = "select z from Request z inner join Task t on z.zkTaskId.id = t.id join t.clientList cl join cl.taskList where :clientId in cl.id and z.dateOfFormation >= :dateStart and z.dateOfFormation <= :dateEnd")
    List<Request> getRequests(Long clientId, LocalDateTime dateStart, LocalDateTime dateEnd);

    @Query(value = "select ri from RequestedInformation ri inner join Request re on ri.requestId.id = re.id where re.id in :requests")
    List<RequestedInformation> getRequestInformation(List<Long> requests);

}
