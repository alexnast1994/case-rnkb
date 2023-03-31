package temp.rj

import com.prime.db.rnkb.model.commucation.judgment.ReasonedJudgment
import com.prime.db.rnkb.model.commucation.judgment.Rjzkrequest
import com.prime.db.rnkb.model.commucation.request.Request

import java.time.LocalDateTime

List<Request> getRequests(Long clientId, LocalDateTime dateStart, LocalDateTime dateEnd) {
    zkRequestRepo.getRequests(clientId, dateStart, dateEnd)
}

Long clientId = execution.getVariable("clientId") as Long
String dateStart = execution.getVariable("startDate") as String
String dateEnd = execution.getVariable("offDate") as String

try {
    List<Request> requestList = getRequests(clientId, LocalDateTime.parse(dateStart),LocalDateTime.parse(dateEnd))
    if (!requestList.isEmpty() && requestList != null) {
        execution.setVariable("requestList", requestList)
        List<Rjzkrequest> rjzkrequestList = new ArrayList<>()
        requestList.each {r ->
            Rjzkrequest rjzkrequest = new Rjzkrequest()
            rjzkrequest.rjId = execution.getVariable("reasonedJudgment") as ReasonedJudgment
            rjzkrequest.dateOfFormation = r.dateOfFormation
            rjzkrequest.requestType = r.requestType
            rjzkrequest.fullInf = r.fullInf
            rjzkrequest.overDue = r.overdue
            rjzkrequest.dateOfExecution = r.dateOfExecution
            if (r.answerList != null && r.answerList.size() > 0) {
                rjzkrequest.dateOfResponse = r.answerList.get(0).dateOfResponse
            }
            rjzkrequest.zkRequestId = r
            rjzkrequestList.add(rjzkrequest)
            execution.setVariable("rjzkrequestList", rjzkrequestList)
            execution.setVariable("emptyrequestList", false)
        }
    }
    else {
        execution.setVariable("emptyrequestList", true)
        println("requestList не найдены")
    }
}
catch (Exception e) {
    execution.setVariable("emptyrequestList", true)
    println "Ошибка при получении requestList " + e.localizedMessage
}

