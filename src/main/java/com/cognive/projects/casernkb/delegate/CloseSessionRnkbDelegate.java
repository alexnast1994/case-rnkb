package com.cognive.projects.casernkb.delegate;

import com.cognive.projects.casernkb.service.SessionCacheService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CloseSessionRnkbDelegate implements JavaDelegate {
    private final SessionCacheService sessionCacheService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        sessionCacheService.closeAndRemove(execution.getBusinessKey());
    }
}
