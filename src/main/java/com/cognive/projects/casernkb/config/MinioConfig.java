package com.cognive.projects.casernkb.config;

import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import io.minio.MinioClient;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Data
@Configuration
public class MinioConfig {

    @Value("${minio.endpoint}" )
    private String endpoint;

    @Value("${minio.port}" )
    private Integer port;

    @Value("${minio.accessKey}" )
    private String accessKey;

    @Value("${minio.secretKey}" )
    private String secretKey;

    @Value("${minio.secure}" )
    private boolean secure;

    @Value("${minio.bucket-name}" )
    private String bucketName;

    @Value("${minio.file-size}" )
    private long fileSize;

    @Bean
    public MinioClient minioClient() {
        MinioClient minioClient =
                MinioClient.builder()
                        .credentials(accessKey, secretKey)
                        .endpoint(endpoint)
                        .build();
        return minioClient;
    }
/*
    @PostConstruct
    private void setTypeToFileUtilType(){
        FileTypeUtil.putFileType("6431303a637265617465345jf65gfg","json");
    }
    */

}
