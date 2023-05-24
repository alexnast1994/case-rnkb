import com.prime.db.rnkb.model.BaseDictionary
import com.prime.db.rnkb.model.commucation.judgment.RjZkActionMidl
import com.prime.db.rnkb.model.commucation.judgment.RjZkFormRequest
import com.prime.db.rnkb.model.commucation.judgment.RjZkRequestInformation
import com.prime.db.rnkb.model.commucation.judgment.Rjzkrequest
import com.prime.db.rnkb.model.commucation.midl.ActionsMIDL
import com.prime.db.rnkb.model.commucation.request.Request
import com.prime.db.rnkb.model.commucation.request.RequestedInformation

List<RequestedInformation> getRequestInfo(List<Long> requests) {
    zkRequestRepo.getRequestInformation(requests)
}

BaseDictionary getJobStatus(String code) {
    baseDictRepo.getByBaseDictionaryTypeCodeAndCode(191, code);
}

RjZkFormRequest saveForm(RjZkFormRequest formRequest) {
    rjFormOfRequestRepo.save(formRequest)
}

List<Request> req = execution.getVariable("requestList") as List<Request>
List<RjZkFormRequest> form = execution.getVariable("rjZkFormRequests") as List<RjZkFormRequest>
List<RequestedInformation> requestedInformations = getRequestInfo(req.collect {r -> r.id})
println(requestedInformations)
List<Rjzkrequest> rjzkrequests = execution.getVariable("rjzkrequestListOut") as List<Rjzkrequest>
if (!requestedInformations.isEmpty() && requestedInformations != null) {
    List<RjZkFormRequest> rjZkFormRequests = new ArrayList<>()
    List<RjZkRequestInformation> rjZkRequestInformations = new ArrayList<>()
    requestedInformations.each {a ->
        RjZkRequestInformation rjZkRequestInformation = new RjZkRequestInformation()
        rjZkRequestInformation.rjZkRequestId = rjzkrequests.find { a.requestId.id == it.zkRequestId.id}
        rjZkRequestInformation.statusOfProviding = a.statusOfProviding
        rjZkRequestInformations.add(rjZkRequestInformation)
    }
    execution.setVariable("rjZkRequestInformations",rjZkRequestInformations)
    execution.setVariable("emptyrjZkRequestInformations", false)
}
else {
    println("Не найдены rjZkRequestInformations")
    execution.setVariable("emptyrjZkRequestInformations", true)
}