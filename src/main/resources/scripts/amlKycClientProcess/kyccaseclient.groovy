import com.prime.db.rnkb.model.BaseDictionary
import com.prime.db.rnkb.model.Case
import com.prime.db.rnkb.model.Client
import com.prime.db.rnkb.model.kyc.KycCaseClient

import java.time.LocalDate
import java.time.LocalDateTime

import static org.camunda.spin.Spin.JSON

BaseDictionary getBd(int type, String code) {
    baseDictRepo.getByBaseDictionaryTypeCodeAndCode(type, code);
}

def pld = execution.getVariable("payloadFull")
def json = JSON(pld)
try {
    if (json.hasProp("InitialClient")
            && json.prop("InitialClient") != null
            && json.prop("InitialClient").hasProp("RequestData")
            && json.prop("InitialClient").prop("RequestData") != null
            && json.prop("InitialClient").prop("RequestData").hasProp("Clients") && json.prop("InitialClient").prop("RequestData").prop("Clients") != null
            && json.prop("InitialClient").prop("RequestData").prop("Clients").elements().size() > 0) {
        def initialClient = json.prop("InitialClient").prop("RequestData").prop("Clients").elements()
        KycCaseClient kyccaseClient = new KycCaseClient()
        kyccaseClient.setCaseId(execution.getVariable("caseNewOut") as Case)
        kyccaseClient.setSourceId(initialClient[0].prop("SourceId").stringValue())
        def clients = execution.getVariable("client") as List<Client>
        def client = clients.get(0)
        kyccaseClient.setClientId(client)
        kyccaseClient.setBranchCode(initialClient[0].hasProp("BranchCode") ? initialClient[0].prop("BranchCode").stringValue() : null)
        kyccaseClient.setBranchName(initialClient[0].hasProp("BranchName") ? initialClient[0].prop("BranchName").stringValue() : null)
        kyccaseClient.setCManagerName(initialClient[0].hasProp("CManagerName") ? initialClient[0].prop("CManagerName").stringValue() : null)
        kyccaseClient.setClientType(getBd(24, initialClient[0].prop("ClientType").stringValue()))
        kyccaseClient.setFullName(initialClient[0].hasProp("FullName") ? initialClient[0].prop("FullName").stringValue() : null)
        kyccaseClient.setInn(initialClient[0].hasProp("INN") ? initialClient[0].prop("INN").stringValue() : null)
        kyccaseClient.setOgrn(initialClient[0].hasProp("OGRN") ? initialClient[0].prop("OGRN").stringValue() : null)
        kyccaseClient.setOgrnDate(initialClient[0].hasProp("OGRNDate") && initialClient[0].prop("OGRNDate") != null ? LocalDate.parse(initialClient[0].prop("OGRNDate").stringValue()).atStartOfDay()  : null)
        kyccaseClient.setClientMark(initialClient[0].hasProp("ClientMark") && initialClient[0].prop("ClientMark") != null ? getBd(22, initialClient[0].prop("ClientMark").stringValue())  : null)
        kyccaseClient.setKio(initialClient[0].hasProp("KIO") && initialClient[0].prop("KIO") != null ? initialClient[0].prop("KIO").stringValue() : null)

        execution.setVariable("kyccaseClient", kyccaseClient)
        execution.setVariable("clientType", initialClient[0].prop("ClientType").stringValue())
        println(initialClient[0].prop("ClientType").stringValue())
        execution.setVariable("emptyInitClient", false)
    } else {
        KycCaseClient kyccaseClient = new KycCaseClient()
        kyccaseClient.setCaseId(execution.getVariable("caseNewOut") as Case)
        def clients = execution.getVariable("client") as List<Client>
        def client = clients.get(0)
        kyccaseClient.setClientId(client)
        kyccaseClient.setSourceId(client.getExClientId())
        kyccaseClient.branchCode = client.getExBranch()
        kyccaseClient.branchName = client.getBranchName()
        kyccaseClient.setCManagerName(client.getManager())
        kyccaseClient.clientType = client.getClientType()
        kyccaseClient.clientMark = client.getClientMark()
        kyccaseClient.fullName = client.getFullName()
        kyccaseClient.inn = client.getInn()
        kyccaseClient.ogrn = client.getOgrn()
        kyccaseClient.ogrnDate = client.getOgrnDate()
        kyccaseClient.kio = client.getKio()

        execution.setVariable("kyccaseClient", kyccaseClient)
        execution.setVariable("clientType", client.clientType == null ? "" : client.clientType.code)
        execution.setVariable("emptyInitClient", false)
    }
}
catch (Exception e) {
    println("Невозможно сформировать kycCaseClient, недостаточно данных из json")
    println(e.getLocalizedMessage())
    execution.setVariable("emptyInitClient", true)
}
