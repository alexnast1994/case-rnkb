import com.prime.db.rnkb.model.commucation.judgment.ReasonedJudgment
import com.prime.db.rnkb.model.commucation.judgment.RjWorksheet

RjWorksheet rjWorksheet = new RjWorksheet()
ReasonedJudgment reasonedJudgment = execution.getVariable("reasonedJudgmentOut") as ReasonedJudgment
rjWorksheet.rjId = reasonedJudgment
execution.setVariable("rjWorksheet", rjWorksheet)