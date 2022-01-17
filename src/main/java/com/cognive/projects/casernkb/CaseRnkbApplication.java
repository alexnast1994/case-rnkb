package com.cognive.projects.casernkb;

import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.ArrayList;
import java.util.List;

@EntityScan(basePackages = {"com.prime.db.rnkb.model"})
@ComponentScan(basePackages = {"com.cognive.projects.kafkaReader",
                               "com.cognive.projects.casernkb.config",
                               "com.cognive.projects.casernkb.utils",
                               "com.cognive.projects.casernkb.delegate",
                               "com.cognive.projects.casernkb.controller",
                               "com.cognive.projects.casernkb.model",
                               "com.cognive.projects.casernkb.scheduler",
                               "com.cognive.projects.casernkb.service",
                               "com.cognive.servicetasks",
                               "com.cognive.platform",
                               "com.prime.db.rnkb.repository"})
@SpringBootApplication
@EnableJpaRepositories({"com.prime.db.rnkb.repository","com.cognive.projects.casernkb.repo"})
//@EnableProcessApplication
@EnableScheduling
public class CaseRnkbApplication {
    public static void main(String[] args) {SpringApplication.run(CaseRnkbApplication.class, args);}
    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*"); // this allows all origin
        config.addAllowedHeader("*"); // this allows all headers
        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("HEAD");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("PATCH");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
