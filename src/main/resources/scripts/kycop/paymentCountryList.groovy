import com.prime.db.rnkb.model.BaseDictionary
import com.prime.db.rnkb.model.PaymentCountryList
import com.prime.db.rnkb.model.PaymentDateDetails
import com.prime.db.rnkb.model.PaymentIdentityAdrList
import com.prime.db.rnkb.model.PaymentParticipantList
import org.camunda.spin.json.SpinJsonNode

BaseDictionary getBd(int type, String code) {
    baseDictRepo.getByBaseDictionaryTypeCodeAndCode(type, code);
}

List<PaymentParticipantList> participantListList = execution.getVariable("participantListListOut") as List<PaymentParticipantList>
def record = execution.getVariable("record") as SpinJsonNode
List<PaymentCountryList> paymentCountryLists = new ArrayList<>()


participantListList.findAll {p -> p.getTypeList() == getBd(214, record.prop("listType").stringValue())}.each { it ->
    if (record.hasProp("countryList") && record.prop("countryList") != null && record.prop("countryList").elements().size() > 0) {
        record.prop("countryList").elements().each { name ->
            PaymentCountryList paymentCountryList = new PaymentCountryList()
            paymentCountryList.paymentParticipantListId = it
            paymentCountryList.countryType = name.hasProp("CountryType") && name.prop("CountryType") != null ?  getBd(284, name.prop("CountryType").stringValue()) : null
            paymentCountryList.countryCode = name.hasProp("CountryCode") && name.prop("CountryCode") != null ? name.prop("CountryCode").stringValue() : null
            paymentCountryList.countryName = name.hasProp("CountryName") && name.prop("CountryName") != null ? name.prop("CountryType").stringValue() : null

            paymentCountryLists.add(paymentCountryList)
        }
    }


}
if (paymentCountryLists.isEmpty()) {
    println("Не записались paymentCountryLists")
    execution.setVariable("countryErr", true)
} else {
    execution.setVariable("countryErr", false)
    execution.setVariable("paymentCountryLists", paymentCountryLists)
}
