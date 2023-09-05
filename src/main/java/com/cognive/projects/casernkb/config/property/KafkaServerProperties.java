package com.cognive.projects.casernkb.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author Yegor Kuzmin (keelfy)
 */
@Data
@ConfigurationProperties(prefix = "server.kafka")
public class KafkaServerProperties {
    private ServiceTopicProperties camunda;
    private ServiceTopicProperties csm;
    private ServiceTopicProperties pipeline;
    private ServiceTopicProperties kyc;
    private ServiceTopicProperties kycOperation;
    private ServiceTopicProperties rj;
    private ServiceTopicProperties bi;
    private ServiceTopicProperties caseSendDecision;
    private String errorTopic;
    private String zkAmlRequestOutputTopic;
    private String zkAmlResponseOutputTopic;

    public List<ServiceTopicProperties> getPropertiesList() {
        return List.of(camunda, csm, pipeline, kyc, kycOperation, rj, bi, caseSendDecision);
    }
}
