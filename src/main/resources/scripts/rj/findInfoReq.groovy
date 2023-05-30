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


List<Request> req = execution.getVariable("requestList") as List<Request>
List<RequestedInformation> requestedInformations = getRequestInfo(req.collect {r -> r.id})
println(requestedInformations)
if (!requestedInformations.isEmpty() && requestedInformations != null) {
    execution.setVariable("rjRequestInformations",requestedInformations)
    execution.setVariable("emptyZkRequestInformations", false)
}
else {
    println("Не найдены ZkRequestInformations")
    execution.setVariable("emptyZkRequestInformations", true)
}
