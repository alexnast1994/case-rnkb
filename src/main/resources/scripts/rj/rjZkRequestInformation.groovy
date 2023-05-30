import com.prime.db.rnkb.model.commucation.judgment.RjZkFormRequest
import com.prime.db.rnkb.model.commucation.judgment.RjZkRequestInformation
import com.prime.db.rnkb.model.commucation.judgment.Rjzkrequest
import com.prime.db.rnkb.model.commucation.request.RequestedInformation

RequestedInformation requestedInformation = execution.getVariable("requestInformation") as RequestedInformation
RjZkFormRequest form = execution.getVariable("formRequestOut") as RjZkFormRequest
println(requestedInformation)
List<Rjzkrequest> rjzkrequests = execution.getVariable("rjzkrequestListOut") as List<Rjzkrequest>

RjZkRequestInformation rjZkRequestInformation = new RjZkRequestInformation()
rjZkRequestInformation.rjZkRequestId = rjzkrequests.find { requestedInformation.requestId.id == it.zkRequestId.id }
rjZkRequestInformation.statusOfProviding = requestedInformation.statusOfProviding
rjZkRequestInformation.formOfReqId = form

execution.setVariable("rjZkRequestInformation", rjZkRequestInformation)
