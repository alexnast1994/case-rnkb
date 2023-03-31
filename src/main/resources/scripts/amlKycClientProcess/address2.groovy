import com.prime.db.rnkb.model.BaseDictionary
import com.prime.db.rnkb.model.kyc.KycCaseClient
import com.prime.db.rnkb.model.kyc.KycCaseClientAddress

import java.time.LocalDate
import java.time.LocalDateTime

import static org.camunda.spin.Spin.JSON

def pld = execution.getVariable("payloadFull")
def json = JSON(pld)

BaseDictionary getBd(int type, String code) {
    baseDictRepo.getByBaseDictionaryTypeCodeAndCode(type, code);
}

List<KycCaseClientAddress> kycAddress = new ArrayList<>()

KycCaseClient kycCaseClient = execution.getVariable("kyccaseClientOutNew") as KycCaseClient

try {
    def addresses = json.prop("InitialClient").prop("RequestData").prop("Clients").elements()[0].prop("Addresses").elements()
    addresses.each {a ->
        KycCaseClientAddress kycCaseClientAddress = new KycCaseClientAddress()
        kycCaseClientAddress.kycCaseByListId = kycCaseClient
        kycCaseClientAddress.sourceSystem = a.hasProp("SourceSystem") && a.prop("SourceSystem") != null ? getBd(59, a.prop("SourceSystem").stringValue()) : null
        kycCaseClientAddress.exAddress = a.hasProp("SourceId") && a.prop("SourceId") != null ? a.prop("SourceId").stringValue() : null
        kycCaseClientAddress.type = a.hasProp("Type") && a.prop("Type") != null ? getBd(3, a.prop("Type").stringValue()) : null
        kycCaseClientAddress.dateModify = a.hasProp("DateModify") && a.prop("DateModify") != null ? LocalDate.parse(a.prop("DateModify").stringValue()).atStartOfDay() : null
        kycCaseClientAddress.isMain = a.hasProp("IsMain") && a.prop("IsMain") != null ? a.prop("IsMain").boolValue() : null
        kycCaseClientAddress.addressLine = a.hasProp("AddressLine") && a.prop("AddressLine") != null ? a.prop("AddressLine").stringValue() : null
        kycCaseClientAddress.addressCode = a.hasProp("AddressCode") && a.prop("AddressCode") != null ? a.prop("AddressCode").stringValue() : null
        kycCaseClientAddress.countryCode = a.hasProp("CountryCode") && a.prop("CountryCode") != null ? getBd(40, a.prop("CountryCode").stringValue()) : null
        kycCaseClientAddress.country = a.hasProp("Country") && a.prop("Country") != null ? a.prop("Country").stringValue() : null
        kycCaseClientAddress.okato = a.hasProp("OKATO") && a.prop("OKATO") != null ? a.prop("OKATO").stringValue() : null
        kycCaseClientAddress.cityName = a.hasProp("CityName") && a.prop("CityName") != null ? a.prop("CityName").stringValue() : null
        kycAddress.add(kycCaseClientAddress)
    }
    execution.setVariable("kycAddress", kycAddress)
    execution.setVariable("addressFail", false)
}
catch (Exception e) {
    execution.setVariable("addressFail", true)
    println("Не удалось сформировать kycCaseClientAddress, отсутствуют ключевые поля")
    println(e.getLocalizedMessage())
}