package scripts.amlPaymentCaseBeneficiaryIdentification

Long respUserId = caseRepo.findFreeResponsibleUser()
execution.setVariable("respUserId", respUserId)

