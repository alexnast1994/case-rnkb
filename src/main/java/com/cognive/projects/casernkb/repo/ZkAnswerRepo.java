package com.cognive.projects.casernkb.repo;

import com.prime.db.rnkb.model.commucation.request.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ZkAnswerRepo extends JpaRepository<Answer, Long> {

    @Query(value = "SELECT * FROM ZK_ANSWERS z where z.REQUEST_ID in :requestIds", nativeQuery = true)
    List<Answer> findByRequestId(List<Long> requestIds);

}
