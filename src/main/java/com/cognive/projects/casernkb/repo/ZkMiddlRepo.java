package com.cognive.projects.casernkb.repo;

import com.google.common.collect.Lists;
import com.prime.db.rnkb.model.commucation.midl.ActionsMIDL;
import com.prime.db.rnkb.model.commucation.request.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.List;

public interface ZkMiddlRepo extends JpaRepository<ActionsMIDL, Long> {

    @Query(value = "SELECT m.* FROM ZK_ACTIONSMIDL m INNER JOIN ZK_REQUESTS z ON m.ISSUE_ID = z.ZK_TASK_ID WHERE z.ID in :requests", nativeQuery = true)
    List<ActionsMIDL> getActionsMidl(List<Long> requests);

    default List<ActionsMIDL> getActionMidl(List<Long> requests) {
        int maxSize = 999;
        if (requests.size() > maxSize) {
            List<List<Long>> splittedRequestList = Lists.partition(requests, maxSize);
            List<ActionsMIDL> responseList = new ArrayList<>();
            splittedRequestList.forEach(requestListPart -> responseList.addAll(getActionsMidl(requestListPart)));
            return responseList;
        } else {
            return getActionsMidl(requests);
        }
    }
}
