import com.prime.db.rnkb.model.commucation.judgment.RjZkActionMidl
import com.prime.db.rnkb.model.commucation.judgment.Rjzkrequest
import com.prime.db.rnkb.model.commucation.midl.ActionsMIDL
import com.prime.db.rnkb.model.commucation.request.Request

List<ActionsMIDL> getActionMidl(List<Long> requests) {
    zkMiddlRepo.getActionMidl(requests)
}
List<Request> req = execution.getVariable("requestList") as List<Request>
List<ActionsMIDL> actionsMIDLList = getActionMidl(req.collect {r -> r.id})
println(actionsMIDLList)
List<Rjzkrequest> rjzkrequests = execution.getVariable("rjzkrequestListOut") as List<Rjzkrequest>
if (!actionsMIDLList.isEmpty() && actionsMIDLList != null) {
    List<RjZkActionMidl> rjZkActionMidlList = new ArrayList<>()
    actionsMIDLList.each {a ->
        RjZkActionMidl rjZkActionMidl = new RjZkActionMidl()
        rjZkActionMidl.rjZkRequestId = rjzkrequests.find { a.getIssueId().getId() == it.getZkRequestId().getZkTaskId().getId()}
        rjZkActionMidl.date = a.date
        rjZkActionMidl.time = a.time
        rjZkActionMidl.quantity = a.quantity
        rjZkActionMidlList.add(rjZkActionMidl)
    }
    execution.setVariable("rjZkActionMidlList",rjZkActionMidlList)
    execution.setVariable("emptyactionsMIDLList", false)
}
else {
    println("Не найдены actionsMIDLList")
    execution.setVariable("emptyactionsMIDLList", true)
}