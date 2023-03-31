import com.prime.db.rnkb.model.Client
import com.prime.db.rnkb.model.ClientIndividual
import com.prime.db.rnkb.model.ClientLegal
import com.prime.db.rnkb.model.commucation.judgment.ReasonedJudgment
import com.prime.db.rnkb.model.commucation.judgment.RjClient

import java.sql.Timestamp
import java.time.LocalDateTime
import org.slf4j.LoggerFactory
def log = LoggerFactory.getLogger(this.class)

List<Timestamp> getDate(String inn) {
    egrulRepo.getDate(inn)
}

BigDecimal getShareCapital(String inn) {
    egrulRepo.getShareCapital(inn)
}

def rjClient = new RjClient()
def reasonedJudgment = execution.getVariable("reasonedJudgmentOut") as ReasonedJudgment

def client = execution.getVariable("clientBase") as Client

def clientLegal = new ClientLegal()
def clientIndividual = new ClientIndividual()

def share = getShareCapital(client.inn)
if(client.clientLegal!=null && share == null ) {
    println("Клиент - юл")
    clientLegal = client.clientLegal
    def sc = clientLegal.sharecapital != null ? clientLegal.sharecapital.longValue() : 0L
    println("Клиент - юл " + sc)
    rjClient.sharecapital = sc
}
else {
    rjClient.sharecapital = share == null ? 0L : share.longValue()
}

if(client.clientIndividual!=null) {
    println("Клиент - фл")
    clientIndividual = client.clientIndividual
}
rjClient.rjId = reasonedJudgment
rjClient.clientInn = client.inn

if(client.clientOfBankFromDate!=null){
    rjClient.clientOfBankFromDate = client.clientOfBankFromDate
}

if(clientIndividual.birthdate!=null){
    rjClient.clientBirthdate = clientIndividual.birthdate
}

if(client.ogrnDate!=null){
    rjClient.ogrndate = client.ogrnDate
}

rjClient.placeofbirth = clientIndividual.placeofbirth
rjClient.clientFullname = client.fullName
rjClient.manager = client.manager
rjClient.branch = client.exBranch
rjClient.fnsSegment = client.fnsSegment
rjClient.ogrn = client.ogrn
rjClient.typeOfClient = client.clientType

rjClient.client = client
rjClient.exClientId = client.exClientId
rjClient.untrueDate = !getDate(client.inn).isEmpty() && getDate(client.inn) != null ? getDate(client.inn).get(0).toLocalDateTime() : null
return rjClient