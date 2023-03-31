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
try {
    com.prime.db.rnkb.model.Case caseNew = new com.prime.db.rnkb.model.Case()
    caseNew.setCaseType(getBd(18, "5"))
    caseNew.setName(getBd(18,"5").getName())
    caseNew.setStatus(json.prop("Clients").elements()[0].prop("Results").elements()[0].prop("rulesResult").elements()[0].prop("autoDecision").boolValue() == true ? getBd(286, "3") : getBd(286, "1"))
    caseNew.setCreationdate(LocalDateTime.now())

    execution.setVariable("createCaseError", false)
    execution.setVariable("case", caseNew)
}
catch (Exception e) {
    println("Не удалось сформировать кейс, выполнение процесса отменено")
    execution.setVariable("createCaseError", true)
}

