package temp.rj

import com.prime.db.rnkb.model.Address
import com.prime.db.rnkb.model.commucation.judgment.RjAddress
import com.prime.db.rnkb.model.commucation.judgment.RjClient

def addresses = execution.getVariable("addresses") as List<Address>
List<RjAddress> addressList = new ArrayList<>()
if (addresses != null && addresses.size()>0) {
    addresses.each {address ->
        RjAddress rjAddress = new RjAddress()
        rjAddress.rjclient = execution.getVariable("rjClient") as RjClient
        rjAddress.addressLine = address.addressLine
        rjAddress.addressType = address.type
        addressList.add(rjAddress)
    }
}
if (!addressList.isEmpty() && addressList != null && addressList.size() >0) {
    execution.setVariable("addressList", addressList)
    execution.setVariable("emptyAddressList", false)
}
else {
    execution.setVariable("emptyAddressList", true)
}
