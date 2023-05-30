import com.prime.db.rnkb.model.commucation.judgment.RjZkFormRequest
import com.prime.db.rnkb.model.commucation.request.RequestedInformation



RequestedInformation requestedInformations = execution.getVariable("requestInformation") as RequestedInformation
println(requestedInformations)


RjZkFormRequest formRequest = new RjZkFormRequest()
formRequest.codInf = requestedInformations.formOfReq != null ? requestedInformations.formOfReq.codInf : null
formRequest.info = requestedInformations.formOfReq != null ? requestedInformations.formOfReq.codInf : null
formRequest.dateForm = requestedInformations.formOfReq != null ? requestedInformations.formOfReq.dateForm : null

execution.setVariable("formRequest",formRequest)


