package com.cognive.projects.casernkb.repo;

import com.prime.db.rnkb.model.commucation.midl.ActionsMIDL;
import com.prime.db.rnkb.model.commucation.request.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ZkMiddlRepo extends JpaRepository<ActionsMIDL, Long> {

    @Query(value = "SELECT m.* FROM ZK_ACTIONSMIDL m INNER JOIN ZK_REQUESTS z ON m.ISSUE_ID = z.ZK_TASK_ID WHERE z.ID in :requests", nativeQuery = true)
    List<ActionsMIDL> getActionMidl(List<Long> requests);

}
