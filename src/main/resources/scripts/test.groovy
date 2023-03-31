import com.prime.db.rnkb.model.BaseDictionary
import com.prime.db.rnkb.model.Case
import com.prime.db.rnkb.model.kyc.KycCaseClient

import static org.camunda.spin.Spin.JSON

def pld = execution.getVariable("payload")
def json = JSON(pld)

def KYCList = json.prop("ResponseData").prop("ClientCheckResult").elements()[0].prop("KYCList")

def typeLists = []
KYCList.elements().stream().map({ r -> r.prop("Id").stringValue() }).each { i -> typeLists.add(i as Long) }
println("Шаг 1")

List<Case> getCases(String exId, List<String> codes) {
    caseRepo.getLatestCaseByClientIdAndExIdAndNumAndTypeList1(exId, codes);
}

println("Шаг 2")

BaseDictionary getBd(int type, String code) {
    baseDictRepo.getByBaseDictionaryTypeCodeAndCode(type, code);
}

println("Шаг 3")
println(execution.getVariable("sourceId"))
println(typeLists)
println(typeLists.class)
def cases = execution.getVariable("cases") as List<KycCaseClient>
println("Шаг 4")
def changeCases = []
println("Шаг 5")
cases.each { c ->
    if (c.getStatus() == getBd(286, "1") || c.getStatus() == getBd(286, "2")) {
        println("Шаг 6")
        execution.setVariable("createCase", true)
        println("Шаг 7")
        changeCases.add(c)
        println("Шаг 8")
        c.setStatus(getBd(286, "7"))
    } else if (c.getStatus() == getBd(286, "7") || c.getStatus() == getBd(286, "4") || c.getStatus() == getBd(286, "5")) {
        execution.setVariable("createCase", true)
        println("Шаг 9")
    } else {
        execution.setVariable("createCase", false)
    }
}
if (execution.getVariable("createCase") == null) {
    execution.setVariable("createCase", false)
}
execution.setVariable("changeCases", changeCases)
println("Шаг 10")