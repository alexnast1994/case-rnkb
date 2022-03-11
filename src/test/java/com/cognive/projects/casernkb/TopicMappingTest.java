package com.cognive.projects.casernkb;

import com.cognive.projects.casernkb.config.MessageMappingConfig;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TopicMappingTest {
    @Autowired
    MessageMappingConfig config;

    @Test
    void test() {
        Assertions.assertNull(config.getBinding("test"));
    }

}
