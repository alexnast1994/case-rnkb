import com.prime.db.rnkb.model.commucation.judgment.RjCounterparty

Long clientId = execution.getVariable("clientId") as Long
String dateStart = execution.getVariable("startDate") as String
String dateEnd = execution.getVariable("offDate") as String

List<RjCounterparty> rjCounterparties = execution.getVariable("rjCounterparties") as List<RjCounterparty>

