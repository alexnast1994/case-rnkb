import com.prime.db.rnkb.model.BaseDictionary
import com.prime.db.rnkb.model.Client
import com.prime.db.rnkb.model.ClientIndividual
import com.prime.db.rnkb.model.ClientRelation
import com.prime.db.rnkb.model.commucation.judgment.RjClient
import com.prime.db.rnkb.model.commucation.judgment.RjClientRe
import org.slf4j.LoggerFactory

def log = LoggerFactory.getLogger(this.class)

def rjClient = execution.getVariable("rjClient") as RjClient
def client = execution.getVariable("clientBase") as Client
def rjClientRe = new RjClientRe()

def clientRelation = execution.getVariable("clientRelationBase") as ClientRelation


rjClientRe.rjclient = rjClient
rjClientRe.exToClientId = client
rjClientRe.toClientId = client
if (clientRelation != null) {
    if (clientRelation.toClientId != null) {
        rjClientRe.fullname = clientRelation.toClientId.fullName
        if (clientRelation.toClientId.clientIndividual != null) {
            rjClientRe.birthDate = clientRelation.toClientId.clientIndividual.birthdate
        }

    }
    rjClientRe.relationType = clientRelation.relationType
    rjClientRe.isEIOChild = clientRelation.iseiochild
    if (client.inn != null) {
        rjClientRe.inn = Long.valueOf(client.inn)
    }
    if (client.ogrn != null) {
        rjClientRe.ogrn = Long.valueOf(client.ogrn)
    }
    rjClientRe.isEIOToDateDate = clientRelation.iseiotodatedate
    rjClientRe.isEIOFromDate = clientRelation.iseiofromdate
    rjClientRe.sharePercent = clientRelation.sharepercent
}
def baseDic = client.clientMark as BaseDictionary
rjClientRe.clientMark = baseDic

return rjClientRe