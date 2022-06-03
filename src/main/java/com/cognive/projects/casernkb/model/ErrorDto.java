package com.cognive.projects.casernkb.model;

import lombok.Data;

@Data
public class ErrorDto {
    String key;
    String processName;
    String description;
    String stackTrace;
}
