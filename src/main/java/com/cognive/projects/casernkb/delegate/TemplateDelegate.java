package com.cognive.projects.casernkb.delegate;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class TemplateDelegate implements JavaDelegate {
    @Override
    @SuppressWarnings("all")
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String template = (String)delegateExecution.getVariable("template");
        String outputVariable = (String)delegateExecution.getVariable("outputVarName");
        Map<String, Object> parameters = (Map<String, Object>) delegateExecution.getVariable("parameters");

        if(log.isDebugEnabled()) {
            log.debug("Template: {}", template);
        }
        try {
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_29);
            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

            Template t = new Template("template", new StringReader(template), cfg);
            Map<String, Object> root = new HashMap<>();
            parameters.forEach((k, v) -> {
                root.put(k, v);
            });
            root.put("dateTimeFormat", new TemplateLocalDateTimeModel());

            StringWriter stringWriter = new StringWriter();
            t.process(root, stringWriter);

            if(log.isDebugEnabled()) {
                log.debug("{}", stringWriter);
            }

            delegateExecution.setVariable(outputVariable, stringWriter.toString());
        } catch (TemplateException | IOException ex) {
            throw new RuntimeException("Failed process template", ex);
        }
    }
}
