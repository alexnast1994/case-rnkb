import com.prime.db.rnkb.model.BaseDictionary
import com.prime.db.rnkb.model.Case
import com.prime.db.rnkb.model.Client
import com.prime.db.rnkb.model.commucation.judgment.ReasonedJudgment

import java.time.LocalDateTime

BaseDictionary getStatus(String code) {
    baseDictRepo.getByBaseDictionaryTypeCodeAndCode(213, code);
}

BaseDictionary getJobStatus(String code) {
    baseDictRepo.getByBaseDictionaryTypeCodeAndCode(266, code);
}

BaseDictionary getTypeOfControl(String code) {
    baseDictRepo.getByBaseDictionaryTypeCodeAndCode(181, code);
}

BaseDictionary getTypeRj(String code) {
    baseDictRepo.getByBaseDictionaryTypeCodeAndCode(277, code);
}

BaseDictionary getClientType(String code) {
    baseDictRepo.getByBaseDictionaryTypeCodeAndCode(24, code);
}

ReasonedJudgment reasonedJudgment = new ReasonedJudgment()
if (!execution.getVariable("nonCase") as Boolean) {

    def case1 = execution.getVariable("caseBase") as Case
    def caseDic = case1.caseType as BaseDictionary

    if (caseDic.code == "3") {
        reasonedJudgment.typeOfControl = getTypeOfControl("1")
    }

    if (caseDic.code == "4") {
        reasonedJudgment.typeOfControl = getTypeOfControl("2")
    }

}
else {

    reasonedJudgment.typeOfControl = getTypeOfControl("3")

}


def typeRj = getTypeRj(execution.getVariable("typeRj"))

reasonedJudgment.status = getStatus("5")
Client client = execution.getVariable("clientBase")
reasonedJudgment.clientId = client
reasonedJudgment.jobStatus = getJobStatus("1")
reasonedJudgment.typeRj = typeRj

String conclusion = "По результатам проверки деятельности, анализа выписок и всех имеющихся у Банка документов и информации о Клиенте и его контрагентах"

if (client.clientType.code == "3" || client.clientType.code == "5") {
    if (typeRj.code == "1") {
        conclusion = conclusion + " деятельность Клиента соответствует заявленным масштабам, признаки сомнительности отсутствуют. Операции Клиента осуществляются в рамках заявленной деятельности и соответствуют общепринятой рыночной практике."
    }
    else if (typeRj.code == "2") {
        conclusion = conclusion + " признать операции Клиента, указанные в прилагаемом списке, подозрительными и направить по ним сообщения в Уполномоченный орган в установленный законом срок."
    }
}
else if (client.clientType.code == "4") {
    if (typeRj.code == "1") {
        conclusion = conclusion + " признаки сомнительности отсутствуют."
    }
    else if (typeRj.code == "2") {
        conclusion = conclusion + " признать операции Клиента, указанные в прилагаемом списке, подозрительными и направить по ним сообщения в Уполномоченный орган в установленный законом срок."
    }
}

reasonedJudgment.conclusion = conclusion

try {
    def startDateLDT = LocalDateTime.parse(execution.getVariable("startDate")) as LocalDateTime
    reasonedJudgment.startDate = startDateLDT
} catch (Exception e) {
    e.getMessage()
}

try {
    def offDateLDT = LocalDateTime.parse(execution.getVariable("offDate")) as LocalDateTime
    reasonedJudgment.offDate = offDateLDT
} catch (Exception e) {
    e.getMessage()
}

reasonedJudgment.developmentDate = LocalDateTime.now()
return reasonedJudgment
