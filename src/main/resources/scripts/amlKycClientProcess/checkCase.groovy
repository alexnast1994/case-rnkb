import com.prime.db.rnkb.model.kyc.KycCaseClient

def cases = execution.getVariable("cases") as List<KycCaseClient>

println("Кейс: " + execution.getVariable("cases"))
if (cases != null && cases.size() > 0) {
    execution.setVariable("existCase", true)
}
else {
    execution.setVariable("existCase", false)
}

println("Существует кейс? " + execution.getVariable("existCase"))