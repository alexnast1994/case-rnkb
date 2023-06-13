import com.prime.db.rnkb.model.BaseDictionary
import com.prime.db.rnkb.model.Client
import com.prime.db.rnkb.model.ClientCheckResult
import org.camunda.spin.json.SpinJsonNode

import java.time.LocalDateTime

String exClientId = execution.getVariable("exClientId") as String
String module = execution.getVariable("module") as String
def jsonObject = execution.getVariable("jsonObject") as SpinJsonNode
def checkResult = execution.getVariable("checkResult") as SpinJsonNode

Client client = execution.getVariable("client")

BaseDictionary getBd(int type, String code) {
    baseDictRepo.getByBaseDictionaryTypeCodeAndCode(type, code);
}

List<ClientCheckResult> getCheckResult(Long clientId, Long moduleType, Long modelRule) {
    checkResultRepo.existCheckResult(clientId, moduleType, modelRule)
}

void updateCheckResult(LocalDateTime checkDate, LocalDateTime decisionDate, Long id) {
     checkResultRepo.updateCheckResult(checkDate, decisionDate, id);
}

BaseDictionary mt = getBd(298, module)
BaseDictionary mr = getBd(214, checkResult.prop("rule").stringValue())

List<ClientCheckResult> resultList = getCheckResult(client.getId(), mt.id, mr.id)
Boolean isMatch = checkResult.prop("isMatch").boolValue()
if (resultList.isEmpty() && !isMatch) {

    ClientCheckResult clientCheckResult = new ClientCheckResult()
    clientCheckResult.setClient(client)
    clientCheckResult.setIsMatch(isMatch)
    clientCheckResult.setModuleRule(mr)
    clientCheckResult.setModuleType(mt)
    clientCheckResult.setCheckDate(checkResult.hasProp("checkDate") && checkResult.prop("checkDate") != null ? LocalDateTime.parse(checkResult.prop("checkDate").stringValue()) : null)
    clientCheckResult.setStartDate(checkResult.hasProp("decisionDate") && checkResult.prop("decisionDate") != null ? LocalDateTime.parse(checkResult.prop("decisionDate").stringValue()) : null)
    clientCheckResult.setEndDate(checkResult.hasProp("decisionDate") && checkResult.prop("decisionDate") != null ? LocalDateTime.parse(checkResult.prop("decisionDate").stringValue()) : null)
    execution.setVariable("clientCheckResult", clientCheckResult)
    execution.setVariable("isInsert", true)

}
else if (resultList.isEmpty() && isMatch) {

    ClientCheckResult clientCheckResult = new ClientCheckResult()
    clientCheckResult.setClient(client)
    clientCheckResult.setIsMatch(isMatch)
    clientCheckResult.setModuleRule(mr)
    clientCheckResult.setModuleType(mt)
    clientCheckResult.setCheckDate(checkResult.hasProp("checkDate") && checkResult.prop("checkDate") != null ? LocalDateTime.parse(checkResult.prop("checkDate").stringValue()) : null)
    clientCheckResult.setStartDate(checkResult.hasProp("decisionDate") && checkResult.prop("decisionDate") != null ? LocalDateTime.parse(checkResult.prop("decisionDate").stringValue()) : null)
    execution.setVariable("clientCheckResult", clientCheckResult)
    execution.setVariable("isInsert", true)
}
else if (!resultList.isEmpty() && !isMatch) {
    resultList.each {r ->
        updateCheckResult(checkResult.hasProp("checkDate") && checkResult.prop("checkDate") != null ? LocalDateTime.parse(checkResult.prop("checkDate").stringValue()) : null, checkResult.hasProp("decisionDate") && checkResult.prop("decisionDate") != null ? LocalDateTime.parse(checkResult.prop("decisionDate").stringValue()) : null, r.id)
    }
    execution.setVariable("isInsert", false)\

}
else {
    execution.setVariable("isInsert", false)
}