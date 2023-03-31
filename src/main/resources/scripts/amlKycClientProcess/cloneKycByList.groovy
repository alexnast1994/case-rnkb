import com.prime.db.rnkb.model.Case
import com.prime.db.rnkb.model.kyc.KycCaseClientList2

Case aCase = execution.getVariable("cloneCases")
Case bCase = execution.getVariable("newCase")

List<KycCaseClientList2> kycCaseClientList2sOld1 = aCase.getKycCaseByLists()
List<KycCaseClientList2> kycCaseClientList2sOld = []
kycCaseClientList2sOld1.each {it ->
    KycCaseClientList2 k = new KycCaseClientList2()
    k.matchType = it.matchType
    k.typeList = it.typeList
    k.exId = it.exId
    k.num = it.num
    k.markers = it.markers
    k.nameMatch = it.nameMatch
    k.firstName = it.firstName
    k.middleName = it.middleName
    k.lastName = it.lastName
    k.levelBlocking = it.levelBlocking
    k.checkStatus = it.checkStatus
    k.isActive = it.isActive
    k.entityType = it.entityType
    k.dateStart = it.dateStart
    k.dateDeactivate = it.dateDeactivate
    k.numberOfSolution = it.numberOfSolution
    kycCaseClientList2sOld.add(k)
}
execution.setVariable("kycCaseClientList2sOld", kycCaseClientList2sOld1)

if (!kycCaseClientList2sOld.isEmpty()) {
    List<KycCaseClientList2> kycCaseClientList2sNew = []
    kycCaseClientList2sOld.each {it ->
        it.caseId = bCase
        kycCaseClientList2sNew.add(it)
    }
    execution.setVariable("kycCaseClientList2sNew",kycCaseClientList2sNew)
    execution.setVariable("caseByListClone", false)
}
else {
    println("Не удалось склонировать KycCaseByLists")
    execution.setVariable("caseByListClone", false)
}

