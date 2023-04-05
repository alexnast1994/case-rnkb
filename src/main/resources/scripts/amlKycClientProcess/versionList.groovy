import com.prime.db.rnkb.model.BaseDictionary
import com.prime.db.rnkb.model.kyc.KycCaseClientList2
import com.prime.db.rnkb.model.kyc.KycCaseClientVersionsList

import java.time.LocalDateTime

import static org.camunda.spin.Spin.JSON

BaseDictionary getBd(int type, String code) {
    baseDictRepo.getByBaseDictionaryTypeCodeAndCode(type, code);
}

def pld = execution.getVariable("payload")
def json = JSON(pld)

List<KycCaseClientList2> kyc2list = execution.getVariable("kycCaseClientList2List")
List<KycCaseClientVersionsList> kycCaseClientVersionsLists = []

def result = json.prop("Clients").elements()[0].prop("Results").elements()
kyc2list.each { k ->
    def record = result.find { r -> r.prop("record").prop("listType").stringValue() == k.getTypeList().getCode()
    }.prop("record")
    if (record.hasProp("versionsList") && record.prop("versionsList") != null && record.prop("versionsList").elements().size() > 0) {
        record.prop("versionsList").elements().each { n ->
            KycCaseClientVersionsList kycCaseClientVersionsList = new KycCaseClientVersionsList()
            kycCaseClientVersionsList.setKycCaseByListId(k)
            kycCaseClientVersionsList.setListIdentifier(n.hasProp("listIdentifier") && n.prop("listIdentifier") != null ? n.prop("listIdentifier").stringValue() : null)
            kycCaseClientVersionsList.setListVersion(n.hasProp("listVersion") && n.prop("listVersion") != null ? n.prop("listVersion").stringValue() : null)
            kycCaseClientVersionsList.setVersionDate(n.hasProp("versionDate") && n.prop("versionDate") != null ? LocalDateTime.parse(n.prop("versionDate").stringValue()) : null )
            kycCaseClientVersionsList.setLoadDate(n.hasProp("loadDate") && n.prop("loadDate") != null ? LocalDateTime.parse(n.prop("loadDate").stringValue()):null)
            kycCaseClientVersionsList.setUpdateDate(n.hasProp("updateDate") && n.prop("updateDate") != null ? LocalDateTime.parse(n.prop("updateDate").stringValue()):null)
            kycCaseClientVersionsList.setIsLastVersion(n.hasProp("isLastVersion") && n.prop("isLastVersion") != null ? n.prop("isLastVersion").boolValue():null)
            kycCaseClientVersionsLists.add(kycCaseClientVersionsList)
        }
    }

}
if (kycCaseClientVersionsLists.isEmpty()) {
    println("Не удалось записать kycCaseClientVersionsLists")
    execution.setVariable("versionErr", true)
}
else {
    execution.setVariable("versionErr", false)
    execution.setVariable("kycCaseClientVersionsLists", kycCaseClientVersionsLists)
}
