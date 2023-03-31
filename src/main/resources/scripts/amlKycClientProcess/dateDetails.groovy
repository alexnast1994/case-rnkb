import com.prime.db.rnkb.model.kyc.KycCaseClientDateDetails
import com.prime.db.rnkb.model.kyc.KycCaseClientList2
import com.prime.db.rnkb.model.BaseDictionary

import java.time.LocalDateTime

import static org.camunda.spin.Spin.JSON

def pld = execution.getVariable("payload")
def json = JSON(pld)

BaseDictionary getBd(int type, String code) {
    baseDictRepo.getByBaseDictionaryTypeCodeAndCode(type, code);
}

List<KycCaseClientList2> kyc2list = execution.getVariable("kycCaseClientList2List")
List<KycCaseClientDateDetails> kycCaseClientDateDetails = []

def result = json.prop("Clients").elements()[0].prop("Results").elements()
kyc2list.each {k ->
    def record = result.find { r -> r.prop("record").prop("listType").stringValue() == k.getTypeList().getCode()
    }.prop("record")
    if (record.hasProp("dateDetails") && record.prop("dateDetails") != null && record.prop("dateDetails").toString() != "{}") {
        record.prop("dateDetails").elements().each {n ->

            KycCaseClientDateDetails kycCaseClientDateDetails1 = new KycCaseClientDateDetails()
            kycCaseClientDateDetails1.setKycCaseByListId(k)
            kycCaseClientDateDetails1.setDateTypeId(n.hasProp("dateTypeId") && n.prop("dateTypeId") != null ? getBd(282, n.prop("dateTypeId").stringValue()) : null)
            kycCaseClientDateDetails1.dateValue = n.hasProp("dateValue") && !n.prop("dateValue").isNull() ? n.prop("dateValue").stringValue() : ""
            kycCaseClientDateDetails1.day = n.hasProp("day") && n.prop("day") != null ? n.prop("day").toString() : ""
            kycCaseClientDateDetails1.month = n.hasProp("month") && n.prop("month") != null ? n.prop("month").toString() : ""
            kycCaseClientDateDetails1.year = n.hasProp("year") && n.prop("year") != null ? n.prop("year").toString() : ""
            kycCaseClientDateDetails.add(kycCaseClientDateDetails1)

        }
    }

}
if (!kycCaseClientDateDetails.isEmpty()) {
    execution.setVariable("kycCaseClientDateDetails", kycCaseClientDateDetails)
    execution.setVariable("detailsErr", false)
}
else {
    println("Не удалось создать kycCaseClientDateDetails")
    execution.setVariable("detailsErr", true)
}
