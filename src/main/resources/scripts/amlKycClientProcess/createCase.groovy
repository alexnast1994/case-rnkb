import com.prime.db.rnkb.model.Case
import com.prime.db.rnkb.model.BaseDictionary
import java.time.LocalDateTime
import static org.camunda.spin.Spin.JSON

def pld = execution.getVariable("payload")
def json = JSON(pld)


BaseDictionary getBd(int type, String code) {
    baseDictRepo.getByBaseDictionaryTypeCodeAndCode(type, code);
}
println "Старт формирования кейса"
def results = json.prop("Clients").elements()[0].prop("Results").elements().findAll {r -> r.hasProp("record")}
List<Case> caseList = new ArrayList<>()
if (results.size() > 0) {
    results.each {r ->
        try {
            com.prime.db.rnkb.model.Case caseNew = new com.prime.db.rnkb.model.Case()
            caseNew.setCaseType(getBd(18, "5"))
            caseNew.setName(getBd(18,"5").getName())
            caseNew.setStatus(r.prop("rulesResult").elements()[0].prop("autoDecision").boolValue() == true ? getBd(286, "3") : getBd(286, "1"))
            caseNew.setCreationdate(LocalDateTime.now())
            caseList.add(caseNew)
        }
        catch (Exception ignored) {
            println("Не удалось сформировать кейс, выполнение процесса отменено")
        }
    }

}

if (caseList.size() > 0) {
    execution.setVariable("case", caseList)
    execution.setVariable("createCaseError",false)
}
else {
    execution.setVariable("createCaseError",true)
}