import com.prime.db.rnkb.model.Case
import com.prime.db.rnkb.model.CaseUser
import com.prime.db.rnkb.model.commucation.judgment.ReasonedJudgment
import com.prime.db.rnkb.model.BaseDictionary

import java.time.LocalDateTime

BaseDictionary getBd(Integer typeCode, String code) {
    baseDictRepo.getByBaseDictionaryTypeCodeAndCode(typeCode, code);
}

ReasonedJudgment judgment = execution.getVariable("reasonedJudgment")

def cases = []
def caseUsers = []
def callProcesses = false

BaseDictionary status = null
BaseDictionary caseStatus = null

if(judgment.typeRj.getCode() == "2" && judgment.typeOfControl.getCode() == "1") {
    status = getBd(131, "7")
    caseStatus = getBd(178, "4")
    callProcesses = true
} else if(judgment.typeRj.getCode() == "2" && judgment.typeOfControl.getCode() == "2") {
    status = getBd(140, "6")
    caseStatus = getBd(179, "4")
} else if(judgment.typeRj.getCode() == "1" && judgment.typeOfControl.getCode() == "2") {
    status = getBd(140, "7")
    caseStatus = getBd(179, "4")
}

judgment.caseReasonedJudgmentsList.each{caseRj ->
    if(caseRj.caseId != null) {
        if(status != null && caseStatus != null) {

            def caseData = caseRj.caseId
            caseData.status = status
            caseData.caseStatus = caseStatus

            def caseUser = new CaseUser()
            caseUser.caseId = caseData
            caseUser.decisionDate = LocalDateTime.now()
            caseUser.status = status

            if(judgment.confirmingDate != null && judgment.approvalDate == null) {
                caseUser.responsible = judgment.assignedTo
            } else if(judgment.confirmingDate != null) {
                caseUser.responsible = judgment.responsibleUser
            }

            caseUsers.add(caseUser)
            cases.add(caseData)
        }
    }
}

execution.setVariable("cases", cases)
execution.setVariable("caseUsers", caseUsers)
execution.setVariable("callProcesses", callProcesses)
