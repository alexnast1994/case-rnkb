import com.prime.db.rnkb.model.BaseDictionary
import com.prime.db.rnkb.model.PaymentDateDetails
import com.prime.db.rnkb.model.PaymentParticipantList
import org.camunda.spin.json.SpinJsonNode

BaseDictionary getBd(int type, String code) {
    baseDictRepo.getByBaseDictionaryTypeCodeAndCode(type, code);
}

List<PaymentParticipantList> participantListList = execution.getVariable("participantListListOut") as List<PaymentParticipantList>
def record = execution.getVariable("record") as SpinJsonNode
List<PaymentDateDetails> paymentDateDetailsList = new ArrayList<>()


participantListList.stream().filter({ p -> p.getTypeList() == getBd(214, record.prop("listType").stringValue()) }).each { it ->
    if (record.hasProp("dateDetails") && record.prop("dateDetails") != null && record.prop("dateDetails").elements().size() > 0) {
        record.prop("dateDetails").elements().each { name ->
            PaymentDateDetails paymentDateDetails = new PaymentDateDetails()
            paymentDateDetails.paymentParticipantListId = it
            name.hasProp("dateTypeId") && name.prop("dateTypeId") != null ? paymentDateDetails.dateTypeId = getBd(282, name.prop("dateTypeId").stringValue()) : null
            paymentDateDetails.dateValue = name.hasProp("dateValue") ? name.prop("dateValue").stringValue() : null
            paymentDateDetails.day = name.hasProp("day") ? name.prop("day").toString() : null
            paymentDateDetails.month = name.hasProp("month") ? name.prop("month").toString() : null
            paymentDateDetails.year = name.hasProp("year") ? name.prop("year").toString() : null
            paymentDateDetailsList.add(paymentDateDetails)
        }

    }

}

if (paymentDateDetailsList.isEmpty()) {
    println("Не записались dateDetails")
    execution.setVariable("dateDetailsErr", true)
} else {
    execution.setVariable("dateDetailsErr", false)
    execution.setVariable("paymentDateDetailsList", paymentDateDetailsList)
}
