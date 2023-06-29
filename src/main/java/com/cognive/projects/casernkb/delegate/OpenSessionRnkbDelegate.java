package com.cognive.projects.casernkb.delegate;

import com.cognive.projects.casernkb.service.SessionCacheService;
import com.cognive.servicetasks.executesql.IExecuteSqlService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OpenSessionRnkbDelegate implements JavaDelegate {

    private final IExecuteSqlService executeSqlService;
    private final SessionCacheService sessionCacheService;

    public void execute(DelegateExecution ex) {
        var outputVarName = "session";


        if (!ex.hasVariable("session")) {
            if (ex.getVariable("session") == null) {
                Session session = this.executeSqlService.openSession();
                sessionCacheService.putSession(ex.getBusinessKey(), session);
                ex.setVariable(outputVarName, session);
            }

        }

    }
}
