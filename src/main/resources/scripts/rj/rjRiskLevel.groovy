import com.prime.db.rnkb.model.BaseDictionary
import com.prime.db.rnkb.model.RiskLevel
import com.prime.db.rnkb.model.commucation.judgment.RjClient
import com.prime.db.rnkb.model.commucation.judgment.RjRiskLevel

BaseDictionary getStatus(String code) {
    baseDictRepo.getByBaseDictionaryTypeCodeAndCode(245, code);
}

List<RiskLevel> riskLevelList = execution.getVariable("risklevel") as List<RiskLevel>
def rjClient = execution.getVariable("rjClient") as RjClient
List<RjRiskLevel> rjRiskLevels = new ArrayList<>()
if (!riskLevelList.isEmpty() && riskLevelList != null) {

    riskLevelList.each {r ->
        RjRiskLevel riskLevel = new RjRiskLevel()
        riskLevel.rjClientId = rjClient
        riskLevel.scoreSum = r.scoreSum
        riskLevel.levelRisk = r.levelRisk
        riskLevel.typeOfRisk = getStatus(r.typeOfRisk)
        rjRiskLevels.add(riskLevel)
    }
    execution.setVariable("rjRiskLevels", rjRiskLevels)
    execution.setVariable("emptyRiskLevel", false)
}
else {
    println("Не найдено записей RiskLevel")
    execution.setVariable("emptyRiskLevel", true)
}