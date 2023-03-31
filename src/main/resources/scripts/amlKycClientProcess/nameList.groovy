import com.prime.db.rnkb.model.kyc.KycCaseClientList2
import com.prime.db.rnkb.model.kyc.KycCaseClientNameList
import com.prime.db.rnkb.model.BaseDictionary
import org.camunda.spin.json.SpinJsonNode

import static org.camunda.spin.Spin.JSON

def pld = execution.getVariable("payload")
def json = JSON(pld)

BaseDictionary getBd(int type, String code) {
    baseDictRepo.getByBaseDictionaryTypeCodeAndCode(type, code);
}

def kyc2list = execution.getVariable("kycCaseClientList2List") as List<KycCaseClientList2>
println("Списки kyc")
println(kyc2list)
List<KycCaseClientNameList> kycCaseClientNameLists = []
try {
    def result = json.prop("Clients").elements()[0].prop("Results").elements()
    kyc2list.each {k ->
        def record = result.find { r -> r.prop("record").prop("listType").stringValue() == k.getTypeList().getCode()
        }.prop("record")
        try {
            if (record.hasProp("nameList") && record.prop("nameList").toString() != "{}")
                record.prop("nameList").elements().each {n ->
                    KycCaseClientNameList kycCaseClientNameList = new KycCaseClientNameList()
                    kycCaseClientNameList.setKycCaseByListId(k)
                    kycCaseClientNameList.setNameType(getBd(280, n.prop("nameType").stringValue()))
                    kycCaseClientNameList.setName(n.prop("name").stringValue())
                    kycCaseClientNameList.setFirstName(n.hasProp("firstName") && n.prop("firstName") != null ? n.prop("firstName").stringValue() : null)
                    kycCaseClientNameList.setLastName(n.hasProp("lastName") && n.prop("lastName") != null  ? n.prop("lastName").stringValue() : null)
                    kycCaseClientNameList.setMiddleName(n.hasProp("middleName") && n.prop("middleName") != null ? n.prop("middleName").stringValue() : null)
                    def thisResult = result.find { r -> r.prop("record").prop("listType").stringValue() == k.getTypeList().getCode()
                    }
                    kycCaseClientNameList.setBestMatch(thisResult.hasProp("bestMatch") && thisResult.prop("bestMatch") != null ? true : false as Boolean)
                    kycCaseClientNameList.latin = n.hasProp("latin") && n.prop("latin") != null ? n.prop("latin").stringValue() : null
                    kycCaseClientNameList.dateOfBirth = n.hasProp("dateOfBirth") && n.prop("dateOfBirth") != null ? n.prop("dateOfBirth").stringValue() : null
                    kycCaseClientNameList.yearOfBirth = n.hasProp("yearOfBirth") && n.prop("yearOfBirth") != null ? n.prop("yearOfBirth").toString() : null
                    kycCaseClientNameList.placeOfBirth = n.hasProp("placeOfBirth") && n.prop("placeOfBirth") != null ? n.prop("placeOfBirth").stringValue() : null
                    kycCaseClientNameList.inn = n.hasProp("inn") && n.prop("inn") != null ? n.prop("inn").stringValue() : null
                    kycCaseClientNameList.inn = n.hasProp("snils") && n.prop("snils") != null ? n.prop("snils").stringValue() : null
                    kycCaseClientNameList.registrationDate = n.hasProp("registrationDate") && n.prop("registrationDate") != null ? n.prop("registrationDate").stringValue() : null
                    kycCaseClientNameList.placeOfRegistration = n.hasProp("placeOfRegistration") && n.prop("placeOfRegistration") != null ? n.prop("placeOfRegistration").stringValue() : null
                    kycCaseClientNameList.ogrn = n.hasProp("ogrn") && n.prop("ogrn") != null ? n.prop("ogrn").stringValue() : null
                    kycCaseClientNameLists.add(kycCaseClientNameList)
                }
        }
        catch (Exception e) {
            println("Пропуск")
        }

    }
    if (kycCaseClientNameLists.isEmpty()) {
        println("Не удалось сформировать nameList, отсутствуют ключевые поля")
        execution.setVariable("nameListErr", true)
    }
    else {
        execution.setVariable("kycCaseClientNameLists", kycCaseClientNameLists)
        execution.setVariable("nameListErr", false)
    }

}
catch (Exception e) {
    println("Не удалось сформировать nameList, отсутствуют ключевые поля")
    println(e.localizedMessage)
    execution.setVariable("nameListErr", true)
}

