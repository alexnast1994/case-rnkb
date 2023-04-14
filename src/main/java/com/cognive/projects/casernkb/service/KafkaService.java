package com.cognive.projects.casernkb.service;

import com.cognive.projects.casernkb.model.zk_request.AMLResponse;

public interface KafkaService {
    void commonMessageOutput(String messageId, String key, String data);

    void sendZkRequestAnswer(AMLResponse response);

    void sendZkResponseAnswer(AMLResponse response);

    void sendError(String processName, String key, Exception ex);

    void sendSimpleMessage(String topic, String message, String key);
}
