import com.prime.db.rnkb.model.af.AfBlockAttributesList
import com.prime.db.rnkb.model.commucation.judgment.RjBlockAttributesList
import com.prime.db.rnkb.model.commucation.judgment.RjClient

import java.time.LocalDateTime


Long clientId = execution.getVariable("clientId") as Long
String dateStart = execution.getVariable("startDate") as String
String dateEnd = execution.getVariable("offDate") as String

List<AfBlockAttributesList> getBlocks(Long clientId, LocalDateTime startDate, LocalDateTime offDate) {
    blockAttrRepo.getBlocks(clientId, startDate, offDate)
}

List<AfBlockAttributesList> afBlockAttributesLists = getBlocks(clientId, LocalDateTime.parse(dateStart), LocalDateTime.parse(dateEnd))

def rjClient = execution.getVariable("rjClient") as RjClient
List<RjBlockAttributesList> rjBlockAttributesLists = new ArrayList<>()

if (!afBlockAttributesLists.isEmpty() && afBlockAttributesLists != null) {
    afBlockAttributesLists.each {a ->
        RjBlockAttributesList rjBlockAttributesList = new RjBlockAttributesList()
        rjBlockAttributesList.rjClientId = rjClient
        rjBlockAttributesList.productName = a.productName
        rjBlockAttributesList.productNumber = a.productNumber
        rjBlockAttributesList.dateBlock = a.dateBlock
        rjBlockAttributesList.dateUnblock = a.dateUnBlock
        rjBlockAttributesLists.add(rjBlockAttributesList)

    }
    execution.setVariable("emptyAfBlockAttributesList", false)
    execution.setVariable("rjBlockAttributesLists", rjBlockAttributesLists)
}
else {
    println("Не найдено AfBlockAttributesList")
    execution.setVariable("emptyAfBlockAttributesList", true)
}