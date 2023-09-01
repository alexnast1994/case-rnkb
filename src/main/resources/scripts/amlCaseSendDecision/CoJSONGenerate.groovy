import com.cognive.projects.casernkb.model.PaymentDecisionResponse.AmlResponseCoDto
import com.cognive.projects.casernkb.model.PaymentDecisionResponse.ResponseDataCoDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.prime.db.rnkb.model.SysUser

import java.time.LocalDateTime

def blockDocumResultDtos = execution.getVariable("blockDocumResultDtos") as List
def paymentCheckResultDtos = execution.getVariable("paymentCheckResultDtos") as List
SysUser requestUser = execution.getVariable("requestUser")

ResponseDataCoDto responseDataCoDto = new ResponseDataCoDto()
responseDataCoDto.setBlockDocumResultDtos(blockDocumResultDtos)
responseDataCoDto.setPaymentCheckResultDtos(paymentCheckResultDtos)

AmlResponseCoDto out = new AmlResponseCoDto();
out.setId(UUID.randomUUID().toString())
out.setTimeStamp(LocalDateTime.now())
out.setSourceSystemId(1000)
out.setSourceSystem("AML ADVISER")
out.setRequestType("PaymentCheck")
out.setObjectType("Operation")
out.setObjectSubType("Payment")
out.setObjectDesc("ObjectDesc")
out.setObjCreateDate(LocalDateTime.now())
out.setVersion("1.3")
if(requestUser != null) {
    out.setUser(requestUser.getUsername())
}
out.setResponseDataCoDto(responseDataCoDto)

ObjectMapper mapper = new ObjectMapper()
execution.setVariable("messageToKafka", mapper.writeValueAsString(out))
println(mapper.writeValueAsString(out))