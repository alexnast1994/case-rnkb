import com.prime.db.rnkb.model.ProfileAggGeneral
import com.prime.db.rnkb.model.commucation.judgment.RjAggregate
import com.prime.db.rnkb.model.commucation.judgment.RjClient

import java.time.LocalDate
import java.time.LocalDateTime


Long clientId = execution.getVariable("clientId") as Long
String dateStart = execution.getVariable("startDate") as String
String dateEnd = execution.getVariable("offDate") as String

List<ProfileAggGeneral> getAggrs(Long clientId, LocalDate startDate, LocalDate offDate) {
    aggGeneralRepo.getAggr(clientId, startDate, offDate)
}

List<ProfileAggGeneral> profAggGeneral = getAggrs(clientId, LocalDate.parse(dateStart.substring(0, 10)), LocalDate.parse(dateEnd.substring(0, 10)))
def rjClient = execution.getVariable("rjClient") as RjClient


RjAggregate rjAggregate = new RjAggregate()
println("rjClient " + rjClient.toString())
println(profAggGeneral.toString())
println(profAggGeneral.size())
rjAggregate.rjClientId = rjClient
rjAggregate.turnoverDt = profAggGeneral == null || profAggGeneral.isEmpty() || !profAggGeneral.any {it -> it.profAgr1 != null} ? 0 : profAggGeneral.findAll {it -> it.profAgr1 != null}.collect().sum { p -> p.profAgr1 } as BigDecimal
rjAggregate.turnoverKt = profAggGeneral == null || profAggGeneral.isEmpty() || !profAggGeneral.any {it -> it.profAgr2 != null} ? 0 : profAggGeneral.findAll {it -> it.profAgr2 != null}.collect().sum { p -> p.profAgr2 } as BigDecimal
rjAggregate.turnoverDtClean = profAggGeneral == null || profAggGeneral.isEmpty() || !profAggGeneral.any {it -> it.profAgr5 != null} ? 0 : profAggGeneral.findAll {it -> it.profAgr5 != null}.collect().sum { p -> p.profAgr5 } as BigDecimal
rjAggregate.turnoverKtClean = profAggGeneral == null || profAggGeneral.isEmpty() || !profAggGeneral.any {it -> it.profAgr82 != null} ? 0 : profAggGeneral.findAll {it -> it.profAgr82 != null}.collect().sum { p -> p.profAgr82 } as BigDecimal
rjAggregate.transferToKo = profAggGeneral == null || profAggGeneral.isEmpty() || !profAggGeneral.any {it -> it.profAgr29 != null} ? 0 : profAggGeneral.findAll {it -> it.profAgr29 != null}.collect().sum { p -> p.profAgr29 } as BigDecimal
rjAggregate.transferFromKo = profAggGeneral == null || profAggGeneral.isEmpty() || !profAggGeneral.any {it -> it.profAgr44 != null} ? 0 : profAggGeneral.findAll {it -> it.profAgr44 != null}.collect().sum { p -> p.profAgr44 } as BigDecimal
rjAggregate.taxSum = profAggGeneral == null || profAggGeneral.isEmpty() || !profAggGeneral.any {it -> it.profAgr13 != null} ? 0 : profAggGeneral.findAll {it -> it.profAgr13 != null}.collect().sum { p -> p.profAgr13 } as BigDecimal
rjAggregate.economicActivitySum = profAggGeneral == null || profAggGeneral.isEmpty() || !profAggGeneral.any {it -> it.profAgr33 != null} ? 0 : profAggGeneral.findAll {it -> it.profAgr33 != null}.collect().sum { p -> p.profAgr33 } as BigDecimal
rjAggregate.arrivalSum = profAggGeneral == null || profAggGeneral.isEmpty() || !profAggGeneral.any {it -> it.profAgr5 != null} ? 0 : profAggGeneral.findAll {it -> it.profAgr5 != null}.collect().sum { p -> p.profAgr5 } as BigDecimal
rjAggregate.spendSum = profAggGeneral == null || profAggGeneral.isEmpty() || !profAggGeneral.any {it -> it.profAgr82 != null} ? 0 : profAggGeneral.findAll {it -> it.profAgr82 != null}.collect().sum { p -> p.profAgr82 } as BigDecimal
execution.setVariable("rjAggregates", rjAggregate)
execution.setVariable("emptyProfileAggGeneral", false)
