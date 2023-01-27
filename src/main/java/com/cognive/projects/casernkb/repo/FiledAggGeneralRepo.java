package com.cognive.projects.casernkb.repo;

import com.prime.db.rnkb.model.FieldAggGeneral;
import com.prime.db.rnkb.model.QFieldAggGeneral;
import com.prime.db.rnkb.repository.IBaseDslRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FiledAggGeneralRepo extends IBaseDslRepository<FieldAggGeneral, QFieldAggGeneral> {

    @Query(value = "SELECT " +
            "branch_group, " +
            "abg.NAME AS branch_group_name " +
            "FROM " +
            "(SELECT " +
            "agg_id, " +
            "branch_group, " +
            "max(sum_order) AS group_sum " +
            "FROM (" +
            "SELECT " +
            "   fag.CLIENTID, " +
            "   fag.AGGDIRID AS agg_id, " +
            "   fag.\"TYPE\", " +
            "   fag.STRING AS branch_group, " +
            "   fag.SUM, " +
            "   fag.CALCULATION_DATE, " +
            "   SUM(fag.SUM) OVER(PARTITION BY fag.STRING, fag.AGGDIRID ORDER BY CALCULATION_DATE) sum_order " +
            "FROM FIELD_AGG_GENERAL fag " +
            "JOIN RULE r ON fag.AGGDIRID = r.ID " +
            "JOIN BASEDICTIONARY b ON fag.\"TYPE\" = b.ID " +
            "WHERE " +
            " r.CODE IN ('AGRT098', 'AGRT099') " +
            " AND " +
            " b.CODE = '4' " +
            " AND " +
            " fag.CLIENTID = :clientId " +
            " AND " +
            " fag.CALCULATION_DATE >= TO_DATE(:dateStart, 'YYYY-MM-DD') " +
            " AND " +
            " fag.CALCULATION_DATE <= TO_DATE(:dateEnd, 'YYYY-MM-DD') " +
            " )" +
            " GROUP BY branch_group,agg_id " +
            " ORDER BY GROUP_SUM DESC OFFSET 0 ROWS FETCH NEXT 1 ROWS ONLY) " +
            "JOIN ANTG_BRANCH_GROUP abg ON abg.ID = branch_group ", nativeQuery = true)
    Object[] findBranch(Long clientId, String dateStart, String dateEnd);

/*    @Query(value = "SELECT fag.CLIENTID, " +
            "       fag.AGGDIRID, " +
            "       fag.TYPE, " +
            "       fag.STRING, " +
            "       SUM(fag.SUM) OVER(PARTITION BY fag.AGGDIRID, fag.TYPE, fag.STRING), " +
            "       SUM(fag.COUNT) OVER(PARTITION BY fag.AGGDIRID, fag.TYPE, fag.STRING) " +
            "       FROM FIELD_AGG_GENERAL fag WHERE fag.CALCULATION_DATE >= :dateStart AND fag.CALCULATION_DATE <= :dateEnd AND fag.CLIENTID = :clientId")
    List<Object[]> getFieldAgg(Long clientId, String dateStart, String dateEnd); */


}
