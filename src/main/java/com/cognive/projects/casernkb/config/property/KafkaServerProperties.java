package com.cognive.projects.casernkb.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

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
    private String errorTopic;
    private String zkAmlRequestOutputTopic;
    private String zkAmlResponseOutputTopic;
}
