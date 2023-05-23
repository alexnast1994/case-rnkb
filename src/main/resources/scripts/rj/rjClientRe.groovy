import com.prime.db.rnkb.model.ClientRelation
import com.prime.db.rnkb.model.commucation.judgment.RjClient
import com.prime.db.rnkb.model.commucation.judgment.RjClientRe


def rjClient = execution.getVariable("rjClient") as RjClient
def rjClientRe = new RjClientRe()

def clientRelation = execution.getVariable("clientRelationBase") as ClientRelation


rjClientRe.rjclient = rjClient

if (clientRelation != null) {
    if (clientRelation.toClientId != null) {
        rjClientRe.fullname = clientRelation.toClientId.fullName
        if (clientRelation.toClientId.clientIndividual != null) {
            rjClientRe.birthDate = clientRelation.toClientId.clientIndividual.birthdate
        }
        if (clientRelation.toClientId.inn != null) {
            rjClientRe.inn = Long.valueOf(clientRelation.toClientId.inn)
        }
        if (clientRelation.toClientId.ogrn != null) {
            rjClientRe.ogrn = Long.valueOf(clientRelation.toClientId.ogrn)
        }

        rjClientRe.clientMark = clientRelation.toClientId.clientMark
    }
    rjClientRe.toClientId = clientRelation.toClientId
    rjClientRe.relationType = clientRelation.relationType
    rjClientRe.isEIOChild = clientRelation.iseiochild
    rjClientRe.isEIOToDateDate = clientRelation.iseiotodatedate
    rjClientRe.isEIOFromDate = clientRelation.iseiofromdate
    rjClientRe.sharePercent = clientRelation.sharepercent
}

return rjClientRe