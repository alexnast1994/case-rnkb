import com.prime.db.rnkb.model.commucation.judgment.RjZkAnswer
import com.prime.db.rnkb.model.commucation.judgment.Rjzkrequest
import com.prime.db.rnkb.model.commucation.request.Answer
import com.prime.db.rnkb.model.commucation.request.Request

List<Answer> findByRequestId(List<Long> requestIds) {
    zkAnswerRepo.findByRequestId(requestIds)
}


List<Request> req = execution.getVariable("requestList") as List<Request>
List<RjZkAnswer> rjZkAnswerList = new ArrayList<>()
List<Rjzkrequest> rjzkrequests = execution.getVariable("rjzkrequestListOut") as List<Rjzkrequest>

List<Answer> answerList = findByRequestId(req.collect {r -> r.id})
if (answerList != null && answerList.size() >0) {
    answerList.each {it ->
        RjZkAnswer rjZkAnswer = new RjZkAnswer()
        rjZkAnswer.rjZkRequestId = rjzkrequests.find {a -> it.requestId.id == a.zkRequestId.id }
        rjZkAnswer.dateOfResponse = it.dateOfResponse
        rjZkAnswerList.add(rjZkAnswer)
    }
    execution.setVariable("rjZkAnswerList",rjZkAnswerList)
    execution.setVariable("emptyrjZkAnswerList",false)
}
else {
    execution.setVariable("emptyrjZkAnswerList",true)
}


