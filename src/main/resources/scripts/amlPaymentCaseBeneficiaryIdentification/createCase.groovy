package scripts.amlPaymentCaseBeneficiaryIdentification

import com.prime.db.rnkb.model.Case
import com.prime.db.rnkb.model.BaseDictionary
import java.time.LocalDateTime

BaseDictionary getBd(int type, String code) {
    baseDictRepo.getByBaseDictionaryTypeCodeAndCode(type, code);
}

def caseType = execution.getVariable("caseType");
def user = execution.getVariable("user");

def status = getBd(233, "1")
def caseStatus = getBd(299, "1")

Case caseData = new Case()
if (getBd(233, "1") != null) {
    caseData.name = getBd(233, "1").getType().getNameRus()
}
caseData.caseObjectType = getBd(14, "2")
caseData.setCreationdate(LocalDateTime.now())
caseData.setCaseType(getBd(18, caseType))
caseData.author = user
caseData.status = status
caseData.caseStatus = caseStatus

execution.setVariable("caseData", caseData);