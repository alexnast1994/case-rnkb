package com.cognive.projects.casernkb.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UtilController {
    private static final String VERSION = "/version";
    private static final String ALIVE = "/alive";
    private final BuildProperties buildProperties;

    @GetMapping(VERSION)
    public ResponseEntity<String> getVersion() {
        return ResponseEntity.ok(buildProperties.getVersion());
    }

    @GetMapping(ALIVE)
    public ResponseEntity<Void> getAlive() {
        return ResponseEntity.ok().build();
    }
}
