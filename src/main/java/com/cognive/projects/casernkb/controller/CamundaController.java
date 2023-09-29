package com.cognive.projects.casernkb.controller;

import com.cognive.projects.casernkb.service.BPMProcessService;
import com.cognive.projects.casernkb.service.KafkaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.ObjectValue;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/camunda")
@RequiredArgsConstructor
@Slf4j
public class CamundaController {
    private static final String START = "/start/{processName}";
    private static final String START_AND_RETURN_VARIABLES = "/start/{processName}/return/{returnVariables}";
    private static final String START_AND_RETURN_ALL_VARIABLES = "/start/{processName}/return";
    private final BPMProcessService bpmService;
    private final KafkaService kafkaService;

    @PostMapping(value = START)
    public ResponseEntity<String> camundaProcessStart(@PathVariable String processName, @RequestBody Map<String, Object> body) {
        Map<String, Object> variables = getVariables(body);
        var key = getKey(processName);
        try {
            var result = bpmService.startProcess(processName, key, variables);
            return ResponseEntity.ok(result);
        } catch (Exception ex) {
            kafkaService.sendError(processName, key, ex);
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping(value = START_AND_RETURN_VARIABLES)
    public ResponseEntity<Map<String,Object>> camundaProcessStartReturnVariables(@PathVariable String processName, @PathVariable List<String> returnVariables, @RequestBody Map<String, Object> body) {
        Map<String, Object> variables = getVariables(body);
        var key = getKey(processName);
        try {
            var result = bpmService.startProcessReturnVariables(processName, key, variables, returnVariables);
            return ResponseEntity.ok(result);
        } catch (Exception ex) {
            kafkaService.sendError(processName, key, ex);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", ex.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping(value = START_AND_RETURN_ALL_VARIABLES)
    public ResponseEntity<String> camundaProcessStartReturnVariable(@PathVariable String processName, @RequestBody Map<String, Object> body) {
        Map<String, Object> variables = getVariables(body);
        var key = getKey(processName);
        try {
            var result = bpmService.startProcessReturnVariable(processName, key, variables);
            return ResponseEntity.ok(result);
        } catch (Exception ex) {
            kafkaService.sendError(processName, key, ex);
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @NotNull
    private static Map<String, Object> getVariables(Map<String, Object> body) {
        Map<String, Object> variables = new HashMap<>();

        // Store value as json, prevent Camunda String limitation (4000 and 2000 for Oracle)
        ObjectValue jsonData = Variables.objectValue(body).serializationDataFormat("application/json").create();
        variables.put("payload", jsonData);
        return variables;
    }

    @NotNull
    private static String getKey(String processName) {
        var now = LocalDateTime.now();
        return "rest_" + now + "_" + processName;
    }
}
