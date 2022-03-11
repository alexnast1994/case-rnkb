package com.cognive.projects.casernkb.service;

public interface KafkaService {
    void commonMessageOutput(String messageId, String key, String data);
}
