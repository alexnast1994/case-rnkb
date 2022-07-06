package com.cognive.projects.casernkb.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Yegor Kuzmin (keelfy)
 */
@Data
@ConfigurationProperties(prefix = "server.kafka")
public class KafkaServerProperties {
    private ServiceProperties camunda;
    private ServiceProperties csm;
    private ServiceProperties pipeline;
    private String errorTopic;
}
