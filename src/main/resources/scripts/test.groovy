import static org.camunda.spin.Spin.JSON

def jsonStr = execution.getVariable("payload")
def jsonData = JSON(jsonStr)

def payloadObject = jsonData.prop("payload").prop("amlAutoReasonedJudgment")

def clientId = payloadObject.prop("clientId").numberValue().longValue()
def typeRj = payloadObject.prop("typeRj").stringValue()
def caseElements = typeRj != "3" && payloadObject.hasProp("caseIds") && payloadObject.prop("caseIds") != null && payloadObject.prop("caseIds").elements().size() > 0 ? payloadObject.prop("caseIds").elements() : null
def startDate = payloadObject.prop("startDate").stringValue()
def offDate = payloadObject.prop("offDate").stringValue()


def caseIds = []
if (caseElements != null) {
    execution.setVariable("nonCase", false)
    caseElements.each { caseElement ->
        caseIds.add(caseElement.numberValue().longValue())
    }
    def caseIdsString = caseIds.inject { first, second ->
        "$first,$second"
    }
    execution.setVariable("caseIds", caseIds)
    execution.setVariable("caseIdsString", caseIdsString)
}
else {
    execution.setVariable("nonCase", true)
}

execution.setVariable("clientId", clientId)
execution.setVariable("startDate", startDate)
execution.setVariable("offDate", offDate)
execution.setVariable("typeRj", typeRj)