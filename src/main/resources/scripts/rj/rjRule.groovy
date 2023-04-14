import com.prime.db.rnkb.model.CaseRules
import com.prime.db.rnkb.model.commucation.judgment.ReasonedJudgment
import com.prime.db.rnkb.model.commucation.judgment.RjRule

List<CaseRules> getCaseRules(List<Long> ids) {
    caseRuleRepo.getCaseRules(ids)
}

def caseRules = getCaseRules(execution.getVariable("caseIds") as List<Long>)
if (!caseRules.isEmpty() && caseRules != null) {
    List<RjRule> rjRuleList = new ArrayList<>()

    caseRules.each {c ->
        RjRule rjRule = new RjRule()
        rjRule.rjId = execution.getVariable("reasonedJudgment") as ReasonedJudgment
        rjRule.fesCode = c.code
        rjRule.rule = c.ruleId
        rjRuleList.add(rjRule)
    }
    execution.setVariable("rjRuleList", rjRuleList)
    execution.setVariable("emptycaserules", false)
}
else {
    println("Не удалось получить caserules")
    execution.setVariable("emptycaserules", true)
}