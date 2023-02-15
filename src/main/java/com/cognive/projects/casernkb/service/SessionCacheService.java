package com.cognive.projects.casernkb.service;

import org.hibernate.Session;

public interface SessionCacheService {
    void putSession(String businessKey, Session session);

    void removeSession(String businessKey);

    void closeAndRemove(String businessKey);
}
