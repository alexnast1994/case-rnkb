import com.prime.db.rnkb.model.BaseDictionary
import com.prime.db.rnkb.model.kyc.KycCaseClientList2
import com.prime.db.rnkb.model.kyc.KycList639P
import groovy.json.JsonSlurperClassic

import java.time.LocalDateTime

import static org.camunda.spin.Spin.JSON

def pld = execution.getVariable("payload")
def json = JSON(pld)

BaseDictionary getBd(int type, String code) {
    baseDictRepo.getByBaseDictionaryTypeCodeAndCode(type, code);
}

List<KycCaseClientList2> caseClientList2 = execution.getVariable("kycCaseClientList2List") as List<KycCaseClientList2>
List<KycList639P> kycList639PList = new ArrayList<>()

try {

    def results = json.prop("Clients").elements()[0].prop("Results").elements()


    caseClientList2.each { k ->
        def result = results.find { r -> r.prop("record").prop("listType").stringValue() == k.getTypeList().getCode() }
        if (result != null && result.hasProp("record") && result.prop("record").hasProp("refuseData") && result.prop("record").prop("refuseData").toString() != "{}") {
            def refuseData = result.prop("record").prop("refuseData")
            KycList639P kycList639P = new KycList639P()
            kycList639P.kycCaseByListId = k
            kycList639P.messageDate = refuseData.hasProp("messageDate") && refuseData.prop("messageDate") != null ? LocalDateTime.parse(refuseData.prop("messageDate").stringValue()) : null
            kycList639P.recordType = refuseData.hasProp("recordType") && refuseData.prop("recordType") != null ? getBd(189, refuseData.prop("recordType").stringValue()) : null
            kycList639P.refuseCode = refuseData.hasProp("refuseCode") && refuseData.prop("refuseCode") != null ? refuseData.prop("refuseCode").stringValue() : null
            kycList639P.refuseDate = refuseData.hasProp("refuseDate") && refuseData.prop("refuseDate") != null ? LocalDateTime.parse(refuseData.prop("refuseDate").stringValue()) : null
            kycList639P.refuseCodeReason = refuseData.hasProp("refuseCodeReason") && refuseData.prop("refuseCodeReason") != null ? refuseData.prop("refuseCodeReason").stringValue() : null
            kycList639P.codeOperation = refuseData.hasProp("codeOperation") && refuseData.prop("codeOperation") != null ? refuseData.prop("codeOperation").stringValue() : null
            kycList639P.signOfUnusualOperation = refuseData.hasProp("signOfUnusualOperation") && refuseData.prop("signOfUnusualOperation") != null ? refuseData.prop("signOfUnusualOperation").stringValue() : null
            kycList639PList.add(kycList639P)
        }


    }


    if (!kycList639PList.isEmpty()) {
        execution.setVariable("KycList639PError", false)
        execution.setVariable("kycList639PList", kycList639PList)
        println("Сформированный kycList639PList: " + kycList639PList.toString())
    } else {
        println("Не удалось сформировать KycList639P, отсутствуют ключевые поля")
        execution.setVariable("KycList639PError", true)
    }

}
catch (Exception e) {
    println("Не удалось сформировать KycList639P, отсутствуют ключевые поля")
    println(e.localizedMessage)
    execution.setVariable("KycList639PError", true)
}