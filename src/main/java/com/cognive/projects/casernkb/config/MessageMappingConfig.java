package com.cognive.projects.casernkb.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Message to topic mapping
 * Spring map topics to binding
 * Camunda operate messageId
 */
@Getter
@Setter
@Component
@AllArgsConstructor
@ConfigurationProperties(prefix = "server")
public class MessageMappingConfig {
    private Map<String, String> messageMapping;
    private final Map<String, String> reverseMessageMapping;

    public String getMessage(String topicName) {
        if(this.reverseMessageMapping.isEmpty()) {
            messageMapping.forEach((x, y) -> reverseMessageMapping.put(y, x));
        }
        return reverseMessageMapping.get(topicName);
    }

    public String getTopic(String messageId) {
        return messageMapping.get(messageId);
    }
}
