import static org.camunda.spin.Spin.JSON

def jsonStr = execution.getVariable("payload") as String
def jsonData = JSON(jsonStr).prop("payload").prop("amlCsmKycClientRequest")
def jsonFull = JSON(jsonStr).prop("payload")
def executionId = jsonStr.contains("executionId")
def clientCheckResult = jsonData.prop("ResponseData").prop("ClientCheckResult").elements()
execution.setVariable("sourceId", clientCheckResult[0].prop("SourceId").stringValue())
execution.setVariable("kycSourceId", jsonFull.hasProp("InitialClient") && jsonFull.prop("InitialClient") != null && jsonFull.prop("InitialClient").hasProp("RequestData")  ? jsonFull.prop("InitialClient").prop("RequestData").prop("Clients").elements()[0].prop("SourceId").stringValue() : clientCheckResult[0].prop("SourceId").stringValue())
execution.setVariable("payload", jsonData)
execution.setVariable("executionId", executionId)
execution.setVariable("payloadFull", jsonFull)