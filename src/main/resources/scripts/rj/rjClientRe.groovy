import com.prime.db.rnkb.model.ClientRelation
import com.prime.db.rnkb.model.commucation.judgment.RjClient
import com.prime.db.rnkb.model.commucation.judgment.RjClientRe


def rjClient = execution.getVariable("rjClient") as RjClient


def clientRelation = execution.getVariable("clientRelationBase") as List<ClientRelation>

List<RjClientRe> rjClientReList = new ArrayList<>()

print(clientRelation.size())
if (clientRelation != null && clientRelation.size() > 0) {
    clientRelation.each {it ->
        RjClientRe rjClientRe = new RjClientRe()
        rjClientRe.rjclient = rjClient
        if (it.toClientId != null) {
            rjClientRe.fullname = it.toClientId.fullName
            if (it.toClientId.clientIndividual != null) {
                rjClientRe.birthDate = it.toClientId.clientIndividual.birthdate
            }
            if (it.toClientId.inn != null) {
                rjClientRe.inn = Long.valueOf(it.toClientId.inn)
            }
            if (it.toClientId.ogrn != null) {
                rjClientRe.ogrn = Long.valueOf(it.toClientId.ogrn)
            }

            rjClientRe.clientMark = it.toClientId.clientMark
        }
        rjClientRe.toClientId = it.toClientId
        rjClientRe.relationType = it.relationType
        rjClientRe.isEIOChild = it.iseiochild
        rjClientRe.isEIOToDateDate = it.iseiotodatedate
        rjClientRe.isEIOFromDate = it.iseiofromdate
        rjClientRe.sharePercent = it.sharepercent
        rjClientReList.add(rjClientRe)
    }
    execution.setVariable("rjClientReList",rjClientReList)
    execution.setVariable("emptyrjClientReList",false)

}
else {
    execution.setVariable("emptyrjClientReList",true)
}