package com.cognive.projects.casernkb.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Data
@ConfigurationProperties(prefix = "zk")
public class ZkProperties {
    private String legalDocPattern;
    private String legalDocPatternTable;
    private String individualDocPattern;
    private String individualDocPatternTable;

    public InputStream getPattern(String name) throws IOException {
        Resource resource = new ClassPathResource("docx/" + name);
        return new FileInputStream(resource.getFile());
    }
}
