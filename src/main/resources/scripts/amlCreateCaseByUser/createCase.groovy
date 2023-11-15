import com.prime.db.rnkb.model.BaseDictionary
import com.prime.db.rnkb.model.Case

import java.time.LocalDateTime

BaseDictionary getBd(int type, String code) {
    return baseDictRepo.getByBaseDictionaryTypeCodeAndCode(type, code)
}

def user = execution.getVariable("requestUser")
def isOfm = execution.getVariable("isOfm")

Case caseData = new Case(
        name: getBd(18, isOfm ? "2" : "4").name,
        creationdate: LocalDateTime.now(),
        caseType: getBd(18, isOfm ? "2" : "4"),
        author: user,
        status: getBd(isOfm ? 104 : 140, "1"),
        caseStatus: getBd(isOfm ? 177 : 179, "1")
)

execution.setVariable("caseData", caseData)