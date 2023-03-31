import com.prime.db.rnkb.model.BaseDictionary
import com.prime.db.rnkb.model.kyc.KycCaseClientCountryList
import com.prime.db.rnkb.model.kyc.KycCaseClientList2

import static org.camunda.spin.Spin.JSON

def pld = execution.getVariable("payload")
def json = JSON(pld)

BaseDictionary getBd(int type, String code) {
    baseDictRepo.getByBaseDictionaryTypeCodeAndCode(type, code);
}

List<KycCaseClientList2> kyc2list = execution.getVariable("kycCaseClientList2List")
List<KycCaseClientCountryList> kycCaseClientCountryLists = []

def result = json.prop("Clients").elements()[0].prop("Results").elements()

kyc2list.each { k ->
    def record = result.find { r -> r.prop("record").prop("listType").stringValue() == k.getTypeList().getCode()
    }.prop("record")
    if (record.hasProp("countryList") && record.prop("countryList") != null && record.prop("countryList").toString() != "{}") {
        record.prop("countryList").elements().each { n ->
            KycCaseClientCountryList kycCaseClientCountryList = new KycCaseClientCountryList()
            kycCaseClientCountryList.setKycCaseByListId(k)
            kycCaseClientCountryList.countryType = n.hasProp("countryType") && n.prop("countryType") != null ? getBd(284, n.prop("countryType").stringValue()) : null
            kycCaseClientCountryList.countryCode = n.hasProp("countryCode") && n.prop("countryCode") != null ? n.prop("countryCode").stringValue() : null
            kycCaseClientCountryList.countryName = n.hasProp("countryName") && n.prop("countryName") != null ? n.prop("countryName").stringValue() : null
            kycCaseClientCountryLists.add(kycCaseClientCountryList)
        }

    }
}
if (!kycCaseClientCountryLists.isEmpty()) {
    execution.setVariable("kycCaseClientCountryLists", kycCaseClientCountryLists)
    execution.setVariable("countryErr", false)
}
else {
    println("Не удалось создать kycCaseClientCountryLists")
    execution.setVariable("countryErr", true)
}

