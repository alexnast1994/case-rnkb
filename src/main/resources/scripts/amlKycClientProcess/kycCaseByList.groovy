import com.prime.db.rnkb.model.BaseDictionary
import com.prime.db.rnkb.model.kyc.KycCaseClientList2
import org.camunda.spin.json.SpinJsonNode

import java.time.LocalDateTime

import static org.camunda.spin.Spin.JSON

def pld = execution.getVariable("payload")
def json = JSON(pld)
def recreate = execution.getVariable("recreate") as Boolean

BaseDictionary getBd(int type, String code) {
    baseDictRepo.getByBaseDictionaryTypeCodeAndCode(type, code);
}
def jsonFull = execution.getVariable("payloadFull") as SpinJsonNode
List<KycCaseClientList2> kycCaseClientList2List = []
def kycList = json.prop("ResponseData").prop("ClientCheckResult").elements()[0].prop("KYCList").elements()
def filteredList = kycList.findAll { k ->
    k.prop("CheckStatus").stringValue() == "1" ||
            k.prop("CheckStatus").stringValue() == "2"
}

filteredList.each { l ->
    def result = json.prop("Clients").elements()[0].prop("Results").elements().find { r -> r.prop("record").prop("listType").stringValue() == l.prop("Id").stringValue()}
    KycCaseClientList2 kycCaseClientList2 = new KycCaseClientList2()
    kycCaseClientList2.setCaseId(execution.getVariable("caseNewOut"))
    def record = result.prop("record")
    java.lang.System.out.println(record)
    kycCaseClientList2.setTypeList(getBd(214, record.prop("listType").stringValue()))
    kycCaseClientList2.setExId(record.prop("exId").stringValue())
    kycCaseClientList2.setNum(record.prop("num").stringValue())
    println("Маркеры")
    java.lang.System.out.println(result.prop("D0"))
    StringBuilder markerString = new StringBuilder()
    java.lang.System.out.println(result.fieldNames())
    def fields = result.fieldNames().findAll { it =~ /^[D,M][0-9]*$/ }
    java.lang.System.out.println(fields)
    fields.each { it ->
        if ((result.prop(it).isBoolean() && result.prop(it).boolValue() != false)
                || (result.prop(it).isString() && result.prop(it).stringValue() != "")
                || (result.prop(it).isNumber() && result.prop(it).numberValue() != 0)) {
            markerString.append(it)
            markerString.append(", ")
        }
    }
    java.lang.System.out.println(markerString.toString().substring(0, markerString.toString().size() - 2))
    kycCaseClientList2.setMarkers(markerString.size() > 0 ? markerString.toString().substring(0, markerString.toString().size() - 2) : "")
    kycCaseClientList2.setNameMatch(result.prop("D0").numberValue() as Integer)
    kycCaseClientList2.setFirstName(result.prop("D28").numberValue() as Integer)
    kycCaseClientList2.setLastName(result.prop("D27").numberValue() as Integer)
    kycCaseClientList2.setMiddleName(result.prop("D29").numberValue() as Integer)
    kycCaseClientList2.setLevelBlocking(l.prop("LevelBlocking").stringValue() as Integer)
    kycCaseClientList2.setCheckStatus(l.prop("CheckStatus").stringValue() as Integer)
    kycCaseClientList2.setIsActive(record.prop("isActive").boolValue() as Boolean)
    kycCaseClientList2.setEntityType(record.prop("entityType").stringValue())
    kycCaseClientList2.dateStart = record.hasProp("dateStart") && record.prop("dateStart") != null ? LocalDateTime.parse(record.prop("dateStart").stringValue()) : null
    kycCaseClientList2.dateDeactivate = record.hasProp("dateDeactivate") && record.prop("dateDeactivate") != null ? LocalDateTime.parse(record.prop("dateDeactivate").stringValue()) : null
    kycCaseClientList2.numberOfSolution = record.hasProp("numberOfSolution") && record.prop("numberOfSolution") != null ? record.prop("numberOfSolution").stringValue() : null
    if (recreate) {
        BaseDictionary b = execution.getVariable("lastType") as BaseDictionary
        println(b.getCode())
        if (jsonFull.hasProp("executionId") && !jsonFull.prop("executionId").isNull() && b.getCode() == "1") {
            kycCaseClientList2.matchType =  getBd(290, "3")
        }
        else if (jsonFull.hasProp("executionId") && jsonFull.prop("executionId").isNull() && b.getCode() == "1") {
            kycCaseClientList2.matchType =  getBd(290, "1")
        }
        else if (jsonFull.hasProp("executionId") && jsonFull.prop("executionId").isNull() && b.getCode() == "2") {
            kycCaseClientList2.matchType =  getBd(290, "3")
        }
        else if (jsonFull.hasProp("executionId") && !jsonFull.prop("executionId").isNull() && b.getCode() == "2") {
            kycCaseClientList2.matchType =  getBd(290, "2")
        }
        else if (b.getCode() == "3") {
            kycCaseClientList2.matchType =  getBd(290, "3")
        }
    }
    else {
        def executionId = jsonFull.hasProp("executionId") ? jsonFull.prop("executionId") : null
        println(executionId)
        kycCaseClientList2.matchType = executionId.isNull() ? getBd(290, "1") : getBd(290, "2")
    }
    kycCaseClientList2.signOfTerrorist = record.hasProp("signOfTerrorist") && record.prop("signOfTerrorist") != null ? record.prop("signOfTerrorist").numberValue() == 1 : null
    kycCaseClientList2List.add(kycCaseClientList2)
}
execution.setVariable("kycCaseClientList2List", kycCaseClientList2List)