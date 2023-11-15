import static org.camunda.spin.Spin.JSON

def jsonStr = execution.getVariable("payload")
def jsonData = JSON(jsonStr)

def payloadObject = jsonData.prop("amlCreateCaseByClient")

def clientId = payloadObject.prop("clientId").numberValue().longValue()
def userId = payloadObject.prop("userId").numberValue().longValue()
def rule = payloadObject.prop("rule").stringValue()
def isOfm = payloadObject.prop("isOfm").boolValue()

execution.setVariable("clientId", clientId)
execution.setVariable("userId", userId)
execution.setVariable("rule", rule)
execution.setVariable("isOfm", isOfm)