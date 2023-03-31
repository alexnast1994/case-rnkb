import com.prime.db.rnkb.model.BaseDictionary
import com.prime.db.rnkb.model.PaymentIdentityAdrList
import com.prime.db.rnkb.model.PaymentParticipantList
import org.camunda.spin.json.SpinJsonNode

BaseDictionary getBd(int type, String code) {
    baseDictRepo.getByBaseDictionaryTypeCodeAndCode(type, code);
}

List<PaymentParticipantList> participantListList = execution.getVariable("participantListListOut") as List<PaymentParticipantList>
def record = execution.getVariable("record") as SpinJsonNode
List<PaymentIdentityAdrList> paymentIdentityAdrLists = new ArrayList<>()


participantListList.findAll {p -> p.getTypeList() == getBd(214, record.prop("listType").stringValue())}.each { it ->
    if (record.hasProp("identityAtrList") && record.prop("identityAtrList") != null && record.prop("identityAtrList").elements().size() > 0) {
        record.prop("identityAtrList").elements().each { name ->
            PaymentIdentityAdrList paymentIdentityAdrList = new PaymentIdentityAdrList()
            paymentIdentityAdrList.paymentParticipantListId = it
            name.hasProp("idType") && name.prop("idType") != null ? paymentIdentityAdrList.idType = getBd(283, name.prop("idType").stringValue()) : null
            paymentIdentityAdrList.idValue = name.hasProp("idValue") ? name.prop("idValue").stringValue() : null
            paymentIdentityAdrList.idSerial = name.hasProp("idSerial") ? name.prop("idSerial").stringValue() : null
            paymentIdentityAdrList.idNumber = name.hasProp("idNumber") ? name.prop("idNumber").stringValue() : null
            paymentIdentityAdrLists.add(paymentIdentityAdrList)
        }

    }

}

if (paymentIdentityAdrLists.isEmpty()) {
    println("Не записались IdentityAttr")
    execution.setVariable("IdentityAttrErr", true)
} else {
    execution.setVariable("IdentityAttrErr", false)
    execution.setVariable("paymentIdentityAdrLists", paymentIdentityAdrLists)
}