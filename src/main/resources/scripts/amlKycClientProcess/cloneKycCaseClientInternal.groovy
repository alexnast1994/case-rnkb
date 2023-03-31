import com.prime.db.rnkb.model.kyc.KycCaseClient
import com.prime.db.rnkb.model.kyc.KycCaseClientAddress
import com.prime.db.rnkb.model.kyc.KycCaseClientIndividual
import com.prime.db.rnkb.model.kyc.KycCaseClientLegal
import com.prime.db.rnkb.model.kyc.KycCaseClientVerdoc


List<KycCaseClient> kycCaseClientOld = execution.getVariable("kycCaseClientOld")
List<KycCaseClient> kycCaseClientNew = execution.getVariable("kycCaseClientNew")

List<KycCaseClientAddress> kycCaseClientAddressesNew = []
List<KycCaseClientLegal> kycCaseClientLegalsNew = []
List<KycCaseClientIndividual> kycCaseClientIndividualsNew = []
List<KycCaseClientVerdoc> kycCaseClientVerdocsNew = []


kycCaseClientOld.each { it ->
    KycCaseClient newK = kycCaseClientNew.find {it1 -> it1.sourceId == it.sourceId}

    if (!it.getKycCaseClientAddress().isEmpty() && it.getKycCaseClientAddress() != null) {
        List<KycCaseClientAddress> kycCaseClientAddresses1 = it.getKycCaseClientAddress()
        List<KycCaseClientAddress> kycCaseClientAddresses = []
        kycCaseClientAddresses1.each {it1 ->
            KycCaseClientAddress k = new KycCaseClientAddress()
            k.sourceSystem = it1.sourceSystem
            k.exAddress = it1.exAddress
            k.type = it1.type
            k.dateModify = it1.dateModify
            k.isMain = it1.isMain
            k.addressLine = it1.addressLine
            k.addressCode = it1.addressCode
            k.country = it1.country
            k.countryCode = it1.countryCode
            k.okato = it1.okato
            k.cityName = it1.cityName
            kycCaseClientAddresses.add(k)
        }
        kycCaseClientAddresses.each {k ->
            k.kycCaseByListId = newK
            kycCaseClientAddressesNew.add(k)
        }
    }
    if (!it.getKycCaseClientLegalList().isEmpty() && it.getKycCaseClientLegalList() != null) {
        List<KycCaseClientLegal> kycCaseClientLegals1 = it.getKycCaseClientLegalList()
        List<KycCaseClientLegal> kycCaseClientLegals = []
        kycCaseClientLegals1.each {it1 ->
            KycCaseClientLegal k = new KycCaseClientLegal()
            k.legalName = it1.legalName
            k.shortName = it1.shortName
            k.shortNameEng = it1.shortNameEng
            k.kpp = it1.kpp
            k.swiftCd = it1.swiftCd
            k.bankId = it1.bankId
            k.registrationCity = it1.registrationCity
            k.okfs = it1.okfs
            k.taxSerialIssueDate = it1.taxSerialIssueDate
            k.registrationUnit = it1.registrationUnit
            k.dateOfRegistrationBeforeOgrn = it1.dateOfRegistrationBeforeOgrn
            k.okato = it1.okato
            k.legalForm = it1.legalForm
            k.giin = it1.giin
            k.registrationUnitAddress = it1.registrationUnitAddress
            k.jurisdiction = it1.jurisdiction
            k.lei = it1.lei
            kycCaseClientLegals.add(k)
        }
        kycCaseClientLegals.each {k ->
            k.kycCaseClientId = newK
            kycCaseClientLegalsNew.add(k)
        }
    }
    if (!it.getKycCaseClientIndividualList().isEmpty() && it.getKycCaseClientIndividualList() != null) {
        List<KycCaseClientIndividual> kycCaseClientIndividuals1 = it.getKycCaseClientIndividualList()
        List<KycCaseClientIndividual> kycCaseClientIndividuals = []
        kycCaseClientIndividuals1.each {it1 ->
            KycCaseClientIndividual k = new KycCaseClientIndividual()
            k.lastNameEng = it1.lastNameEng
            k.firstNameEng = it1.firstNameEng
            k.middleNameEng = it1.middleNameEng
            k.lastName = it1.lastName
            k.firstName = it1.firstName
            k.middleName = it1.middleName
            k.fullNameEng = it1.fullNameEng
            k.birthdate = it1.birthdate
            k.placeOfBirth = it1.placeOfBirth
            k.citizenship = it1.citizenship
            k.registerNumber = it1.registerNumber
            kycCaseClientIndividuals.add(k)
        }
        kycCaseClientIndividuals.each {k ->
            k.kycCaseClientId = newK
            kycCaseClientIndividualsNew.add(k)
        }
    }
    if (!it.getKycCaseClientVerdocList().isEmpty() && it.getKycCaseClientVerdocList() != null) {
        List<KycCaseClientVerdoc> kycCaseClientVerdocs1 = it.getKycCaseClientVerdocList()
        List<KycCaseClientVerdoc> kycCaseClientVerdocs = []
        kycCaseClientVerdocs1.each {it1 ->
            KycCaseClientVerdoc k = new KycCaseClientVerdoc()
            k.sourceId = it1.sourceId
            k.isMain = it1.isMain
            k.issueDate = it1.issueDate
            k.name = it1.name
            k.type = it1.type
            k.docNumber = it1.docNumber
            k.issueByOrganization = it1.issueByOrganization
            k.expirationDate = it1.expirationDate
            k.issueByDepartmentCode = it1.issueByDepartmentCode
            kycCaseClientVerdocs.add(k)
        }
        kycCaseClientVerdocs.each {k ->
            k.kycCaseClientId = newK
            kycCaseClientVerdocsNew.add(k)
        }
    }

}

if (!kycCaseClientAddressesNew.isEmpty()) {
    execution.setVariable("kycCaseClientAddressesNew", kycCaseClientAddressesNew)
    execution.setVariable("kycCaseClientAddressesNewErr", false)
}
else {
    execution.setVariable("kycCaseClientAddressesNewErr", true)
}
if (!kycCaseClientLegalsNew.isEmpty()) {
    execution.setVariable("kycCaseClientLegalsNew", kycCaseClientLegalsNew)
    execution.setVariable("kycCaseClientLegalsNewErr", false)
}
else {
    execution.setVariable("kycCaseClientLegalsNewErr", true)
}
if (!kycCaseClientIndividualsNew.isEmpty()) {
    execution.setVariable("kycCaseClientIndividualsNew", kycCaseClientIndividualsNew)
    execution.setVariable("kycCaseClientIndividualsNewErr", false)
}
else {
    execution.setVariable("kycCaseClientIndividualsNewErr", true)
}
if (!kycCaseClientVerdocsNew.isEmpty()) {
    execution.setVariable("kycCaseClientVerdocsNew", kycCaseClientVerdocsNew)
    execution.setVariable("kycCaseClientVerdocsNewErr", false)
}
else {
    execution.setVariable("kycCaseClientVerdocsNewErr", true)
}

