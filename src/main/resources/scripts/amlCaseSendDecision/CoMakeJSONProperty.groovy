import com.cognive.projects.casernkb.model.PaymentDecisionResponse.BlockDocumResultDto
import com.cognive.projects.casernkb.model.PaymentDecisionResponse.PaymentCheckResultDto
import com.prime.db.rnkb.model.CaseOperation
import com.prime.db.rnkb.model.SysUser

import java.time.LocalDateTime

def caseIdsTypeThree = execution.getVariable("caseIdsTypeThree") as List

def decisionResult = execution.getVariable("decisionResult") as String
SysUser requestUser = execution.getVariable("requestUser")

def blockDocumResultDtos = []
def paymentCheckResultDtos = []

caseIdsTypeThree.forEach({ aCase ->

    List<CaseOperation> caseOperationList = caseOperationRepo.findAllByCaseId(aCase)
    caseOperationList.forEach {it ->
        BlockDocumResultDto blockDocumResultDto = new BlockDocumResultDto()
        blockDocumResultDto.paymentReference = it.getPaymentId().getPaymentReference()
        blockDocumResultDto.sourceId = it.getPaymentId().getExId()
        blockDocumResultDto.sourceSystem = it.getPaymentId().getSourceSystems() != null ? it.getPaymentId().getSourceSystems().getName() : null

        blockDocumResultDtos.add(blockDocumResultDto)

        PaymentCheckResultDto paymentCheckResultDto = new PaymentCheckResultDto()
        paymentCheckResultDto.paymentReference = it.getPaymentId().getPaymentReference()
        paymentCheckResultDto.sourceId = it.getPaymentId().getExId()
        paymentCheckResultDto.sourceSystem = it.getPaymentId().getSourceSystems() != null ? it.getPaymentId().getSourceSystems().getName() : null
        paymentCheckResultDto.checkStatus = decisionResult
        if(decisionResult == "1") {
            paymentCheckResultDto.rejectType = "2"
            paymentCheckResultDto.rejectDescription = ""
        }
        if(requestUser != null) {
            paymentCheckResultDto.setDecisionByUser(requestUser.getUsername())
        }
        paymentCheckResultDto.setDecisionDate(LocalDateTime.now())

        paymentCheckResultDtos.add(paymentCheckResultDto)
    }
})

execution.setVariable("blockDocumResultDtos", blockDocumResultDtos)
execution.setVariable("paymentCheckResultDtos", paymentCheckResultDtos)