package com.cognive.projects.casernkb.delegate;

import com.cognive.projects.casernkb.service.SessionCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class CloseSessionRnkbDelegate implements JavaDelegate {
    private final SessionCacheService sessionCacheService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        sessionCacheService.closeAndRemove(execution.getBusinessKey());
        log.info("Session closed");
    }
}
