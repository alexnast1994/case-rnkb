import com.prime.db.rnkb.model.BaseDictionary
import com.prime.db.rnkb.model.Client
import com.prime.db.rnkb.model.VerificationDocument
import com.prime.db.rnkb.model.kyc.KycCaseClientVerdoc

import java.time.LocalDate

import static org.camunda.spin.Spin.JSON

BaseDictionary getBd(int type, String code) {
    baseDictRepo.getByBaseDictionaryTypeCodeAndCode(type, code);
}

def pld = execution.getVariable("payloadFull")
def json = JSON(pld)

List<KycCaseClientVerdoc> caseClientVerdocList = new ArrayList<>()

if (json.prop("InitialClient").hasProp("RequestData") && json.prop("InitialClient").prop("RequestData").prop("Clients").elements()[0].hasProp("VerificationDocuments") && json.prop("InitialClient").prop("RequestData").prop("Clients").elements()[0].prop("VerificationDocuments").elements().size() > 0) {
    def initialClient = json.prop("InitialClient").prop("RequestData").prop("Clients").elements()[0].prop("VerificationDocuments").elements()
    initialClient.each {it ->
        KycCaseClientVerdoc kycCaseClientVerdoc = new KycCaseClientVerdoc()
        kycCaseClientVerdoc.setKycCaseClientId(execution.getVariable("kyccaseClientOutNew"))
        kycCaseClientVerdoc.setSourceId(it.hasProp("SourceId") ? it.prop("SourceId").stringValue() : null)
        kycCaseClientVerdoc.setIsMain(it.hasProp("IsMain") ? it.prop("IsMain").boolValue() : null)
        kycCaseClientVerdoc.setIssueDate(it.hasProp("IssueDate") ? LocalDate.parse(it.prop("IssueDate").stringValue()): null)
        kycCaseClientVerdoc.setName(it.hasProp("Name") ? it.prop("Name").stringValue() : null)
        kycCaseClientVerdoc.setType(it.hasProp("Type") ? getBd(34, it.prop("Type").stringValue()) : null)
        kycCaseClientVerdoc.setDocNumber(it.hasProp("DocNumber") ? it.prop("DocNumber").stringValue() : null)
        kycCaseClientVerdoc.setIssueByOrganization(it.hasProp("IssueByOrganization") ? it.prop("IssueByOrganization").stringValue() : null)
        kycCaseClientVerdoc.setExpirationDate(it.hasProp("ExpirationDate") ? LocalDate.parse(it.prop("ExpirationDate").stringValue()) : null)
        kycCaseClientVerdoc.setIssueByDepartmentCode(it.hasProp("IssueByDepartmentCode") ? it.prop("IssueByDepartmentCode").stringValue() : null)
        caseClientVerdocList.add(kycCaseClientVerdoc)
    }
    execution.setVariable("kycCaseClientVerdoc", caseClientVerdocList)
    execution.setVariable("emptyVerdoc", false)
}
else {
    def clients = execution.getVariable("client") as List<Client>
    def client = clients.get(0)
    List<VerificationDocument> verificationDocument = client.getVerificationDocumentList()
    println(verificationDocument.size())
    if (verificationDocument.isEmpty()) {
        println("Не найден VerificationDocumentList для клиента " + client.getId())
        execution.setVariable("emptyVerdoc", true)
    }
    else {
        verificationDocument.each {v ->
            KycCaseClientVerdoc kycCaseClientVerdoc = new KycCaseClientVerdoc()
            kycCaseClientVerdoc.setKycCaseClientId(execution.getVariable("kyccaseClient"))
            kycCaseClientVerdoc.setSourceId(v.getExId())
            kycCaseClientVerdoc.setIsMain(v.getIsMain())
            kycCaseClientVerdoc.setIssueDate(v.getIssueDate())
            kycCaseClientVerdoc.setName(v.getName())
            kycCaseClientVerdoc.setType(v.getType())
            kycCaseClientVerdoc.setDocNumber(v.getDocNumber())
            kycCaseClientVerdoc.setIssueByOrganization(v.getIssueByOrganization())
            kycCaseClientVerdoc.setExpirationDate(v.getExpirationDate())
            kycCaseClientVerdoc.setIssueByDepartmentCode(v.getIssueByDepartmentCode())
            caseClientVerdocList.add(kycCaseClientVerdoc)
        }
        execution.setVariable("kycCaseClientVerdoc", caseClientVerdocList)
        execution.setVariable("emptyVerdoc", false)
    }
}

