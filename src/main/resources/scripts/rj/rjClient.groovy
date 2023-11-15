import com.prime.db.rnkb.model.Client
import com.prime.db.rnkb.model.commucation.judgment.ReasonedJudgment
import com.prime.db.rnkb.model.commucation.judgment.RjClient
import org.slf4j.LoggerFactory

import java.sql.Timestamp

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

rjClient.rjId = reasonedJudgment
rjClient.clientInn = client.inn
rjClient.numemployees = client.clientLegal?.numemployees
rjClient.clientOfBankFromDate = client.clientOfBankFromDate
rjClient.clientBirthdate = client.clientIndividual?.birthdate
rjClient.ogrndate = client.ogrnDate
rjClient.placeofbirth = client.clientIndividual?.placeofbirth
rjClient.clientFullname = client.fullName
rjClient.manager = client.manager
rjClient.branch = client.exBranch
rjClient.fnsSegment = client.fnsSegment
rjClient.ogrn = client.ogrn
rjClient.typeOfClient = client.clientType
rjClient.client = client
rjClient.exClientId = client.exClientId

def share = getShareCapital(client.inn)
rjClient.sharecapital = client.clientLegal ? (share?.longValue() ?: client.clientLegal.sharecapital?.longValue() ?: 0L) : share?.longValue()

def dateList = getDate(client.inn)
rjClient.untrueDate = !dateList?.isEmpty() ? dateList.get(0).toLocalDateTime() : null

return rjClient