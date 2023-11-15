import com.prime.db.rnkb.model.BaseDictionary
import com.prime.db.rnkb.model.ClientCheckResult

import java.time.LocalDateTime

import static org.camunda.spin.Spin.JSON

def pld = execution.getVariable("payload")
def json = JSON(pld)
def client = execution.getVariable("client")

BaseDictionary getBd(int type, String code) {
    baseDictRepo.getByBaseDictionaryTypeCodeAndCode(type, code)
}

def kycList = json.prop("ResponseData").prop("ClientCheckResult").elements()[0].prop("KYCList").elements().findAll()
def autoDecision = json.prop("Clients").elements()[0].prop("Results").elements()[0].prop("rulesResult").elements()[0].prop("autoDecision").boolValue()
def listType = json.prop("Clients").elements()[0].prop("Results").elements()[0].prop("record").prop("listType").stringValue()

List<ClientCheckResult> clientCheckResultList = kycList.collect { l ->
    def kycId = l.prop("Id").stringValue()
    new ClientCheckResult(
            client: client,
            moduleType: getBd(298, "16"),
            moduleRule: getBd(214, kycId),
            isMatch: l.prop("CheckStatus").stringValue() == "2",
            checkDate: LocalDateTime.now(),
            decisionDate: autoDecision && listType == kycId ? LocalDateTime.now() : null
    )
}

execution.setVariable("clientCheckResultList", clientCheckResultList)