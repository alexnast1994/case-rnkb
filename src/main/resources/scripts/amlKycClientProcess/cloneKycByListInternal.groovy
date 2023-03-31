import com.prime.db.rnkb.model.kyc.KycCaseClientAddressList
import com.prime.db.rnkb.model.kyc.KycCaseClientCountryList
import com.prime.db.rnkb.model.kyc.KycCaseClientDateDetails
import com.prime.db.rnkb.model.kyc.KycCaseClientIdentityAtrList
import com.prime.db.rnkb.model.kyc.KycCaseClientList2
import com.prime.db.rnkb.model.kyc.KycCaseClientNameList
import com.prime.db.rnkb.model.kyc.KycCaseClientVersionsList
import com.prime.db.rnkb.model.kyc.KycList639P

List<KycCaseClientList2> kycCaseClientList2sOld = execution.getVariable("kycCaseClientList2sOld")
List<KycCaseClientList2> kycCaseClientList2sNew = execution.getVariable("kycCaseClientList2sNew")

List<KycList639P> kycList639PListNew = []
List<KycCaseClientNameList> kycCaseClientNameListsNew = []
List<KycCaseClientDateDetails> kycCaseClientDateDetailsNew = []
List<KycCaseClientIdentityAtrList> kycCaseClientIdentityAtrListsNew = []
List<KycCaseClientCountryList> kycCaseClientCountryListsNew = []
List<KycCaseClientVersionsList> kycCaseClientVersionsListsNew = []
List<KycCaseClientAddressList> kycCaseClientAddressListsNew = []

kycCaseClientList2sOld.each { it ->
    KycCaseClientList2 newK = kycCaseClientList2sNew.find {it1 -> it1.exId == it.exId}

    if (!it.getKycCaseClient639pDetailsLists().isEmpty() && it.getKycCaseClient639pDetailsLists() != null) {
        List<KycList639P> kycList639PList1 = it.getKycCaseClient639pDetailsLists()
        List<KycList639P> kycList639PList = []
        kycList639PList1.each {it1 ->
            KycList639P k = new KycList639P()
            k.messageDate = it1.messageDate
            k.recordType = it1.recordType
            k.refuseCode = it1.refuseCode
            k.refuseDate = it1.refuseDate
            k.refuseCodeReason = it1.refuseCodeReason
            k.codeOperation = it1.codeOperation
            k.signOfUnusualOperation = it1.signOfUnusualOperation
            kycList639PList.add(k)
        }
        kycList639PList.each {k ->
            k.kycCaseByListId = newK
            kycList639PListNew.add(k)
        }
    }
    if (!it.getKycCaseClientNameLists().isEmpty() && it.getKycCaseClientNameLists() != null) {
        List<KycCaseClientNameList> kycCaseClientNameLists1 = it.getKycCaseClientNameLists()
        List<KycCaseClientNameList> kycCaseClientNameLists = []
        kycCaseClientNameLists1.each {it1 ->
            KycCaseClientNameList k = new KycCaseClientNameList()
            k.nameType = it1.nameType
            k.name = it1.name
            k.firstName = it1.firstName
            k.lastName = it1.lastName
            k.middleName = it1.middleName
            k.bestMatch = it1.bestMatch
            k.latin = it1.latin
            k.dateOfBirth = it1.dateOfBirth
            k.yearOfBirth = it1.yearOfBirth
            k.placeOfBirth = it1.placeOfBirth
            k.inn = it1.inn
            k.snils = it1.snils
            k.registrationDate = it1.registrationDate
            k.placeOfRegistration = it1.placeOfRegistration
            k.ogrn = it1.ogrn
            kycCaseClientNameLists.add(k)
        }
        kycCaseClientNameLists.each {k ->
            k.kycCaseByListId = newK
            kycCaseClientNameListsNew.add(k)
        }
    }
    if (!it.getKycCaseClientDateDetailsList().isEmpty() && it.getKycCaseClientDateDetailsList() != null) {
        List<KycCaseClientDateDetails> kycCaseClientDateDetails1 = it.getKycCaseClientDateDetailsList()
        List<KycCaseClientDateDetails> kycCaseClientDateDetails = []
        kycCaseClientDateDetails1.each {it1 ->
            KycCaseClientDateDetails k = new KycCaseClientDateDetails()
            k.dateTypeId = it1.dateTypeId
            k.dateValue = it1.dateValue
            k.day = it1.day
            k.month = it1.month
            k.year = it1.year
            kycCaseClientDateDetails.add(k)
        }
        kycCaseClientDateDetails.each {k ->
            k.kycCaseByListId = newK
            kycCaseClientDateDetailsNew.add(k)
        }
    }
    if (!it.getKycCaseClientIdentityAtrLists().isEmpty() && it.getKycCaseClientIdentityAtrLists() != null) {
        List<KycCaseClientIdentityAtrList> kycCaseClientIdentityAtrLists1 = it.getKycCaseClientIdentityAtrLists()
        List<KycCaseClientIdentityAtrList> kycCaseClientIdentityAtrLists = []
        kycCaseClientIdentityAtrLists1.each {it1 ->
            KycCaseClientIdentityAtrList k = new KycCaseClientIdentityAtrList()
            k.idValue = k.idValue
            k.idType = k.idType
            k.idSerial = k.idSerial
            k.idNumber = k.idNumber
            k.comment = k.comment
            k.dateDocument = k.dateDocument
            k.issuingAuthority = k.issuingAuthority
            k.expiryDateOfDocument = k.expiryDateOfDocument
            k.validDoc = k.validDoc
            kycCaseClientIdentityAtrLists.add(k)
        }
        kycCaseClientIdentityAtrLists.each {k ->
            k.kycCaseByListId = newK
            kycCaseClientIdentityAtrListsNew.add(k)
        }
    }
    if (!it.getKycCaseClientCountryLists().isEmpty() && it.getKycCaseClientCountryLists() != null) {
        List<KycCaseClientCountryList> kycCaseClientCountryLists1 = it.getKycCaseClientCountryLists()
        List<KycCaseClientCountryList> kycCaseClientCountryLists = []
        kycCaseClientCountryLists1.each {it1 ->
            KycCaseClientCountryList k = new KycCaseClientCountryList()
            k.countryCode = k.countryCode
            k.countryType = k.countryType
            k.countryName = k.countryName
            kycCaseClientCountryLists.add(k)
        }
        kycCaseClientCountryListsNew.each {k ->
            k.kycCaseByListId = newK
            kycCaseClientCountryLists.add(k)
        }
    }
    if (!it.getKycCaseClientVersionsLists().isEmpty() && it.getKycCaseClientVersionsLists() != null) {
        List<KycCaseClientVersionsList> kycCaseClientVersionsLists1 = it.getKycCaseClientVersionsLists()
        List<KycCaseClientVersionsList> kycCaseClientVersionsLists = []
        kycCaseClientVersionsLists1.each {it1 ->
            KycCaseClientVersionsList k = new KycCaseClientVersionsList()
            k.listIdentifier = k.listIdentifier
            k.listVersion = k.listVersion
            k.versionDate = k.versionDate
            k.loadDate = k.loadDate
            k.updateDate = k.updateDate
            k.isLastVersion = k.isLastVersion
            kycCaseClientVersionsLists.add(k)
        }
        kycCaseClientVersionsLists.each {k ->
            k.kycCaseByListId = newK
            kycCaseClientVersionsListsNew.add(k)
        }
    }
    if (!it.getKycCaseClientAddressLists().isEmpty() && it.getKycCaseClientAddressLists() != null) {
        List<KycCaseClientAddressList> kycCaseClientAddressLists1 = it.getKycCaseClientAddressLists()
        List<KycCaseClientAddressList> kycCaseClientAddressLists = []
        kycCaseClientAddressLists1.each {it1 ->
            KycCaseClientAddressList k = new KycCaseClientAddressList()
            k.addressType = it1.addressType
            k.addressLine = it1.addressLine
            k.countryCode = it1.countryCode
            k.country = it1.country
            kycCaseClientAddressLists.add(k)
        }
        kycCaseClientAddressLists.each {k ->
            k.kycCaseByListId = newK
            kycCaseClientAddressListsNew.add(k)
        }
    }
}

if (!kycList639PListNew.isEmpty()) {
    execution.setVariable("kycList639PListNew", kycList639PListNew)
    execution.setVariable("kycList639PListNewErr", false)
}
else {
    execution.setVariable("kycList639PListNewErr", true)
}
if (!kycCaseClientNameListsNew.isEmpty()) {
    execution.setVariable("kycCaseClientNameListsNew", kycCaseClientNameListsNew)
    execution.setVariable("kycCaseClientNameListsNewErr", false)
}
else {
    execution.setVariable("kycCaseClientNameListsNewErr", true)
}
if (!kycCaseClientDateDetailsNew.isEmpty()) {
    execution.setVariable("kycCaseClientDateDetailsNew", kycCaseClientDateDetailsNew)
    execution.setVariable("kycCaseClientDateDetailsNewErr", false)
}
else {
    execution.setVariable("kycCaseClientDateDetailsNewErr", true)
}
if (!kycCaseClientIdentityAtrListsNew.isEmpty()) {
    execution.setVariable("kycCaseClientIdentityAtrListsNew", kycCaseClientIdentityAtrListsNew)
    execution.setVariable("kycCaseClientIdentityAtrListsNewErr", false)
}
else {
    execution.setVariable("kycCaseClientIdentityAtrListsNewErr", true)
}
if (!kycCaseClientCountryListsNew.isEmpty()) {
    execution.setVariable("kycCaseClientCountryListsNew", kycCaseClientCountryListsNew)
    execution.setVariable("kycCaseClientCountryListsNewErr", false)
}
else {
    execution.setVariable("kycCaseClientCountryListsNewErr", true)
}
if (!kycCaseClientVersionsListsNew.isEmpty()) {
    execution.setVariable("kycCaseClientVersionsListsNew", kycCaseClientVersionsListsNew)
    execution.setVariable("kycCaseClientVersionsListsNewErr", false)
}
else {
    execution.setVariable("kycCaseClientVersionsListsNewErr", true)
}
if (!kycCaseClientAddressListsNew.isEmpty()) {
    execution.setVariable("kycCaseClientAddressListsNew", kycCaseClientAddressListsNew)
    execution.setVariable("kycCaseClientAddressListsNewErr", false)
}
else {
    execution.setVariable("kycCaseClientAddressListsNewErr", true)
}