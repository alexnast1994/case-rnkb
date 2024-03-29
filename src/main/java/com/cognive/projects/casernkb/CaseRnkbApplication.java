package com.cognive.projects.casernkb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@EntityScan(basePackages = {"com.prime.db.rnkb.model"})
@ComponentScan(basePackages = {"com.cognive.projects.casernkb.config",
        "com.cognive.projects.casernkb.utils",
        "com.cognive.projects.casernkb.delegate",
        "com.cognive.projects.casernkb.controller",
        "com.cognive.projects.casernkb.model",
        "com.cognive.projects.casernkb.scheduler",
        "com.cognive.projects.casernkb.service",
        "com.cognive.servicetasks",
        "com.cognive.platform",
        "com.prime.db.rnkb.repository",
        "com.prime.db.rnkb.service"})
@SpringBootApplication
@EnableJpaRepositories({"com.prime.db.rnkb.repository", "com.cognive.projects.casernkb.repo"})
//@EnableProcessApplication
@ConfigurationPropertiesScan("com.cognive.projects.casernkb.config.property")
@EnableScheduling
public class CaseRnkbApplication {
    public static void main(String[] args) {
        SpringApplication.run(CaseRnkbApplication.class, args);
    }

    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedHeader("*"); // this allows all headers
        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("HEAD");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("PATCH");
        config.addAllowedOriginPattern("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
