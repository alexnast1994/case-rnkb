import com.prime.db.rnkb.model.BaseDictionary
import com.prime.db.rnkb.model.Client
import com.prime.db.rnkb.model.ClientRbs
import com.prime.db.rnkb.model.ClientRbsBlock
import com.prime.db.rnkb.model.commucation.judgment.RjClient
import com.prime.db.rnkb.model.commucation.judgment.RjRbs

import java.time.LocalDate
import java.time.LocalDateTime


BaseDictionary getStatus(String code) {
    baseDictRepo.getByBaseDictionaryTypeCodeAndCode(269, code);
}

def client = execution.getVariable("clientBase") as Client
def rjClient = execution.getVariable("rjClient") as RjClient
def clientRbsBlock1 = execution.getVariable("rbsBlockBase") as List<ClientRbsBlock>
List<RjRbs> rjRbs1 = new ArrayList<>()

List<ClientRbs> getRbs(Long id) {
    clientRbsRepo.getRbs(id)
}
List<ClientRbsBlock> clientRbsBlock = new ArrayList<>()
List<ClientRbs> rbs = getRbs(client.id)
rbs.each {it ->
    clientRbsBlock.addAll(it.clientRbsBlockList)
}
if (clientRbsBlock != null) {
    clientRbsBlock.each {
        println("Итерация")
        def rjRbs = new RjRbs()
        def clientRbs = new ClientRbs()
        clientRbs = it.clientRbs
        rjRbs.blockdate = it.agreementBlockDate
        rjRbs.unblockdate = it.agreementUnblockDate
        if (clientRbs != null) {
            def rjRbsDic = clientRbs.status as BaseDictionary
            if (rjRbsDic != null) {
                rjRbs.status = getStatus(rjRbsDic.code)
            }
        }
        if (client != null && client.id != null) {
            rjRbs.dboClientId = client.id.toString()
        }
        rjRbs.rjclient = rjClient
        rjRbs1.add(rjRbs)
    }
}


return rjRbs1


