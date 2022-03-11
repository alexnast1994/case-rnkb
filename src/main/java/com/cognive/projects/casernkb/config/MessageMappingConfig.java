package com.cognive.projects.casernkb.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
@Component
@AllArgsConstructor
@ConfigurationProperties(prefix = "spring.cloud.stream")
public class MessageMappingConfig {
    private Map<String, BindingsItem> bindings;
    //private Map<String, String> bindings;

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
