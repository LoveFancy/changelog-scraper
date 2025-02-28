package com.cursor.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChangelogEntry {
    private String version;
    private String date;
    private String updateVersions;
    private String updateDescription;
    private List<String> content;
    private List<String> simpleUpdates;
} 