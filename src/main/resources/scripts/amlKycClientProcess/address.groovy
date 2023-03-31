import com.prime.db.rnkb.model.kyc.KycCaseClientAddressList
import com.prime.db.rnkb.model.kyc.KycCaseClientList2
import com.prime.db.rnkb.model.BaseDictionary


import static org.camunda.spin.Spin.JSON

def pld = execution.getVariable("payload")
def json = JSON(pld)

BaseDictionary getBd(int type, String code) {
    baseDictRepo.getByBaseDictionaryTypeCodeAndCode(type, code);
}

List<KycCaseClientList2> kyc2list = execution.getVariable("kycCaseClientList2List")
List<KycCaseClientAddressList> kycCaseClientAddressLists = []

def result = json.prop("Clients").elements()[0].prop("Results").elements()
kyc2list.each {k ->
    def record = result.find { r -> r.prop("record").prop("listType").stringValue() == k.getTypeList().getCode()
    }.prop("record")
    if (record.hasProp("addressList") && record.prop("addressList") != null && record.prop("addressList").elements().size() > 0) {
        record.prop("addressList").elements().each {n ->
            KycCaseClientAddressList kycCaseClientAddressList = new KycCaseClientAddressList()
            kycCaseClientAddressList.setKycCaseByListId(k)
            kycCaseClientAddressList.setAddressType(getBd(281, n.prop("addressType").stringValue()))
            kycCaseClientAddressList.setAddressLine(n.hasProp("addressLine") ? n.prop("addressLine").stringValue() : null)
            kycCaseClientAddressList.countryCode = n.hasProp("countryCode") && n.prop("countryCode") != null ? n.prop("countryCode").stringValue() : null
            kycCaseClientAddressList.country = n.hasProp("country") && n.prop("country") != null ? n.prop("country").stringValue() : null
            kycCaseClientAddressLists.add(kycCaseClientAddressList)
        }
    }

}

if (kycCaseClientAddressLists.isEmpty()) {
    execution.setVariable("addressListErr", true)
    println("Не удалось сформировать kycCaseClientAddressLists")
}
else {
    execution.setVariable("kycCaseClientAddressLists", kycCaseClientAddressLists)
    execution.setVariable("addressListErr", false)
}

