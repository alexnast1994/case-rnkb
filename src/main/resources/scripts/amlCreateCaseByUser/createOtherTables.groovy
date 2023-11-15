import com.prime.db.rnkb.model.BaseDictionary
import com.prime.db.rnkb.model.CaseClientNew
import com.prime.db.rnkb.model.CaseRules
import com.prime.db.rnkb.model.Client

BaseDictionary getBd(int type, String code) {
    return baseDictRepo.getByBaseDictionaryTypeCodeAndCode(type, code);
}

def caseData = execution.getVariable("caseDataOut")
String rule = execution.getVariable("rule");
Client client = execution.getVariable("requestClient")

def caseRelationList = []

client.okvedList.each { okved ->
    CaseClientNew caseClient = new CaseClientNew().with {
        it.caseId = caseData
        it.clientId = client
        it.inn = client?.inn
        it.kpp = client?.clientLegal?.kpp
        it.clientType = client?.clientType
        it.registrationDate = client?.formCreationDate
        it.openingDate = client?.clientOfBankFromDate
        it.addressOfService = client?.branch?.branchname
        it.contactKm = client?.manager
        it.clientName = client?.fullName
        it.clientStatus = client?.clientMark
        it.okvedName = okved?.name
        it.okvedCode = okved?.code
        return it
    }
    caseRelationList << caseClient
}

CaseRules cr = new CaseRules()
cr.ruleId = getBd(272, rule)
cr.caseId = caseData

caseRelationList << cr

execution.setVariable("caseRelationList", caseRelationList)
