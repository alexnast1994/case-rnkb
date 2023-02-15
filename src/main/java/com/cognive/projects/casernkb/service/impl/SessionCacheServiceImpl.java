package com.cognive.projects.casernkb.service.impl;

import com.cognive.projects.casernkb.service.SessionCacheService;
import org.hibernate.Session;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
@Primary
public class SessionCacheServiceImpl implements SessionCacheService {
    private final ConcurrentHashMap<String, Session> cache = new ConcurrentHashMap<>();

    @Override
    public void putSession(String businessKey, Session session) {
        cache.put(businessKey, session);
    }

    @Override
    public void removeSession(String businessKey) {
        cache.remove(businessKey);
    }

    @Override
    public void closeAndRemove(String businessKey) {
        var session = cache.get(businessKey);
        if (session.isOpen()) {
            session.getTransaction().rollback();
            session.close();
        }
        removeSession(businessKey);
    }
}
