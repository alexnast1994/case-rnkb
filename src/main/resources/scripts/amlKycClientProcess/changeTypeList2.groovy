import com.prime.db.rnkb.model.BaseDictionary
import com.prime.db.rnkb.model.Case
import com.prime.db.rnkb.model.kyc.KycCaseClientList2

import static org.camunda.spin.Spin.JSON

def pld = execution.getVariable("payload")
def json = JSON(pld)


println("Шаг 1")
List<Case> getCases(String exId) {
    caseRepo.getLatestCaseByClientIdAndExIdWithoutTypeList(exId);
}
println("Шаг 2")

BaseDictionary getBd(int type, String code) {
    baseDictRepo.getByBaseDictionaryTypeCodeAndCode(type, code);
}

println("Шаг 3")
def cases = getCases(execution.getVariable("sourceId") as String).unique {it, it1 -> it.getId() <=> it1.getId()}
println(cases.toString())
println("Шаг 4")
List<Case> changeCases = []
println("Шаг 5")
List<Case> cloneCases = []
cases.each{c ->

    if (c.getStatus().getId() == getBd(286, "1").getId() || c.getStatus().getId() == getBd(286, "2").getId()) {
        println("Шаг 6")
        if (!cloneCases.any{i -> i.getId() == c.getId()}) {
            cloneCases.add(c)
        }
        if (!changeCases.any{i -> i.getId() == c.getId()}) {
            c.setStatus(getBd(286, "7"))
            changeCases.add(c)
        }

    }
    else if (c.getStatus().getId() == getBd(286, "3").getId()) {
        if (!cloneCases.any{i -> i.getId() == c.getId()}) {
            cloneCases.add(c)
            println("Шаг 7")
        }
    }
    else {
        println("Шаг 8")
    }
}
if (!changeCases.isEmpty()) {
    execution.setVariable("changeCases" ,changeCases)
    execution.setVariable("changeFlag" ,true)
}
else {
    execution.setVariable("changeFlag" ,false)
}
if (!cloneCases.isEmpty()) {
    execution.setVariable("cloneCases" ,cloneCases)
    execution.setVariable("cloneFlag" ,true)
}
else {
    execution.setVariable("cloneFlag" ,false)
}

