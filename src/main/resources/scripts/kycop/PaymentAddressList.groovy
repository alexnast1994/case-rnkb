import com.prime.db.rnkb.model.BaseDictionary
import com.prime.db.rnkb.model.PaymentParticipantList
import org.camunda.spin.json.SpinJsonNode

BaseDictionary getBd(int type, String code) {
    baseDictRepo.getByBaseDictionaryTypeCodeAndCode(type, code);
}

List<PaymentParticipantList> participantListList = execution.getVariable("participantListListOut") as List<PaymentParticipantList>
def record = execution.getVariable("record") as SpinJsonNode
List<com.prime.db.rnkb.model.PaymentAddressList> paymentAddressLists = new ArrayList<>()


participantListList.stream().filter({ p -> p.getTypeList() == getBd(214, record.prop("listType").stringValue()) }).each { it ->


    if (record.hasProp("addressList") && record.prop("addressList") != null && record.prop("addressList").elements().size() > 0) {
        record.prop("addressList").elements().each { name ->
            com.prime.db.rnkb.model.PaymentAddressList paymentAddressList = new com.prime.db.rnkb.model.PaymentAddressList()
            paymentAddressList.paymentParticipantListId = it
            name.hasProp("addressType") && name.prop("addressType") != null ? paymentAddressList.addressType = getBd(281, name.prop("addressType").stringValue()) : null
            paymentAddressList.addressLine = name.hasProp("addressLine") ? name.prop("addressLine").stringValue() : null
            paymentAddressLists.add(paymentAddressList)
        }

    }


}
if (paymentAddressLists.isEmpty()) {
    println("Не записались address")
    execution.setVariable("errorAddr", true)
} else {
    execution.setVariable("errorAddr", false)
    execution.setVariable("paymentAddressLists", paymentAddressLists)
}
