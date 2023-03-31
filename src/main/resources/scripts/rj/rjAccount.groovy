package temp.rj

import com.prime.db.rnkb.model.ClientAccount
import com.prime.db.rnkb.model.commucation.judgment.RjAccount
import com.prime.db.rnkb.model.commucation.judgment.RjClient

def clientAccounts = execution.getVariable("clientAccounts") as List<ClientAccount>

List<RjAccount> rjAccountList = new ArrayList<>()
if (clientAccounts != null && clientAccounts.size() > 0) {
    clientAccounts.each { clientAccount ->
        RjAccount rjAccount = new RjAccount()
        rjAccount.rjclient = execution.getVariable("rjClient") as RjClient
        rjAccount.accountcontracttype = clientAccount.accountcontracttype
        rjAccount.agreementnumber = clientAccount.agreementnumber
        rjAccount.accountnumber = clientAccount.accountnumber
        rjAccount.startdate = clientAccount.startdate
        rjAccount.closedate = clientAccount.closedate
        rjAccount.currency = clientAccount.currency
        rjAccountList.add(rjAccount)
    }
}
if (!rjAccountList.isEmpty() && rjAccountList != null && rjAccountList.size() > 0) {
    execution.setVariable("rjAccountList", rjAccountList)
    execution.setVariable("emptyAccountList", false)
}
else {
    execution.setVariable("emptyAccountList", true)
}
