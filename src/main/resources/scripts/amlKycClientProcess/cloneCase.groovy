import com.prime.db.rnkb.model.BaseDictionary
import com.prime.db.rnkb.model.Case

import java.time.LocalDateTime

Case aCase = execution.getVariable("cloneCase")

BaseDictionary getBd(int type, String code) {
    baseDictRepo.getByBaseDictionaryTypeCodeAndCode(type, code);
}
try {
    Case bCase = new Case()
    bCase.status = getBd(286, "2")
    bCase.caseType = aCase.caseType
    bCase.name = aCase.name
    bCase.status = aCase.status
    bCase.creationdate = LocalDateTime.now()
    execution.setVariable("bCase", bCase)
    execution.setVariable("cloned", true)
    println("Склонирован новый кейс")
}
catch (Exception e) {
    println("Не удалось склонировать кейс")
    execution.setVariable("cloned", false)
}