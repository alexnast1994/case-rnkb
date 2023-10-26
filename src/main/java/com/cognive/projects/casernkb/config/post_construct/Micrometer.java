package com.cognive.projects.casernkb.config.post_construct;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


@Component
@RequiredArgsConstructor
public class Micrometer {
    private final BuildProperties buildProperties;
    private final MeterRegistry meterRegistry;

    @PostConstruct
    private void meterRegistry() {
        Counter.builder("service_information")
                .description("service current information")
                .tag("group", buildProperties.getGroup())
                .tag("artifact", buildProperties.getArtifact())
                .tag("name", buildProperties.getName())
                .tag("version", buildProperties.getVersion())
                .register(meterRegistry);
    }
}
