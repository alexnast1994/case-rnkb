package com.cognive.projects.casernkb.service;

import com.cognive.projects.casernkb.model.zk_request.AMLRequest;
import com.cognive.projects.casernkb.model.zk_request.AMLRequestStatus;
import com.cognive.projects.casernkb.model.zk_request.AMLResponse;

public interface ZkRequestService {
    void createObjectsAndSend(AMLRequest request);

    void createObjectsAndSend(AMLResponse response);

    void saveStatus(AMLRequestStatus request);
}
