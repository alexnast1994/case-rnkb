import com.prime.db.rnkb.model.ClLicense
import com.prime.db.rnkb.model.commucation.judgment.RjClient
import com.prime.db.rnkb.model.commucation.judgment.RjLicense

def rjLicense = new RjLicense()
def rjclient = execution.getVariable("rjClient") as RjClient
def clLicense = execution.getVariable("clientLicenseBase") as ClLicense

if(clLicense!=null){
    rjLicense.client = rjclient
    rjLicense.licensenumber = clLicense.licnumber
    rjLicense.licenseissueby = clLicense.issueby
    if(clLicense.issuedate!=null){rjLicense.licenseissuedate = clLicense.issuedate}
    if(clLicense.expirationdate!=null){
        rjLicense.licenseExpirationDate = clLicense.expirationdate}
    rjLicense.licenseactivitylist = clLicense.activitylist
    rjLicense.licenseType = clLicense.type

    rjLicense.licenseValidity = clLicense.validity
    execution.setVariable("rjLicense", rjLicense)
    execution.setVariable("emptyLicense", false)
}
else {
    execution.setVariable("emptyLicense", true)
}