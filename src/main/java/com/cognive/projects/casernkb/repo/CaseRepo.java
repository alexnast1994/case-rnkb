package com.cognive.projects.casernkb.repo;

import com.cognive.projects.casernkb.model.projection.CaseProjection;
import com.prime.db.rnkb.model.Case;
import com.prime.db.rnkb.model.QCase;
import com.prime.db.rnkb.repository.IBaseDslRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Yegor Kuzmin (keelfy)
 */
public interface CaseRepo extends IBaseDslRepository<Case, QCase> {

    @Query(value = "SELECT c.* FROM \"CASE\" c " +
            "INNER JOIN KYCCASECLIENTLIST1 l1 ON l1.CASEID = c.ID " +
            "INNER JOIN KYCCASECLIENTLIST2 l2 ON l2.CASEID = c.ID " +
            "WHERE l1.CLIENTID = :clientId " +
            "   AND l2.TYPELIST IN :typeList " +
            "   AND l2.EXID = :exId " +
            "   AND l2.NUM = :num " +
            "   AND c.CREATIONDATE = (" +
            "       SELECT max(c.CREATIONDATE) FROM \"CASE\" c " +
            "       INNER JOIN KYCCASECLIENTLIST1 l1 ON l1.CASEID = c.ID " +
            "       INNER JOIN KYCCASECLIENTLIST2 l2 ON l2.CASEID = c.ID " +
            "       WHERE l1.CLIENTID = :clientId " +
            "           AND l2.TYPELIST IN :typeList " +
            "           AND l2.EXID = :exId " +
            "           AND l2.NUM = :num" +
            "   ) " +
            "ORDER BY c.CREATIONDATE desc", nativeQuery = true)
    List<Case> getLatestCaseByClientIdAndExIdAndNumAndTypeList(@Param("clientId") Long clientId,
                                                               @Param("typeList") List<Long> typeList,
                                                               @Param("exId") String exId,
                                                               @Param("num") String num);


    @Query(value = "SELECT c.* FROM \"CASE\" c " +
            "INNER JOIN KYCCASECLIENTLIST1 l1 ON l1.CASEID = c.ID " +
            "INNER JOIN KYCCASECLIENTLIST2 l2 ON l2.CASEID = c.ID " +
            "WHERE l2.TYPELIST IN :typeList " +
            "   AND l2.EXID = :exId " +
            "   AND c.CREATIONDATE = (" +
            "       SELECT max(c.CREATIONDATE) FROM \"CASE\" c " +
            "       INNER JOIN KYCCASECLIENTLIST1 l1 ON l1.CASEID = c.ID " +
            "       INNER JOIN KYCCASECLIENTLIST2 l2 ON l2.CASEID = c.ID " +
            "       WHERE " +
            "           l2.TYPELIST IN :typeList " +
            "           AND l2.EXID = :exId " +
            "   ) " +
            "ORDER BY c.CREATIONDATE desc", nativeQuery = true)
    List<Case> getLatestCaseByClientIdAndExIdAndNumAndTypeList1(@Param("exId") String exId,
                                                               @Param("typeList") List<Long> typeList);

    @Query(value = "select c.creationdate as creationDate, k2.typeList as typeList from Case c " +
            "inner join KycCaseClientList1 k1 on k1.caseId = c " +
            "inner join KycCaseClientList2 k2 on k2.caseId = k1.caseId  " +
            "where k1.clientId.id = :clientId and c.caseType.code = '5' and c.creationdate >= :dateStart and c.creationdate <= :dateEnd and k2.checkStatus in (1,2)")
    List<CaseProjection> getCaseKyc(Long clientId, LocalDateTime dateStart, LocalDateTime dateEnd);


}
