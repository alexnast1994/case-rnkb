package com.cognive.projects.casernkb.config;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.camunda.spin.impl.json.jackson.format.JacksonJsonDataFormat;
import org.camunda.spin.spi.DataFormatConfigurator;
import org.springframework.context.annotation.Configuration;
import spinjar.com.fasterxml.jackson.databind.DeserializationFeature;
import spinjar.com.fasterxml.jackson.databind.ObjectMapper;

import java.time.format.DateTimeFormatter;


@Configuration
public class JacksonDataFormatConfigurator implements DataFormatConfigurator<JacksonJsonDataFormat> {

    public void configure(JacksonJsonDataFormat dataFormat) {
        ObjectMapper objectMapper = dataFormat.getObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTimeSerializer localDateTimeSerializer = new LocalDateTimeSerializer(formatter);
    }

    public Class<JacksonJsonDataFormat> getDataFormatClass() {
        return JacksonJsonDataFormat.class;
    }
}
