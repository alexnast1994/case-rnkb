import static org.camunda.spin.Spin.JSON

def pld = execution.getVariable("payload")
def json = JSON(pld)

try {
    def kycLists = json
            .prop("ResponseData")
            .prop("ClientCheckResult")
            .elements()[0]
            .prop("KYCList").elements().any {k ->
        k.prop("CheckStatus").stringValue() == "1" || k.prop("CheckStatus").stringValue() == "2"}
    println("Завершить процесс? " + !kycLists)
    execution.setVariable("kycLists", kycLists)
}
catch (Exception e) {
    execution.setVariable("kycLists", false)
}


