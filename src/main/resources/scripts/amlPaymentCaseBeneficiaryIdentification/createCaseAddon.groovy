package scripts.amlPaymentCaseBeneficiaryIdentification

import com.prime.db.rnkb.model.CaseOperation

def payment = execution.getVariable("payment")
def caseData = execution.getVariable("caseDataOut")

def caseRelationList = []

CaseOperation caseOperation = new CaseOperation()

caseOperation.setCaseId(caseData);
caseOperation.setPaymentId(payment)
caseOperation.setDateoper(payment.getDateIn())
caseOperation.setAmount(payment.getAmountNationalCurrency())
caseOperation.setPayeeaccountnumber(payment.getPayeeAccountNumber())
caseOperation.setPayeraccountnumber(payment.getPayerAccountNumber())

if(payment.getBankPayerId() != null)
    caseOperation.setPayerbankname(payment.getBankPayerId().getName())
if(payment.getBankPayeeId() != null)
    caseOperation.setPayeebankname(payment.getBankPayeeId().getName())

caseOperation.setPayername(payment.getPayerName())
caseOperation.setPayeename(payment.getPayeeName())
caseOperation.setType(payment.getType())
caseOperation.setCurrency(payment.getCurrency())
caseOperation.setPayerClientId(payment.getPayerClientId())
caseOperation.setPayeeClientId(payment.getPayeeClientId())

caseRelationList.add(caseOperation)

execution.setVariable("caseRelationList", caseRelationList)
