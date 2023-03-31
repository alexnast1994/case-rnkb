package temp.rj

import com.cognive.projects.casernkb.model.projection.BankProjection
import com.prime.db.rnkb.model.FieldAggGeneral
import com.prime.db.rnkb.model.commucation.judgment.ReasonedJudgment
import com.prime.db.rnkb.model.commucation.judgment.RjCounterparty
import com.prime.db.rnkb.model.commucation.judgment.RjCounterpartyBank

List<FieldAggGeneral> getClientAggIds(Long clientId, String dateStart, String dateEnd, List<String> inns) {
    filedAggGeneralRepo.getClientAggIds(clientId, dateStart, dateEnd, inns)
}

List<BankProjection> getPayerBanks(List<String> inn) {
    paymentRepo.getPayerBanks(inn)
}

List<BankProjection> getPayeeBanks(List<String> inn) {
    paymentRepo.getPayeeBanks(inn)
}

List<RjCounterparty> getRjCounterparty(ReasonedJudgment rj) {
    counterPartyRepo.getRjCounterparty(rj)
}

Long clientId = execution.getVariable("clientId") as Long
String dateStart = execution.getVariable("startDate") as String
String dateEnd = execution.getVariable("offDate") as String

println("Начало записи банков")
List<RjCounterparty> rjCounterparties = getRjCounterparty(execution.getVariable("reasonedJudgment") as ReasonedJudgment)
println("Инн контрагентов: " + rjCounterparties.collect{it.inn})
List<BankProjection> bankList = new ArrayList<>()
bankList.addAll(getPayerBanks(rjCounterparties.collect { it.inn }))
bankList.addAll(getPayeeBanks(rjCounterparties.collect { it.inn }))
println("Количество банков: " + bankList.size())
println("Инн банков: " + bankList.collect {it.inn})
if (bankList != null && !bankList.isEmpty()) {
    def u = bankList.unique {b1, b2 -> b1.bank.id <=> b2.bank.id ?: b1.inn <=> b2.inn}
    List<RjCounterpartyBank> rjCounterpartyBanks = new ArrayList<>()
    u.each { b ->
        RjCounterpartyBank rjCounterpartyBank = new RjCounterpartyBank()
        try {

            rjCounterpartyBank.counterpartyId = rjCounterparties.find { r -> r.inn == b.inn }
            rjCounterpartyBank.name = b.bank.name
            rjCounterpartyBank.bic = b.bank.bic
            rjCounterpartyBanks.addAll(rjCounterpartyBank)
        }
        catch (Exception e) {
            println "Не удалсь записать rjCounterpartyBank для банка: " + b.toString()
        }
    }
    if (!rjCounterpartyBanks.isEmpty() && rjCounterpartyBanks != null) {
        execution.setVariable("rjCounterpartyBanks",rjCounterpartyBanks)
        execution.setVariable("emptyBank", false)
    }
    else {
        println("Не удалось записать ни одного rjCounterpartyBanks")
        execution.setVariable("emptyBank", true)
    }
}
else {
    println "Не удалось найти банки для данных ИНН"
    execution.setVariable("emptyBank", true)
}



