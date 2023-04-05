import com.prime.db.rnkb.model.BaseDictionary
import com.prime.db.rnkb.model.Client
import com.prime.db.rnkb.model.kyc.KycCaseClientIndividual

import java.time.LocalDate
import java.time.LocalDateTime

import static org.camunda.spin.Spin.JSON

BaseDictionary getBd(int type, String code) {
    baseDictRepo.getByBaseDictionaryTypeCodeAndCode(type, code);
}

def pld = execution.getVariable("payloadFull")
def json = JSON(pld)
if (json.hasProp("InitialClient") && json.prop("InitialClient") != null && json.prop("InitialClient").hasProp("RequestData")) {
    def initialClient = json.prop("InitialClient").prop("RequestData").prop("Clients").elements()[0].prop("ClientIndividual")
    KycCaseClientIndividual kycCaseClientIndividual = new KycCaseClientIndividual()
    kycCaseClientIndividual.setKycCaseClientId(execution.getVariable("kyccaseClient"))
    kycCaseClientIndividual.setLastNameEng(initialClient.hasProp("LastNameEng") && initialClient.prop("LastNameEng") != null ? initialClient.prop("LastNameEng").stringValue() : null)
    kycCaseClientIndividual.setFirstNameEng(initialClient.hasProp("FirstNameEng") && initialClient.prop("FirstNameEng") != null ? initialClient.prop("FirstNameEng").stringValue() : null)
    kycCaseClientIndividual.setMiddleNameEng(initialClient.hasProp("MiddleNameEng") && initialClient.prop("MiddleNameEng") != null ? initialClient.prop("MiddleNameEng").stringValue() : null)
    kycCaseClientIndividual.setLastName(initialClient.hasProp("LastName") && initialClient.prop("LastName") != null ? initialClient.prop("LastName").stringValue(): null)
    kycCaseClientIndividual.setFirstName(initialClient.hasProp("FirstName") && initialClient.prop("FirstName") != null ? initialClient.prop("FirstName").stringValue(): null)
    kycCaseClientIndividual.setMiddleName(initialClient.hasProp("MiddleName") && initialClient.prop("MiddleName") != null ? initialClient.prop("MiddleName").stringValue(): null)
    kycCaseClientIndividual.setFullNameEng(initialClient.hasProp("FullNameEng") && initialClient.prop("FullNameEng") != null ? initialClient.prop("FullNameEng").stringValue(): null)
    kycCaseClientIndividual.setPlaceOfBirth(initialClient.hasProp("PlaceOfBirth") && initialClient.prop("PlaceOfBirth") != null ? initialClient.prop("PlaceOfBirth").stringValue(): null)
    kycCaseClientIndividual.setOkato(initialClient.hasProp("OKATORegCode") && initialClient.prop("OKATORegCode") != null ? initialClient.prop("OKATORegCode").stringValue(): null)
    kycCaseClientIndividual.setRegisterNumber(initialClient.hasProp("RegisterNumber") && initialClient.prop("RegisterNumber") != null ? initialClient.prop("RegisterNumber").stringValue(): null)
    kycCaseClientIndividual.setBirthdate(initialClient.hasProp("Birthdate") && initialClient.prop("Birthdate") != null ? LocalDate.parse(initialClient.prop("Birthdate").stringValue()).atStartOfDay() :null)
    kycCaseClientIndividual.setCitizenship(initialClient.hasProp("Citizenship") && initialClient.prop("Citizenship") != null ? getBd(40, initialClient.prop("Citizenship").stringValue()) : null)

    execution.setVariable("kycCaseClientIndividual", kycCaseClientIndividual)
}
else {
    def clients = execution.getVariable("client") as List<Client>
    def client = clients.get(0)
    def clientIndividual = client.getClientIndividual()
    KycCaseClientIndividual kycCaseClientIndividual = new KycCaseClientIndividual()
    kycCaseClientIndividual.setKycCaseClientId(execution.getVariable("kyccaseClient"))
    kycCaseClientIndividual.setLastNameEng(clientIndividual.getLastnameeng())
    kycCaseClientIndividual.setFirstNameEng(clientIndividual.getFirstnameeng())
    kycCaseClientIndividual.setMiddleNameEng(clientIndividual.getMiddlenameeng())
    kycCaseClientIndividual.setLastName(clientIndividual.getLastname())
    kycCaseClientIndividual.setFirstName(clientIndividual.getFirstname())
    kycCaseClientIndividual.setMiddleName(clientIndividual.getMiddlename())
    kycCaseClientIndividual.setFullNameEng(clientIndividual.getFullnameeng())
    kycCaseClientIndividual.setBirthdate(clientIndividual.getBirthdate())
    kycCaseClientIndividual.setPlaceOfBirth(clientIndividual.getPlaceofbirth())
    kycCaseClientIndividual.setCitizenship(clientIndividual.getCitizenship())
    kycCaseClientIndividual.setOkato(clientIndividual.getOkatoRegCode())
    kycCaseClientIndividual.setRegisterNumber(clientIndividual.getRegisternumeber())

    execution.setVariable("kycCaseClientIndividual", kycCaseClientIndividual)

}
