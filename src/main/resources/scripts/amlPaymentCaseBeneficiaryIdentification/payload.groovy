package scripts.amlPaymentCaseBeneficiaryIdentification

import static org.camunda.spin.Spin.JSON

def jsonStr = execution.getVariable("payload")
def jsonData = JSON(jsonStr)
def paymentEvents = jsonData.prop("RequestData").prop("PaymentEvents").elements()
if(paymentEvents.size() > 0) {
    def paymentEvent = paymentEvents.get(0);
    def beneficiaryIsIdentified = paymentEvent.prop("BeneficiaryIsIdentified").boolValue()
    def paymentReference = paymentEvent.prop("PaymentReference").stringValue()
    def checkFlagBeneficiary = paymentEvent.prop("CheckFlagBeneficiary").isNull()
    Boolean checkCaseIsPresent = caseRepo.findCaseByPaymentreference(paymentReference).isPresent()
    execution.setVariable("beneficiaryIsIdentified", beneficiaryIsIdentified)
    execution.setVariable("paymentEvent", paymentEvent)
    execution.setVariable("paymentReference", paymentReference)
    execution.setVariable("checkFlagBeneficiary", checkFlagBeneficiary)
    execution.setVariable("checkCaseIsPresent", checkCaseIsPresent)
    execution.setVariable("caseType", "6")
} else {
    throw new RuntimeException("Invalid PaymentEvents");
}