import com.prime.db.rnkb.model.BaseDictionary
import com.prime.db.rnkb.model.VerificationDocument
import com.prime.db.rnkb.model.commucation.judgment.RjClient
import com.prime.db.rnkb.model.commucation.judgment.RjVerificationdoc
import java.time.LocalDateTime


def rjclient = execution.getVariable("rjClient") as RjClient
def verificationDocument = execution.getVariable("verificationDocumentsBase") as List<VerificationDocument>
List<RjVerificationdoc> rjVerificationdocList = new ArrayList<>()

verificationDocument.each { v ->
    def rjVerificationdoc = new RjVerificationdoc()
    rjVerificationdoc.rjclient = rjclient

    if (v != null) {
        rjVerificationdoc.number = v.docNumber
        rjVerificationdoc.issuebyorganization = v.issueByOrganization
        rjVerificationdoc.issuebydepartamentcode = v.issueByDepartmentCode
        def baseDic = v.type as BaseDictionary
        rjVerificationdoc.type = baseDic
        def localDateTime = v.issueDate != null ?  v.issueDate.atTime(0,0,0) as LocalDateTime : null
        rjVerificationdoc.issuedate = localDateTime
        rjVerificationdocList.add(rjVerificationdoc)
    }
}



return rjVerificationdocList
