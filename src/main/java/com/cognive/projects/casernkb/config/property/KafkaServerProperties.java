package com.cognive.projects.casernkb.config.property;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Yegor Kuzmin (keelfy)
 */
@Data
@ConfigurationProperties(prefix = "server.kafka")
public class KafkaServerProperties {

    private String processNameHeader;

    private String csmProcessNameHeader;

    private String csmKycOnlineClientProcessName;

    private String csmKycOfflineClientProcessName;

    private String csmProcessName;

    private String camundaCsmProcessName;

    private String camundaCsmKycClientProcessName;

    private String camundaPipelineProcessName;

    private String pipelineWorkflowId;

    private String errorTopic;

}
