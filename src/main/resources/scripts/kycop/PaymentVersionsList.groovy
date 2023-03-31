import com.prime.db.rnkb.model.BaseDictionary
import com.prime.db.rnkb.model.PaymentParticipantList
import org.camunda.spin.json.SpinJsonNode

import java.time.LocalDateTime

BaseDictionary getBd(int type, String code) {
    baseDictRepo.getByBaseDictionaryTypeCodeAndCode(type, code);
}

List<PaymentParticipantList> participantListList = execution.getVariable("participantListListOut") as List<PaymentParticipantList>
def record = execution.getVariable("record") as SpinJsonNode
List<com.prime.db.rnkb.model.PaymentVersionsList> paymentVersionsLists = new ArrayList<>()


participantListList.findAll {p -> p.getTypeList() == getBd(214, record.prop("listType").stringValue())}.each { it ->

    if (record.hasProp("versionsList") && record.prop("versionsList") != null && record.prop("versionsList").elements().size() > 0) {
        record.prop("versionsList").elements().each { name ->
            com.prime.db.rnkb.model.PaymentVersionsList paymentVersionsList = new com.prime.db.rnkb.model.PaymentVersionsList()
            paymentVersionsList.paymentParticipantListId = it
            paymentVersionsList.listIdentifier = name.hasProp("listIdentifier") ? name.prop("listIdentifier").stringValue() : null
            paymentVersionsList.listVersion = name.hasProp("listVersion") ? name.prop("listVersion").stringValue() : null
            paymentVersionsList.versionDate = name.hasProp("versionDate") ? LocalDateTime.parse(name.prop("versionDate").stringValue()) : null
            paymentVersionsList.loadDate = name.hasProp("loadDate") ? LocalDateTime.parse(name.prop("loadDate").stringValue()) : null
            paymentVersionsList.updateDate = name.hasProp("updateDate") ? LocalDateTime.parse(name.prop("updateDate").stringValue()) : null
            paymentVersionsList.isLastVersion = name.hasProp("isLastVersion") ? name.prop("isLastVersion").boolValue() : null
            paymentVersionsLists.add(paymentVersionsList)
        }

    }

}
if (paymentVersionsLists.isEmpty()) {
    println("Не записались VersionsLists")
    execution.setVariable("VersionsListsErr", true)
} else {
    execution.setVariable("VersionsListsErr", false)
    execution.setVariable("paymentVersionsLists", paymentVersionsLists)
}