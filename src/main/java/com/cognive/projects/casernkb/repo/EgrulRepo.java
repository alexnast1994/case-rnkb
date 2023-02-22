package com.cognive.projects.casernkb.repo;

import com.cognive.projects.casernkb.model.projection.Egrul;
import com.cognive.projects.casernkb.model.projection.OkvedProj;
import com.prime.db.rnkb.model.egrul.ListEgrulInfoUl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EgrulRepo extends JpaRepository<ListEgrulInfoUl, Long> {

    @Query(value = "select en.fullNameUl as fullName, " +
            "er.dateOgrn as dateOgrn " +
            "from ListEgrulNameUl en " +
            "inner join ListEgrulInfoUl e on e.nameUlId = en.id " +
            "inner join ListEgrulRegUl er on e.regUlId = er.id where e.innUl like :inn")
    Egrul getEgrulByInn(String inn);

    @Query(value = "SELECT a.ID, " +
            "a.INN_UL, " +
            "okved.CODE_OKVED as code, " +
            "okved.NAME_OKVED as name " +
            "FROM LST_EGRUL_INFO_UL a " +
            "JOIN LST_EGRUL_INFO_OKVED_TYPE okved ON a.INFO_OKVED_TYPE_ID = okved.ID WHERE a.INN_UL LIKE :inn", nativeQuery = true)
    OkvedProj getEgrulOkvedByInn(String inn);

    @Query(value = "SELECT GRNDATA.DATE_EGRUL " +
            "FROM LST_EGRUL_INFO_UL UL " +
            "JOIN LST_EGRUL_ADDRESS_UNRELIABILITY ADR_UNR ON ADR_UNR.ADDRESS_UL_ID  = UL.ADDRESS_UL_ID " +
            "JOIN LST_EGRUL_GRNDATA GRNDATA ON GRNDATA.ID = ADR_UNR.GRNDATAID " +
            "WHERE INN_UL = :inn", nativeQuery = true)
    List<Timestamp> getDate(String inn);

    @Query(value = "SELECT ac.SUMM_CAPITAL FROM LST_EGRUL_INFO_UL a " +
            "JOIN LST_EGRUL_AUTHORIZED_CAPITAL ac ON a.AUTHORIZED_CAPITAL_ID = ac.ID " +
            "WHERE a.INN_UL = :inn AND ROWNUM = 1", nativeQuery = true)
    BigDecimal getShareCapital(String inn);


}
