package com.cognive.projects.casernkb.repo;

import com.prime.db.rnkb.model.LstRosstatOkved;
import com.prime.db.rnkb.model.QLstRosstatOkved;
import com.prime.db.rnkb.repository.IBaseDslRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LstRosstatOkvedRepo extends IBaseDslRepository<LstRosstatOkved, QLstRosstatOkved> {

    @Query(value = "SELECT " +
            "BRANCH_GROUP," +
            "CODE_OKVED " +
            "FROM " +
            "(" +
            "SELECT " +
            "b.branch_group_id AS BRANCH_GROUP, " +
            "o.code AS CODE_OKVED " +
            "FROM " +
            " LST_ROSSTAT_OKVED o" +
            " JOIN ANTG_SUBGROUP_OKVED s ON o.id = s.okved_id " +
            " JOIN ANTG_BRANCH_GROUP_SUBGROUP b ON s.subgroup_id = b.subgroup_id " +
            " UNION" +
            " SELECT " +
            " b.branch_group_id AS BRANCH_GROUP," +
            " l2.code AS CODE_OKVED " +
            " FROM " +
            " LST_ROSSTAT_OKVED o" +
            " JOIN LST_ROSSTAT_OKVED l2 ON o.code = substr(l2.code,0,2) " +
            " JOIN ANTG_SUBGROUP_OKVED s ON o.id = s.okved_id " +
            " JOIN ANTG_BRANCH_GROUP_SUBGROUP b ON s.subgroup_id = b.subgroup_id " +
            " ) " +
            "WHERE " +
            " BRANCH_GROUP = :branchGroupId " +
            " AND " +
            " CODE_OKVED = :codeOvked ", nativeQuery = true)
    List<Object[]> findRosstatOkveds(Long branchGroupId, String codeOvked);

}
