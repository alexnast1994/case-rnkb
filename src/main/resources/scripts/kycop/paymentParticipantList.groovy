import com.prime.db.rnkb.model.BaseDictionary
import com.prime.db.rnkb.model.Client
import com.prime.db.rnkb.model.Payment
import com.prime.db.rnkb.model.PaymentParticipantList
import org.camunda.spin.SpinList
import org.camunda.spin.json.SpinJsonNode

import java.time.LocalDateTime

def json = execution.getVariable("jsonData") as SpinJsonNode


Payment getPayment(String exId) {
    paymentRepo.getPaymentByExid(exId)
}

Client getClient(String exClientId) {
    clientRepo.findClientByExid(exClientId)
}

BaseDictionary getBd(int type, String code) {
    baseDictRepo.getByBaseDictionaryTypeCodeAndCode(type, code);
}

def participant = execution.getVariable("participant") as SpinJsonNode
def otherPersons = json.hasProp("OtherPersons") && json.prop("OtherPersons") != null && json.prop("OtherPersons").elements().size() > 0 ? json.prop("OtherPersons") : null
List<PaymentParticipantList> participantListList = new ArrayList<>()
try {
    println("Старт формирования записи")
    def kycList = participant.prop("KYCList")
    participant.prop("KYCList").elements().findAll { k -> k.prop("CheckStatus").stringValue() == "1" || k.prop("CheckStatus").stringValue() == "2" }.each { it ->

        PaymentParticipantList participantList = new PaymentParticipantList()
        println(getPayment(json.prop("ResponseData").prop("PaymentCheckResult").elements()[0].prop("SourceId").stringValue()))
        participantList.paymentId = getPayment(json.prop("ResponseData").prop("PaymentCheckResult").elements()[0].prop("SourceId").stringValue())
        if (participant.hasProp("ClientId") && participant.prop("ClientId") != null && participant.prop("ClientId").stringValue() != "") {
            participantList.clientId = getClient(participant.prop("ClientId").stringValue())
        }

        if (participant.hasProp("PartType") && participant.prop("PartType") != null && participant.prop("PartType").numberValue() == 19) {

            participantList.otherPersonType = participant.hasProp("OtherPersonType") && participant.prop("OtherPersonType") != null ? getBd(169, participant.prop("OtherPersonType").stringValue()) : getBd(169, participant.prop("PartType").toString())

        }
        else {
            participantList.otherPersonType =  participant.hasProp("PartType") && participant.prop("PartType") != null ? getBd(169, participant.prop("PartType").toString()) : null

        }
        participantList.typeList = getBd(214, it.prop("Id").stringValue())
        participantList.levelBlocking = Integer.parseInt(it.prop("LevelBlocking").stringValue())
        participantList.checkStatus = Integer.parseInt(it.prop("CheckStatus").stringValue())
        participantList.createDate = LocalDateTime.now()
        StringBuilder markerString = new StringBuilder()
        if (participant.hasProp("Results") && participant.prop("Results") && participant.prop("Results").elements().size() > 0) {

            def result = participant.prop("Results").elements()
            def fields = result[0].fieldNames().findAll { it =~ /^[D,M][0-9]*$/ }
            participantList.exId = result[0].hasProp("record") && result[0].prop("record").hasProp("exId") ? result[0].prop("record").prop("exId").stringValue() : null
            participantList.num = result[0].hasProp("record") && result[0].prop("record").hasProp("num") ? result[0].prop("record").prop("num").stringValue() : null
            fields.each { it1 ->
                if ((result[0].prop(it1).isBoolean() && result[0].prop(it1).boolValue() != false)
                        || (result[0].prop(it1).isString() && result[0].prop(it1).stringValue() != "")
                        || (result[0].prop(it1).isNumber() && result[0].prop(it1).numberValue() != 0)) {
                    markerString.append(it1)
                    markerString.append("; ")
                }
            }
            participantList.setMarkers(markerString.size() > 0 ? markerString.toString().substring(0, markerString.toString().size() - 2) : "")
            participantList.nameMatch = (Integer) (participant.prop("Results").elements().size() > 0 && participant.prop("Results").elements()[0].hasProp("D0") ? participant.prop("Results").elements()[0].prop("D0").numberValue() : null)
            participantList.isActive = result[0].hasProp("record") && result[0].prop("record") != null && result[0].prop("record").hasProp("isActive") && result[0].prop("record").prop("isActive") != null ? result[0].prop("record").prop("isActive").boolValue() : null
            participantList.entityType = result[0].hasProp("record") && result[0].prop("record") != null && result[0].prop("record").hasProp("entityType") && result[0].prop("record").prop("entityType") != null ? result[0].prop("record").prop("entityType").stringValue() : null


            if (result[0].hasProp("record") && result[0].prop("record") != null) {
                execution.setVariable("isRecord", true)
                execution.setVariable("record", result[0].prop("record"))
            }
            else {
                execution.setVariable("isRecord", false)
            }
        }
        println("Список: " + participantList.toString())

        participantListList.add(participantList)
    }
    println("Размер participantListList: " + participantListList.size())
    if (participantListList.isEmpty()) {
        println("Не удалось создать participantList")
        execution.setVariable("emptyparticipantList", true)
    }
    else {
        execution.setVariable("participantListList", participantListList)
        execution.setVariable("emptyparticipantList", false)
    }

}
catch (Exception e) {
    println("Не удалось создать participantList")
    println(e.localizedMessage)
    execution.setVariable("emptyparticipantList", true)
}