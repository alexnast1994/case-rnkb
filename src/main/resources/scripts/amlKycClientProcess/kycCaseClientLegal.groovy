import com.prime.db.rnkb.model.BaseDictionary
import com.prime.db.rnkb.model.kyc.KycCaseClientLegal

import java.time.LocalDate
import java.time.LocalDateTime

import static org.camunda.spin.Spin.JSON

BaseDictionary getBd(int type, String code) {
    baseDictRepo.getByBaseDictionaryTypeCodeAndCode(type, code);
}

def pld = execution.getVariable("payloadFull")
def json = JSON(pld)
def initialClient = json.prop("InitialClient").prop("RequestData").prop("Clients").elements()[0].prop("ClientLegal")
KycCaseClientLegal kycCaseClientLegal = new KycCaseClientLegal()
kycCaseClientLegal.setKycCaseClientId(execution.getVariable("kyccaseClientOutNew"))
kycCaseClientLegal.setLegalName(initialClient.hasProp("LegalName") ? initialClient.prop("LegalName").stringValue() : null)
kycCaseClientLegal.setShortName(initialClient.hasProp("ShortName") ? initialClient.prop("ShortName").stringValue() : null)
kycCaseClientLegal.setShortNameEng(initialClient.hasProp("ShortNameEng") ? initialClient.prop("ShortNameEng").stringValue() : null)
kycCaseClientLegal.setTaxSerialIssueDate(initialClient.hasProp("TaxSerialIssueDate") ? LocalDate.parse(initialClient.prop("TaxSerialIssueDate").stringValue()).atStartOfDay() : null )
kycCaseClientLegal.setDateOfRegistrationBeforeOgrn(initialClient.hasProp("DateOfRegistrationBeforeOGRN") ? LocalDate.parse(initialClient.prop("DateOfRegistrationBeforeOGRN").stringValue()).atStartOfDay() : null )
kycCaseClientLegal.setKpp(initialClient.hasProp("KPP") ? initialClient.prop("KPP").stringValue() : null)
kycCaseClientLegal.setSwiftCd(initialClient.hasProp("SwiftCd") ? initialClient.prop("SwiftCd").stringValue() : null)
kycCaseClientLegal.setBankId(initialClient.hasProp("BankId") ? initialClient.prop("BankId").stringValue() : null)
kycCaseClientLegal.setOkfs(initialClient.hasProp("OKFS") ? getBd(39,initialClient.prop("OKFS").stringValue()) : null)
kycCaseClientLegal.setJurisdiction(initialClient.hasProp("Jurisdiction") ? getBd(40, initialClient.prop("Jurisdiction").stringValue()) : null)
kycCaseClientLegal.setLegalForm(initialClient.hasProp("LegalForm") ? getBd(76, initialClient.prop("LegalForm").stringValue()) : null)
kycCaseClientLegal.setRegistrationCity(initialClient.hasProp("RegistrationCity") ? initialClient.prop("RegistrationCity").stringValue() : null)
kycCaseClientLegal.setRegistrationUnit(initialClient.hasProp("RegistrationUnit") ? initialClient.prop("RegistrationUnit").stringValue() : null)
kycCaseClientLegal.setOkato(initialClient.hasProp("OKATO") ? initialClient.prop("OKATO").stringValue() : null)
kycCaseClientLegal.setGiin(initialClient.hasProp("GIIN") ? initialClient.prop("GIIN").stringValue() : null)
kycCaseClientLegal.setRegistrationUnitAddress(initialClient.hasProp("RegistrationUnitAddress") ? initialClient.prop("RegistrationUnitAddress").stringValue() : null)
kycCaseClientLegal.setLei(initialClient.hasProp("LEI") ? initialClient.prop("LEI").stringValue() : null)

execution.setVariable("kycCaseClientLegal", kycCaseClientLegal)