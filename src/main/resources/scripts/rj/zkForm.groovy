import com.prime.db.rnkb.model.BaseDictionary
import com.prime.db.rnkb.model.commucation.judgment.RjZkFormRequest
import com.prime.db.rnkb.model.commucation.judgment.Rjzkrequest
import com.prime.db.rnkb.model.commucation.request.Request
import com.prime.db.rnkb.model.commucation.request.RequestedInformation

List<RequestedInformation> getRequestInfo(List<Long> requests) {
    zkRequestRepo.getRequestInformation(requests)
}

List<Request> req = execution.getVariable("requestList") as List<Request>
List<RequestedInformation> requestedInformations = getRequestInfo(req.collect {r -> r.id})
println(requestedInformations)
List<Rjzkrequest> rjzkrequests = execution.getVariable("rjzkrequestListOut") as List<Rjzkrequest>
if (!requestedInformations.isEmpty() && requestedInformations != null) {
    List<RjZkFormRequest> rjZkFormRequests = new ArrayList<>()
    requestedInformations.each {a ->
        if (a.formOfReq != null) {
            RjZkFormRequest formRequest = new RjZkFormRequest()
            formRequest.codInf = a.formOfReq.codInf
            formRequest.info = a.formOfReq.codInf
            formRequest.dateForm = a.formOfReq.dateForm
            rjZkFormRequests.add(formRequest)
        }

    }
    if (!rjZkFormRequests.isEmpty() && rjZkFormRequests.size()>0) {
        execution.setVariable("rjZkFormRequests",rjZkFormRequests)
        execution.setVariable("emptyrjZkFormRequests", false)
    }
    else {
        execution.setVariable("emptyrjZkFormRequests", true)
    }

}
else {
    println("Не найдены rjZkFormRequests")
    execution.setVariable("emptyrjZkFormRequests", true)
}