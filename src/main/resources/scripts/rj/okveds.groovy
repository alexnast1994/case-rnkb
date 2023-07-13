
import com.cognive.projects.casernkb.model.projection.BranchGroup
import com.cognive.projects.casernkb.model.projection.Egrip
import com.cognive.projects.casernkb.model.projection.Egrul
import com.cognive.projects.casernkb.model.projection.OkvedProj
import com.cognive.projects.casernkb.model.projection.RosstatOkveds
import com.prime.db.rnkb.model.Client
import com.prime.db.rnkb.model.Okved
import com.prime.db.rnkb.model.commucation.judgment.RjClient
import com.prime.db.rnkb.model.commucation.judgment.RjOkved
import com.prime.db.rnkb.model.egrip.ListEgripInfoIndividual
import com.prime.db.rnkb.model.egrip.ListEgripOkved
import com.prime.db.rnkb.model.egrul.ListEgrulInfoOkvedType
import com.prime.db.rnkb.model.egrul.ListEgrulInfoUl
import org.apache.tomcat.jni.Local

import java.time.LocalDateTime

def rjOkved = new RjOkved()

Egrul getEgrulByInn(String inn) {
    egrulRepo.getEgrulByInn(inn)
}

OkvedProj getEgrulOkvedByInn(String inn) {
    egrulRepo.getEgrulOkvedByInn(inn)
}

Egrip getEgripByInn(String inn) {
    egripRepo.getEgripByInn(inn)
}

OkvedProj getEgripOkvedByInn(String inn) {
    egripRepo.getEgripOkvedByInn(inn)
}

OkvedProj getOkvedByInn(String inn) {
    okvedRepo.getOkvedByInn(inn)
}

def client = execution.getVariable("clientBase") as Client
def okveds =  execution.getVariable("okveds") as ArrayList<Okved>
def okved = okveds != null && !okveds.isEmpty() && okveds.size() > 0 ? okveds.get(0) : null
def egrul = getEgrulOkvedByInn(client.inn)
def egrip = getEgripOkvedByInn(client.inn)
boolean egrName = false
boolean egrCode = false

List<RosstatOkveds> getBranchGroupOkveds(Long branchGroupId, String okvedCode) {
    lstRosstatOkvedRepo.findRosstatOkveds(branchGroupId, okvedCode)
}

BranchGroup getBranchGroup(Long clientId, String dateStart, String dateEnd) {
    filedAggGeneralRepo.findBranch(clientId, dateStart, dateEnd)
}

Long getCountOkved(Long branchGroupId, String codeOkved) {
    lstRosstatOkvedRepo.getCountOkved(branchGroupId, codeOkved)
}

//OKVED_NAME
if (egrul != null && egrul.name != null) {
    println "ЕГРЮЛ"
    rjOkved.okvedname = egrul.name
    egrName = true
} else if (egrip != null && egrip.name != null) {
    println "ЕГРИЛ"
    rjOkved.okvedname = egrip.name
    egrName = true
} else if (okved != null){
    println "ОКВЭД"
    rjOkved.okvedname = okved.name
}

//OKVED_CODE
if (egrul != null && egrul.code != null) {
    println "ЕГРЮЛ"
    rjOkved.okvedcode = egrul.code
    egrCode = true
} else if (egrip != null && egrip.code != null) {
    println "ЕГРИЛ"
    rjOkved.okvedcode = egrip.code
    egrCode = true
} else if (okved != null){
    rjOkved.okvedcode = okved.code
}

//ISOKVED


Long clientId = execution.getVariable("clientId") as Long
String dateStart = execution.getVariable("startDate") as String
String dateEnd = execution.getVariable("offDate") as String
BranchGroup branch = getBranchGroup(clientId, dateStart.substring(0, 10), dateEnd.substring(0, 10))
println "Полученный объект branch: " + branch


if (branch != null) {
    String branchGroup = branch.getBranch_group()
    Long branchGroupIds = branchGroup as Long


    rjOkved.isokved = getCountOkved(branchGroupIds, rjOkved.okvedcode) != null && getCountOkved(branchGroupIds, rjOkved.okvedcode) > 0

    rjOkved.branchGroupName = branch.getBranch_group_name() as String
}

rjOkved.egrulip = egrName && egrCode
rjOkved.rjClient = execution.getVariable("rjClient") as RjClient
println rjOkved.toString()

execution.setVariable("rjOkved", rjOkved)