package temp.rj

Long clientId = execution.getVariable("clientId") as Long
String dateStart = execution.getVariable("startDate") as String
String dateEnd = execution.getVariable("offDate") as String

List<String> getClientInn(Long clientId, String dateStart, String dateEnd) {
    filedAggGeneralRepo.getInn(clientId, dateStart, dateEnd)
}
List<String> inn = getClientInn(clientId, dateStart.substring(0,10), dateEnd.substring(0,10))
println inn