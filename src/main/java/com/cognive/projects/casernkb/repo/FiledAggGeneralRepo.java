package com.cognive.projects.casernkb.repo;

import com.cognive.projects.casernkb.model.projection.BranchGroup;
import com.cognive.projects.casernkb.model.projection.CounterpartyAgg;
import com.cognive.projects.casernkb.model.projection.FieldAgg;
import com.prime.db.rnkb.model.FieldAggGeneral;
import com.prime.db.rnkb.model.QFieldAggGeneral;
import com.prime.db.rnkb.repository.IBaseDslRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
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
            "MAX(sum_order) AS group_sum " +
            " FROM (" +
            " SELECT " +
            "   fag.CLIENTID, " +
            "   fag.AGGDIRID AS agg_id, " +
            "   fag.\"TYPE\", " +
            "   fag.STRING AS branch_group, " +
            "   fag.SUM, " +
            "   fag.CALCULATION_DATE, " +
            "   SUM(fag.SUM) OVER( PARTITION BY fag.STRING, fag.AGGDIRID ORDER BY CALCULATION_DATE) sum_order " +
            " FROM FIELD_AGG_GENERAL fag " +
            " JOIN RULE r ON fag.AGGDIRID = r.ID " +
            " JOIN BASEDICTIONARY b ON fag.\"TYPE\" = b.ID " +
            " WHERE " +
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
            " JOIN ANTG_BRANCH_GROUP abg ON abg.ID = TO_NUMBER(branch_group) ", nativeQuery = true)
    BranchGroup findBranch(Long clientId, String dateStart, String dateEnd);

    @Query(value = "SELECT " +
            " aggId," +
            " ltype," +
            " string, " +
            " max(lsum) as lsum," +
            " max(lcount) as lcount " +
            " FROM ( " +
            " SELECT " +
            " fag.CLIENTID, " +
            " fag.AGGDIRID AS aggId, " +
            " fag.\"TYPE\" AS ltype, " +
            " fag.STRING AS string, " +
            " fag.SUM, " +
            " fag.CALCULATION_DATE, " +
            " SUM(fag.SUM) OVER(PARTITION BY fag.\"TYPE\", fag.STRING, fag.AGGDIRID ORDER BY CALCULATION_DATE) lsum," +
            " SUM(fag.COUNT) OVER(PARTITION BY fag.\"TYPE\", fag.STRING, fag.AGGDIRID ORDER BY CALCULATION_DATE) lcount " +
            " FROM FIELD_AGG_GENERAL fag " +
            " JOIN RULE r ON fag.AGGDIRID = r.ID " +
            " JOIN BASEDICTIONARY b ON fag.\"TYPE\" = b.ID " +
            " WHERE " +
            " fag.CLIENTID = :clientId " +
            " AND " +
            " fag.CALCULATION_DATE >= TO_DATE(:dateStart, 'YYYY-MM-DD') " +
            " AND " +
            " fag.CALCULATION_DATE <= TO_DATE(:dateEnd, 'YYYY-MM-DD') " +
            " ) " +
            "GROUP BY string,aggId,ltype", nativeQuery = true)
    List<FieldAgg> getFieldAgg(Long clientId, String dateStart, String dateEnd);

    @Query(value = "SELECT distinct fag.STRING as inn, " +
            "       SUM(case when fag.AGGDIRID = (SELECT ID FROM RULE WHERE CODE = 'AGRT096') THEN fag.SUM ELSE 0 END) OVER ( PARTITION BY fag.TYPE, fag.STRING ORDER BY fag.STRING) as sumDt, " +
            "       SUM(case when fag.AGGDIRID = (SELECT ID FROM RULE WHERE CODE = 'AGRT097') THEN fag.SUM ELSE 0 END) OVER ( PARTITION BY fag.TYPE, fag.STRING ORDER BY fag.STRING) as sumKt " +
            "FROM FIELD_AGG_GENERAL fag " +
            "WHERE fag.CLIENTID = :clientId " +
            "  AND fag.AGGDIRID in (SELECT ID FROM RULE WHERE CODE IN ('AGRT096', 'AGRT097')) " +
            "  AND fag.TYPE = (SELECT b.ID " +
            "                  FROM BASEDICTIONARY b " +
            "                  WHERE b.CODE = '1' " +
            "                    AND b.TYPE = (SELECT ID FROM BASEDICTIONARYTYPE WHERE CODE = 1004)) " +
            "  AND fag.CALCULATION_DATE >= TO_DATE(:dateStart, 'YYYY-MM-DD') " +
            "  AND fag.CALCULATION_DATE <= TO_DATE(:dateEnd, 'YYYY-MM-DD') " +
            "ORDER BY fag.STRING" +
            " ", nativeQuery = true)
    List<CounterpartyAgg> getClientAgg(Long clientId, String dateStart, String dateEnd);


    @Query(value = "SELECT distinct * " +
            "FROM FIELD_AGG_GENERAL fag " +
            "WHERE fag.CLIENTID = :clientId " +
            "AND fag.STRING in :inns  " +
            "AND fag.AGGDIRID in (SELECT ID FROM RULE WHERE CODE IN ('AGRT096', 'AGRT097')) " +
            "  AND fag.TYPE = (SELECT b.ID " +
            "                  FROM BASEDICTIONARY b " +
            "                  WHERE b.CODE = '1' AND b.TYPE = (SELECT ID FROM BASEDICTIONARYTYPE WHERE CODE = 1004)) " +
            "  AND fag.CALCULATION_DATE >= TO_DATE(:dateStart, 'YYYY-MM-DD') " +
            "  AND fag.CALCULATION_DATE <= TO_DATE(:dateEnd, 'YYYY-MM-DD')" +
            " ", nativeQuery = true)
    List<FieldAggGeneral> getClientAggIds(Long clientId, String dateStart, String dateEnd, List<String> inns);




}
