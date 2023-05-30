import com.cognive.projects.casernkb.model.CounterPartyFinal
import com.cognive.projects.casernkb.model.projection.CounterpartyAgg
import com.cognive.projects.casernkb.model.projection.Egrip
import com.cognive.projects.casernkb.model.projection.Egrul
import com.cognive.projects.casernkb.model.projection.OkvedProj
import com.prime.db.rnkb.model.Client
import com.prime.db.rnkb.model.commucation.judgment.ReasonedJudgment
import com.prime.db.rnkb.model.commucation.judgment.RjCounterparty

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit


Long clientId = execution.getVariable("clientId") as Long
String dateStart = execution.getVariable("startDate") as String
String dateEnd = execution.getVariable("offDate") as String

List<CounterpartyAgg> getClientAgg(Long clientId, String dateStart, String dateEnd) {
    filedAggGeneralRepo.getClientAgg(clientId, dateStart, dateEnd)
}

Client getCounterpartyClientByInn(String inn) {
    clientRepo.getCounterpartyClientByInn(inn)
}

Egrul getEgrulByInn(String inn) {
    egrulRepo.getEgrulByInn(inn)
}

OkvedProj getEgrulOkvedByInn(String inn) {
    egrulRepo.getEgrulOkvedByInn(inn)
}

String getEgripByInn(String inn) {
    egripRepo.getEgripByInn(inn)
}

LocalDateTime getDateOgrnip(String inn) {
    egripRepo.getDateOgrnip(inn)
}

OkvedProj getEgripOkvedByInn(String inn) {
    egripRepo.getEgripOkvedByInn(inn)
}

OkvedProj getOkvedByInn(String inn) {
    okvedRepo.getOkvedByInn(inn)
}

Long getRuleId(String code) {
    ruleRepo.getRuleId(code)
}

List<CounterpartyAgg> counterpartyAggList = getClientAgg(clientId, dateStart.substring(0,10), dateEnd.substring(0,10))
if (!counterpartyAggList.isEmpty() && counterpartyAggList != null) {
    List<RjCounterparty> rjCounterparties = new ArrayList<>()
    counterpartyAggList.each {c ->
        try {
            println "Начало формирования контрагента с ИНН: " + c.inn
            RjCounterparty rjCounterparty = new RjCounterparty()
            rjCounterparty.rjId = execution.getVariable("reasonedJudgment") as ReasonedJudgment
            Egrul egrul = getEgrulByInn(c.inn)
            try {
                if (egrul != null) {
                    rjCounterparty.fullName = egrul.fullName
                    rjCounterparty.isNewlyFormed = ChronoUnit.MONTHS.between(LocalDateTime.now(), egrul.dateOgrn) < 18
                }
                else {
                    String name = getEgripByInn(c.inn)
                    LocalDateTime dateOgrnip = getDateOgrnip(c.inn)
                    if (name != null) {
                        rjCounterparty.fullName = name
                        rjCounterparty.isNewlyFormed = ChronoUnit.MONTHS.between(LocalDateTime.now(), dateOgrnip) < 18
                    }
                    else {
                        Client client = getCounterpartyClientByInn(c.inn)
                        rjCounterparty.fullName = client.fullName
                        rjCounterparty.isNewlyFormed = ChronoUnit.MONTHS.between(LocalDateTime.now(), client.ogrnDate) < 18
                    }
                }
            }
            catch (Exception e) {
                println "Не найден клиент по ИНН " + e.localizedMessage
            }
            OkvedProj okvedProj = getEgrulOkvedByInn(c.inn)
            if (okvedProj == null) {
                okvedProj = getEgripOkvedByInn(c.inn)
                if (okvedProj == null) {
                    okvedProj = getOkvedByInn(c.inn)
                }
            }
            try {
                rjCounterparty.okvedCode = okvedProj.code
                rjCounterparty.okvedName = okvedProj.name
            }
            catch (Exception e) {
                println "Не найден оквэд клиента по ИНН " + e.localizedMessage
            }
            rjCounterparty.inn = c.inn
                rjCounterparty.oborotDt = c.sumDt
                rjCounterparty.oborotKt = c.sumKt

            rjCounterparties.add(rjCounterparty)
            println "Добавлен контрагент: " + rjCounterparty.toString()
        }
        catch (Exception e) {
            println "Не удалось сформировать запись по контрагенту с ИНН: " + c.inn
        }
    }
    if (rjCounterparties.isEmpty() || rjCounterparties == null) {
        println "Не удалось сформировать записи по контрагентам, в БД ничего записано не будет"
        execution.setVariable("emptyCounterpartyAgg", true)
    }
    else {
        execution.setVariable("rjCounterparties", rjCounterparties)
        println("Можно вызывать запись в банк")
        execution.setVariable("emptyCounterpartyAgg", false)
    }
}
else {
    println "Нужные записи в CounterpartyAgg не найдены"
    println("Нельзя вызывать запись в банк")
    execution.setVariable("emptyCounterpartyAgg", true)
}