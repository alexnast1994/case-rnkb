import com.prime.db.rnkb.model.kyc.KycCaseClientIdentityAtrList
import com.prime.db.rnkb.model.kyc.KycCaseClientList2
import com.prime.db.rnkb.model.BaseDictionary

import static org.camunda.spin.Spin.JSON

def pld = execution.getVariable("payload")
def json = JSON(pld)

BaseDictionary getBd(int type, String code) {
    baseDictRepo.getByBaseDictionaryTypeCodeAndCode(type, code);
}

List<KycCaseClientList2> kyc2list = execution.getVariable("kycCaseClientList2List")
List<KycCaseClientIdentityAtrList> kycCaseClientIdentityAtrLists = []

def result = json.prop("Clients").elements()[0].prop("Results").elements()
kyc2list.each { k ->
    def record = result.find { r -> r.prop("record").prop("listType").stringValue() == k.getTypeList().getCode()
    }.prop("record")
    if (record.hasProp("identityAtrList") && record.prop("identityAtrList") != null && record.prop("identityAtrList").elements().size() > 0) {
        record.prop("identityAtrList").elements().each { n ->
            KycCaseClientIdentityAtrList kycCaseClientIdentityAtrList = new KycCaseClientIdentityAtrList()
            kycCaseClientIdentityAtrList.setKycCaseByListId(k)
            kycCaseClientIdentityAtrList.setIdType(n.hasProp("idType") ? getBd(283, n.prop("idType").stringValue()) : null)
            kycCaseClientIdentityAtrList.setIdValue(n.hasProp("idValue") ? n.prop("idValue").stringValue() : null)
            kycCaseClientIdentityAtrList.setIdSerial(n.hasProp("idSerial") ? n.prop("idSerial").stringValue() : null)
            kycCaseClientIdentityAtrList.setIdNumber(n.hasProp("idNumber") ? n.prop("idNumber").stringValue() : null)
            kycCaseClientIdentityAtrList.setComment(n.hasProp("Comment") ? n.prop("Comment").stringValue() : null)
            kycCaseClientIdentityAtrList.setDateOfDocument(n.hasProp("dateOfDocument") ? n.prop("dateOfDocument").stringValue() : null)
            kycCaseClientIdentityAtrList.setExpiryDateOfDocument(n.hasProp("expiryDateOfDocument") ? n.prop("expiryDateOfDocument").stringValue() : null)
            kycCaseClientIdentityAtrList.comment = n.hasProp("comment") && n.prop("comment") != null ? n.prop("comment").stringValue() : null
            kycCaseClientIdentityAtrList.dateDocument = n.hasProp("dateDocument") && n.prop("dateDocument") != null ? n.prop("dateDocument").stringValue() : null
            kycCaseClientIdentityAtrList.issuingAuthority = n.hasProp("issuingAuthority") && n.prop("issuingAuthority") != null ? n.prop("issuingAuthority").stringValue() : null
            kycCaseClientIdentityAtrList.dateOfDocument = n.hasProp("dateOfDocument") && n.prop("dateOfDocument") != null ? n.prop("dateOfDocument").stringValue() : null
            kycCaseClientIdentityAtrList.expiryDateOfDocument = n.hasProp("expiryDateOfDocument") && n.prop("expiryDateOfDocument") != null ? n.prop("expiryDateOfDocument").stringValue() : null
            kycCaseClientIdentityAtrList.validDoc = n.hasProp("validDoc") && n.prop("validDoc") != null ? n.prop("validDoc").toString() : null
            kycCaseClientIdentityAtrLists.add(kycCaseClientIdentityAtrList)
        }
    }

}
if (kycCaseClientIdentityAtrLists.isEmpty()) {
    println("Не удалось сформировать kycCaseClientIdentityAtrLists")
    execution.setVariable("attrError", true)
}
else {
    execution.setVariable("kycCaseClientIdentityAtrLists", kycCaseClientIdentityAtrLists)
    execution.setVariable("attrError", false)
}
