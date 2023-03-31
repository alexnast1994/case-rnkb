import com.cognive.projects.casernkb.model.projection.KycCaseProjection
import com.prime.db.rnkb.model.BaseDictionary
import com.prime.db.rnkb.model.Case
import com.prime.db.rnkb.model.kyc.KycCaseClientList2

import static org.camunda.spin.Spin.JSON

def pld = execution.getVariable("payload")
def json = JSON(pld)
def jsonFull = execution.getVariable("payloadFull")
def executionId = jsonFull.prop("executionId")
def KYCList = json.prop("ResponseData").prop("ClientCheckResult").elements()[0].prop("KYCList")

def typeLists = []
KYCList.elements().stream().map({ r -> r.prop("Id").stringValue() }).each { i -> typeLists.add(i as String) }
println("Шаг 1")
println(typeLists.toString())

List<KycCaseProjection> getCases(String exId) {
    caseRepo.getLatestCaseByClientIdAndExIdAndNumAndTypeList1(exId);
}

List<Case> getCasesObj(List<Long> id) {
    caseRepo.findCasesByIds(id);
}

void updateStatusCase(Long id, Long status) {
    caseRepo.updateStatusCase(id, status)
}

println("Шаг 2")

BaseDictionary getBd(int type, String code) {
    baseDictRepo.getByBaseDictionaryTypeCodeAndCode(type, code);
}

List<KycCaseClientList2> getKyc(List<Long> caseId) {
    kycCaseByListRepo.getByCaseId(caseId)
}

println("Шаг 3")
println(execution.getVariable("sourceId"))
println(typeLists)
println(typeLists.class)
def cases = getCases(execution.getVariable("sourceId") as String)
def toChange = cases.findAll { it -> typeLists.contains(it.typeList) }
def toClone = cases.findAll { it -> !typeLists.contains(it.typeList) }


List<Case> changeCases = []
List<KycCaseClientList2> kycCaseClientList2 = new ArrayList<>()
if (toClone.isEmpty()) {
    List<Case> toCloneCases = getCasesObj(toClone.collect { it -> it.cased })
    execution.setVariable("toClone", toCloneCases)
    execution.setVariable("cloneF", true)
} else {
    execution.setVariable("cloneF", false)
}
if (toChange.isEmpty()) {
    execution.setVariable("createCase", true)
} else {
    List<Case> toChangeCases = getCasesObj(toChange.collect { it -> it.cased })
    List<KycCaseClientList2> kycCaseClientList2s = getKyc(toChangeCases.collect { c -> c.getId() })
    println("Изначальный kyc: " + kycCaseClientList2s.toString())
    toChangeCases.each { c ->
        println(c.getStatus())
        println(c.getId())
        println(c.getStatus().getId())
        println(getBd(286, "7").getId())
        println(c.getStatus().getId() == getBd(286, "7").getId())
        kycCaseClientList2s.each { k ->
            if ((k.matchType == getBd(290, "1") && executionId != null) || (k.matchType == getBd(290, "2") && executionId == null)) {
                println(k.getKycCaseClient639pDetailsLists())
                k.matchType = getBd(290, "3")
            }
        }
        println("KYC с измененными статусами: " + kycCaseClientList2.toString())
        if (c.getStatus().getId() == getBd(286, "1").getId() || c.getStatus().getId() == getBd(286, "2").getId()) {
            println("Шаг 6")
            execution.setVariable("createCase", true)
            println("Шаг 7")
            updateStatusCase(c.getId(), getBd(286, "7").getId())
            println("Шаг 8")

        } else if (c.getStatus().getId() == getBd(286, "7").getId() || c.getStatus().getId() == getBd(286, "4").getId() || c.getStatus().getId() == getBd(286, "5").getId()) {
            execution.setVariable("createCase", true)
            println("Шаг 9")
        }
    }
    if (!kycCaseClientList2.isEmpty()) {
        execution.setVariable("kycCaseClientList2sChanged", kycCaseClientList2s)
        execution.setVariable("fillByList", true)
    } else {
        execution.setVariable("fillByList", false)
    }
}

if (execution.getVariable("createCase") == null) {
    println("Шаг 12")
    execution.setVariable("createCase", false)
}
if (execution.getVariable("fillByList") == null) {
    println("Шаг 13")
    execution.setVariable("fillByList", false)
}
println("Шаг 10")