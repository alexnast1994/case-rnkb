import com.prime.db.rnkb.model.BaseDictionary
import com.prime.db.rnkb.model.Case
import com.prime.db.rnkb.model.kyc.KycCaseClient

import java.time.LocalDate
import java.time.LocalDateTime

import static org.camunda.spin.Spin.JSON

BaseDictionary getBd(int type, String code) {
    baseDictRepo.getByBaseDictionaryTypeCodeAndCode(type, code);
}

def pld = execution.getVariable("payloadFull")
def json = JSON(pld)
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
    kyccaseClient.setClientId(execution.getVariable("client"))
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
    println("Невозможно сформировать kycCaseClient, недостаточно данных из json")
    execution.setVariable("emptyInitClient", true)
}