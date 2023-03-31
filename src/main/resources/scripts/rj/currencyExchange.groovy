import com.prime.db.rnkb.model.BaseDictionary
import com.prime.db.rnkb.model.ProfileAggGeneral
import com.prime.db.rnkb.model.commucation.judgment.RjAggregate
import com.prime.db.rnkb.model.commucation.judgment.RjAggregateCurrencyExchange

import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

BaseDictionary getQuarter(String code) {
    baseDictRepo.getByBaseDictionaryTypeCodeAndCode(292, code);
}

List<ProfileAggGeneral> getAggrs(Long clientId, LocalDate startDate, LocalDate offDate) {
    aggGeneralRepo.getAggr(clientId, startDate, offDate)
}

Long clientId = execution.getVariable("clientId") as Long
String dateStart = execution.getVariable("startDate") as String
String dateEnd = execution.getVariable("offDate") as String
RjAggregate rjAggregate = execution.getVariable("rjAggregatesOut") as RjAggregate

LocalDate startQ = LocalDate.now().with(LocalDate.now().getMonth().firstMonthOfQuarter()).with(TemporalAdjusters.firstDayOfMonth())
LocalDate endQ = startQ.plusMonths(2).with(TemporalAdjusters.lastDayOfMonth())

List<RjAggregateCurrencyExchange> rjAggregateCurrencyExchangeList = new ArrayList<>()
for (int i = 0; i<4; i++) {

    RjAggregateCurrencyExchange rjAggregateCurrencyExchange = new RjAggregateCurrencyExchange()
    rjAggregateCurrencyExchange.rjAggregateId = rjAggregate
    List<ProfileAggGeneral> agg = getAggrs(clientId, startQ, endQ)
    rjAggregateCurrencyExchange.currExchangeBuyAmount = agg == null || agg.isEmpty() || !agg.any {it -> it.profAgr11 != null} ? 0 : agg.findAll{it -> it.profAgr11 != null}.sum { it -> it.profAgr11} as BigDecimal
    rjAggregateCurrencyExchange.currExchangeSellAmount = agg == null || agg.isEmpty() || !agg.any {it -> it.profAgr12 != null} ? 0 : agg.findAll{it -> it.profAgr12 != null}.sum { it -> it.profAgr12} as BigDecimal
    rjAggregateCurrencyExchange.quaterAgo = getQuarter(i.toString())
    startQ = startQ.minusMonths(3).with(TemporalAdjusters.firstDayOfMonth())
    endQ= endQ.minusMonths(3).with(TemporalAdjusters.lastDayOfMonth())
    rjAggregateCurrencyExchangeList.add(rjAggregateCurrencyExchange)
}
execution.setVariable("rjAggregateCurrencyExchangeList", rjAggregateCurrencyExchangeList)