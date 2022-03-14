package com.cognive.projects.casernkb.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Message to topic mapping
 * Spring map topics to binding
 * Camunda operate messageId
 */
@Getter
@Setter
@Component
@AllArgsConstructor
@ConfigurationProperties(prefix = "spring.cloud.stream")
public class BindingMappingConfig {
    private Map<String, BindingsItem> bindings;
    private final Map<String, String> topicBindingMap = new ConcurrentHashMap<>();

    public String getBinding(String topicName) {
        if(this.topicBindingMap.isEmpty()) {
            bindings.forEach((x, y) -> {
                String[] topics = y.getDestination().split(",");
                for(String t : topics) {
                    topicBindingMap.put(t, x);
                }
            });
        }
        return topicBindingMap.get(topicName);
    }

    @Getter
    @Setter
    public static class BindingsItem {
        private String destination;
        private String group;
        private String binder;
    }
}
