import static org.camunda.spin.Spin.JSON

def jsonStr = execution.getVariable("payload")
def jsonData = JSON(jsonStr)

def jsonObject = jsonData.prop("payload").prop("amlSaveClientCheckResult")

String exClientId = jsonObject.prop("exClientId").stringValue()
String module = jsonObject.prop("module").stringValue()

execution.setVariable("checkResults",jsonObject.prop("checkResult"))
execution.setVariable("exClientId",exClientId)
execution.setVariable("module",module)
execution.setVariable("jsonObject",jsonObject)