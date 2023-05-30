import camundajar.impl.scala.util.parsing.json.JSON

import static org.camunda.spin.Spin.JSON

def jsonStr = execution.getVariable("payload") as String
def jsonData = JSON(jsonStr).prop("payload").prop("amlCsmKycPaymentRequest")

try {
    def participants = jsonData.prop("RequestData").prop("PaymentEvents").elements()[0].prop("Participants").elements().findAll{ it -> it.hasProp("KYCList") && it.hasProp("Results") && it.prop("Results") != null && it.prop("Results").elements().size() > 0 && it.prop("KYCList") != null && it.prop("KYCList").elements().size() > 0 && it.prop("KYCList").elements().find { k -> k.prop("CheckStatus").stringValue() == "1" || k.prop("CheckStatus").stringValue() == "2"}}.toString()
    def patJson = JSON(participants)
    if (participants.size() > 0) {
        execution.setVariable("jsonData", jsonData)
        println("Начало записи партисипантов")
        execution.setVariable("participants", patJson)
        execution.setVariable("otherPersons", other)
        println("Записали партисипанты")
        execution.setVariable("correctJson",  true)
    }
    else {
        println("Не удалось запустить процесс, отсутствуют ключевые поля")
        execution.setVariable("correctJson",  false)
    }
}
catch (Exception e) {
    println("Не удалось запустить процесс, отсутствуют ключевые поля")
    println(e.getLocalizedMessage())
    execution.setVariable("correctJson",  false)
}
