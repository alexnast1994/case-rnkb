import com.cognive.projects.casernkb.model.projection.FieldAgg
import com.cognive.projects.casernkb.repo.FiledAggGeneralRepo
import com.prime.db.rnkb.model.BaseDictionary
import com.prime.db.rnkb.model.Rule
import com.prime.db.rnkb.model.commucation.judgment.RjClient
import com.prime.db.rnkb.model.commucation.judgment.RjFieldAggregate

import java.lang.reflect.Field

Long clientId = execution.getVariable("clientId") as Long
String dateStart = execution.getVariable("startDate") as String
String dateEnd = execution.getVariable("offDate") as String
println clientId
println dateStart.substring(0, 10)
println dateEnd.substring(0, 10)
List<FieldAgg> getFieldAggs(Long clientId, String dateStart, String dateEnd) {
    filedAggGeneralRepo.getFieldAgg(clientId, dateStart.substring(0, 10), dateEnd.substring(0, 10))
}

Rule getRuleById(Long aggrId) {
    ruleRepo.getRule(aggrId)
}

BaseDictionary getBd(Long id) {
    baseDictRepo.getById(id)
}



List<RjFieldAggregate> rjFieldAggregateList = new ArrayList<>()

List<FieldAgg> fieldAggList = getFieldAggs(clientId, dateStart.substring(0, 10), dateEnd.substring(0, 10))

fieldAggList.each {f ->
    RjFieldAggregate rjFieldAggregate = new RjFieldAggregate()
    rjFieldAggregate.rjClientId = execution.getVariable("rjClient") as RjClient
    rjFieldAggregate.type = getBd(f.getLtype())
    rjFieldAggregate.string = f.getString() as String
    rjFieldAggregate.sum = f.lsum as BigDecimal
    rjFieldAggregate.count = f.lcount as Long
    rjFieldAggregate.aggdirid = getRuleById(f.getAggId())

    println rjFieldAggregate.toString()
    rjFieldAggregateList.add(rjFieldAggregate)
}

execution.setVariable("rjFieldAggregateList", rjFieldAggregateList)
