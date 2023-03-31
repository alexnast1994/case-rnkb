import com.cognive.projects.casernkb.model.projection.CaseProjection
import com.prime.db.rnkb.model.commucation.judgment.RjClient
import com.prime.db.rnkb.model.commucation.judgment.RjKyc

import java.time.LocalDateTime

List<CaseProjection> getCaseKyc(Long clientId, LocalDateTime dateStart, LocalDateTime dateEnd) {
    caseRepo.getCaseKyc(clientId, dateStart, dateEnd)
}

def rjClient = execution.getVariable("rjClient") as RjClient
Long clientId = execution.getVariable("clientId") as Long
String dateStart = execution.getVariable("startDate") as String
String dateEnd = execution.getVariable("offDate") as String

List<CaseProjection> caseProjections = getCaseKyc(clientId, LocalDateTime.parse(dateStart), LocalDateTime.parse(dateEnd))
List<RjKyc> kycList = new ArrayList<>()
if (!caseProjections.isEmpty() && caseProjections != null) {
    caseProjections.each {c ->
        RjKyc rjKyc = new RjKyc()
        rjKyc.rjclient = rjClient
        rjKyc.kycName = c.typeList
        rjKyc.startdate = c.creationDate
        kycList.add(rjKyc)
    }
    execution.setVariable("rjkycList",kycList)
    execution.setVariable("emptyCaseKyc", false)
}
else {
    println("Не удалось найти подходящие кейсы kyc")
    execution.setVariable("emptyCaseKyc", true)
}