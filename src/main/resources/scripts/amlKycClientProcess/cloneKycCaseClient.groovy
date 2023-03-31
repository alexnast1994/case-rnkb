import com.prime.db.rnkb.model.Case
import com.prime.db.rnkb.model.kyc.KycCaseClient
import com.prime.db.rnkb.model.kyc.KycCaseClientAddressList

Case aCase = execution.getVariable("cloneCase")
Case bCase = execution.getVariable("newCase")

List<KycCaseClient> kycCaseClientOld1 = aCase.getKycCaseClientList()
List<KycCaseClient> kycCaseClientOld = []
kycCaseClientOld1.each {it1 ->
    KycCaseClient k = new KycCaseClient()
    k.sourceId = it1.sourceId
    k.clientId = it1.clientId
    k.branchCode = it1.branchCode
    k.branchName = it1.branchName
    k.setCManagerName(it1.getCManagerName())
    k.clientType = it1.clientType
    k.clientMark = it1.clientMark
    k.fullName = it1.fullName
    k.inn = it1.inn
    k.ogrn = it1.ogrn
    k.ogrnDate = it1.ogrnDate
    k.kio = it1.kio
    kycCaseClientOld.add(k)
}
execution.setVariable("kycCaseClientOld", kycCaseClientOld1)

if (!kycCaseClientOld.isEmpty()) {
    List<KycCaseClient> kycCaseClientNew = []
    kycCaseClientOld.each {it ->
        it.caseId = bCase
        kycCaseClientNew.add(it)
    }
    execution.setVariable("kycCaseClientNew",kycCaseClientNew)
    execution.setVariable("kycCaseClientClone", true)
}
else {
    println("Не удалось склонировать kycCaseClient")
    execution.setVariable("kycCaseClientClone", false)
}