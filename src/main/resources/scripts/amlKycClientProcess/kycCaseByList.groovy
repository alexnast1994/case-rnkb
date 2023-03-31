import com.prime.db.rnkb.model.BaseDictionary
import com.prime.db.rnkb.model.kyc.KycCaseClientList2

import java.time.LocalDateTime

import static org.camunda.spin.Spin.JSON

def pld = execution.getVariable("payload")
def json = JSON(pld)

BaseDictionary getBd(int type, String code) {
    baseDictRepo.getByBaseDictionaryTypeCodeAndCode(type, code);
}

List<KycCaseClientList2> kycCaseClientList2List = []
def kycList = json.prop("ResponseData").prop("ClientCheckResult").elements()[0].prop("KYCList").elements()
def filteredList = kycList.findAll { k ->
    k.prop("CheckStatus").stringValue() == "1" ||
            k.prop("CheckStatus").stringValue() == "2"
}
def result = json.prop("Clients").elements()[0].prop("Results").elements()
filteredList.each { l ->
    KycCaseClientList2 kycCaseClientList2 = new KycCaseClientList2()
    kycCaseClientList2.setCaseId(execution.getVariable("caseNewOut"))
    kycCaseClientList2.setMatchType(getBd(290, execution.getVariable("executionId") ? "1" : "2"))
    def record = result.find { r -> r.prop("record").prop("listType").stringValue() == l.prop("Id").stringValue()
    }.prop("record")
    java.lang.System.out.println(record)
    kycCaseClientList2.setTypeList(getBd(214, record.prop("listType").stringValue()))
    kycCaseClientList2.setExId(record.prop("exId").stringValue())
    kycCaseClientList2.setNum(record.prop("num").stringValue())
    java.lang.System.out.println(result[0].prop("D0"))
    StringBuilder markerString = new StringBuilder()
    java.lang.System.out.println(result[0].fieldNames())
    def fields = result[0].fieldNames().findAll { it =~ /^[D,M][0-9]*$/ }
    java.lang.System.out.println(fields)
    fields.each { it ->
        if ((result[0].prop(it).isBoolean() && result[0].prop(it).boolValue() != false)
                || (result[0].prop(it).isString() && result[0].prop(it).stringValue() != "")
                || (result[0].prop(it).isNumber() && result[0].prop(it).numberValue() != 0)) {
            markerString.append(it)
            markerString.append(", ")
        }
    }
    java.lang.System.out.println(markerString.toString().substring(0, markerString.toString().size() - 2))
    kycCaseClientList2.setMarkers(markerString.size() > 0 ? markerString.toString().substring(0, markerString.toString().size() - 2) : "")
    kycCaseClientList2.setNameMatch(result[0].prop("D0").numberValue() as Integer)
    kycCaseClientList2.setFirstName(result[0].prop("D28").numberValue() as Integer)
    kycCaseClientList2.setLastName(result[0].prop("D27").numberValue() as Integer)
    kycCaseClientList2.setMiddleName(result[0].prop("D29").numberValue() as Integer)
    kycCaseClientList2.setLevelBlocking(l.prop("LevelBlocking").stringValue() as Integer)
    kycCaseClientList2.setCheckStatus(l.prop("CheckStatus").stringValue() as Integer)
    kycCaseClientList2.setIsActive(record.prop("isActive").boolValue() as Boolean)
    kycCaseClientList2.setEntityType(record.prop("entityType").stringValue())
    kycCaseClientList2.dateStart = record.hasProp("dateStart") && record.prop("dateStart") != null ? LocalDateTime.parse(record.prop("dateStart").stringValue()) : null
    kycCaseClientList2.dateDeactivate = record.hasProp("dateDeactivate") && record.prop("dateDeactivate") != null ? LocalDateTime.parse(record.prop("dateDeactivate").stringValue()) : null
    kycCaseClientList2.numberOfSolution = record.hasProp("numberOfSolution") && record.prop("numberOfSolution") != null ? record.prop("numberOfSolution").stringValue() : null
    kycCaseClientList2.matchType = json.hasProp("executionId") && json.prop("executionId") != null ?  getBd(290, "2") : getBd(290, "1")
    kycCaseClientList2List.add(kycCaseClientList2)
}
execution.setVariable("kycCaseClientList2List", kycCaseClientList2List)