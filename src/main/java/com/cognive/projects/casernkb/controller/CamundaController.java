package com.cognive.projects.casernkb.controller;

import com.cognive.projects.casernkb.service.BPMProcessService;
import com.cognive.projects.casernkb.service.KafkaService;
import com.prime.db.rnkb.repository.ExecutionRepository;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.ObjectValue;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/camunda")
@RequiredArgsConstructor
public class CamundaController {
    private static final String START = "/start/{processName}";
    private final BPMProcessService bpmService;
    private final KafkaService kafkaService;
    private final ExecutionRepository executionRepository;

    @PostMapping(value = START)
    public ResponseEntity<String> camundaProcessStart(@PathVariable String processName, @RequestBody Map<String, Object> body) {
        Map<String, Object> variables = new HashMap<>();

        // Store value as json, prevent Camunda String limitation (4000 and 2000 for Oracle)
        ObjectValue jsonData = Variables.objectValue(body).serializationDataFormat("application/json").create();
        variables.put("payload", jsonData);
        var now = LocalDateTime.now();
        var key = "rest_" + now + "_" + processName;
        try {
            var result = bpmService.startProcess(processName, key, variables);
            return ResponseEntity.ok(result);
        } catch (Exception ex) {
            kafkaService.sendError(processName, key, ex);
            throw ex;
        }
    }
}
