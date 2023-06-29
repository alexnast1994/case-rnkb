import com.prime.db.rnkb.model.BaseDictionary
import com.prime.db.rnkb.model.Client
import com.prime.db.rnkb.model.ClientCheckResult
import org.camunda.spin.json.SpinJsonNode

import java.time.LocalDateTime

String module = execution.getVariable("module") as String
def checkResult = execution.getVariable("checkResult") as SpinJsonNode

Client client = execution.getVariable("client")

BaseDictionary getBd(int type, String code) {
    baseDictRepo.getByBaseDictionaryTypeCodeAndCode(type, code);
}

BaseDictionary mt = getBd(298, module)


List<ClientCheckResult> resultList = new ArrayList<>()
Boolean isMatch = checkResult.prop("isMatch").boolValue()


checkResult.elements().each { r ->

    BaseDictionary mr = getBd(214, r.prop("rule").stringValue())
    ClientCheckResult clientCheckResult = new ClientCheckResult()
    clientCheckResult.setClient(client)
    clientCheckResult.setIsMatch(isMatch)
    clientCheckResult.setModuleRule(mr)
    clientCheckResult.setModuleType(mt)
    clientCheckResult.setDecisionDate(checkResult.hasProp("decisionDate") && checkResult.prop("decisionDate") != null ? LocalDateTime.parse(checkResult.prop("decisionDate").stringValue()) : null)
    resultList.add(clientCheckResult)
}
if (resultList != null && resultList.size() > 0) {
    execution.setVariable("clientCheckResult", resultList)
    execution.setVariable("isInsert", true)
}
else {
    execution.setVariable("isInsert", false)
}

