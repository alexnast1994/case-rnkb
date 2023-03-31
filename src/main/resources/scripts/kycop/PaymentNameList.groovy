import com.prime.db.rnkb.model.BaseDictionary
import com.prime.db.rnkb.model.PaymentParticipantList
import org.camunda.spin.json.SpinJsonNode

BaseDictionary getBd(int type, String code) {
    baseDictRepo.getByBaseDictionaryTypeCodeAndCode(type, code);
}

List<PaymentParticipantList> participantListList = execution.getVariable("participantListListOut") as List<PaymentParticipantList>
def record = execution.getVariable("record") as SpinJsonNode
List<com.prime.db.rnkb.model.PaymentNameList> paymentNameListList = new ArrayList<>()

println("participantListListOut " + participantListList == null)
println(participantListList)
participantListList.stream().filter({ p -> p.getTypeList() == getBd(214, record.prop("listType").stringValue()) }).each { it ->

    if (record.hasProp("nameList") && record.prop("nameList") != null && record.prop("nameList").elements().size() > 0) {
        record.prop("nameList").elements().each { name ->
            com.prime.db.rnkb.model.PaymentNameList paymentNameList = new com.prime.db.rnkb.model.PaymentNameList()
            paymentNameList.paymentParticipantListId = it
            name.hasProp("nameType") && name.prop("nameType") != null ? paymentNameList.nameType = getBd(280, name.prop("nameType").stringValue()) : null
            paymentNameList.name = name.hasProp("name") ? name.prop("name").stringValue() : null
            paymentNameList.firstName = name.hasProp("firstName") ? name.prop("firstName").stringValue() : null
            paymentNameList.lastName = name.hasProp("lastName") ? name.prop("lastName").stringValue() : null
            paymentNameList.middleName = name.hasProp("middleName") ? name.prop("middleName").stringValue() : null
            paymentNameListList.add(paymentNameList)
        }

    }

}
if (paymentNameListList.isEmpty()) {
    println("Не записались name")
    execution.setVariable("errorName", true)
}
else {
    execution.setVariable("errorName", false)
    execution.setVariable("paymentNameListList", paymentNameListList)
}
