package com.cognive.projects.casernkb.repo;

import com.cognive.projects.casernkb.model.projection.CaseProjection;
import com.cognive.projects.casernkb.model.projection.KycCaseProjection;
import com.prime.db.rnkb.model.Case;
import com.prime.db.rnkb.model.QCase;
import com.prime.db.rnkb.model.SysUser;
import com.prime.db.rnkb.repository.IBaseDslRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Yegor Kuzmin (keelfy)
 */

public interface CaseRepo extends IBaseDslRepository<Case, QCase> {

    @Query(value = "SELECT c.* FROM \"CASE\" c " +
            "INNER JOIN KYC_CASE_CLIENT l1 ON l1.CASEID = c.ID " +
            "INNER JOIN KYC_CASE_BY_LIST l2 ON l2.CASEID = c.ID " +
            "WHERE l1.CLIENTID = :clientId " +
            "   AND l2.TYPELIST IN :typeList " +
            "   AND l2.EXID = :exId " +
            "   AND l2.NUM = :num " +
            "   AND c.CREATIONDATE = (" +
            "       SELECT max(c.CREATIONDATE) FROM \"CASE\" c " +
            "       INNER JOIN KYC_CASE_CLIENT l1 ON l1.CASEID = c.ID " +
            "       INNER JOIN KYC_CASE_BY_LIST l2 ON l2.CASEID = c.ID " +
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


    @Query(value = "SELECT c.ID as cased, (SELECT code FROM BASEDICTIONARY WHERE ID = l2.TYPE_LIST) as typeList FROM \"CASE\" c " +
            "INNER JOIN KYC_CASE_CLIENT l1 ON l1.CASE_ID = c.ID " +
            "INNER JOIN KYC_CASE_BY_LIST l2 ON l2.CASE_ID = c.ID " +
            "WHERE " +
            "   l1.SOURCE_ID = :exId " +
            "   AND c.CREATIONDATE in (" +
            "       SELECT max(c.CREATIONDATE) over (Partition By l2.TYPE_LIST) FROM \"CASE\" c " +
            "       INNER JOIN KYC_CASE_CLIENT l1 ON l1.CASE_ID = c.ID " +
            "       INNER JOIN KYC_CASE_BY_LIST l2 ON l2.CASE_ID = c.ID " +
            "       WHERE " +
            "            l1.SOURCE_ID = :exId " +
            "   ) " +
            "ORDER BY c.CREATIONDATE desc", nativeQuery = true)
    List<KycCaseProjection> getLatestCaseByClientIdAndExIdAndNumAndTypeList1(@Param("exId") String exId);

    @Query(value = "SELECT * from CASE c where c.ID in :id", nativeQuery = true)
    List<Case> findCasesByIds(List<Long> id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE \"CASE\" c SET c.STATUS = :status WHERE c.ID = :id", nativeQuery = true)
    void updateStatusCase(Long id, Long status);

    @Query(value = "SELECT c.* FROM \"CASE\" c " +
            "INNER JOIN KYC_CASE_CLIENT l1 ON l1.CASE_ID = c.ID " +
            "INNER JOIN KYC_CASE_BY_LIST l2 ON l2.CASE_ID = c.ID " +
            "WHERE l1.SOURCE_ID = :exId " +
            "   AND c.CREATIONDATE in (" +
            "       SELECT max(c.CREATIONDATE) FROM \"CASE\" c " +
            "       INNER JOIN KYC_CASE_CLIENT l1 ON l1.CASE_ID = c.ID " +
            "       INNER JOIN KYC_CASE_BY_LIST l2 ON l2.CASE_ID = c.ID " +
            "       WHERE " +
            "       l1.SOURCE_ID = :exId " +
            "   ) " +
            "ORDER BY c.CREATIONDATE desc", nativeQuery = true)
    List<Case> getLatestCaseByClientIdAndExIdWithoutTypeList(@Param("exId") String exId);

    @Query(value = "select c.creationdate as creationDate, k2.typeList as typeList from Case c " +
            "inner join KycCaseClient k1 on k1.caseId = c " +
            "inner join KycCaseClientList2 k2 on k2.caseId = k1.caseId  " +
            "where k1.clientId.id = :clientId and c.caseType.code = '5' and c.creationdate >= :dateStart and c.creationdate <= :dateEnd and k2.checkStatus in (1,2)")
    List<CaseProjection> getCaseKyc(Long clientId, LocalDateTime dateStart, LocalDateTime dateEnd);

    @Query(value = "SELECT RESPONSIBLEUSER FROM CASE " +
            "WHERE CASETYPE IN (SELECT ID FROM BASEDICTIONARY b WHERE b.CODE = 6 AND b.TYPE = " +
            "(SELECT ID FROM BASEDICTIONARYTYPE bt WHERE bt.CODE = 18)) " +
            "GROUP BY RESPONSIBLEUSER ORDER BY COUNT(id) ASC, RESPONSIBLEUSER ASC " +
            "OFFSET 0 ROWS FETCH NEXT 1 ROWS ONLY", nativeQuery = true)
    Long findFreeResponsibleUser();

}
