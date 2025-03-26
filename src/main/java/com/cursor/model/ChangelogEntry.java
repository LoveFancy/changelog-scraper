package com.cursor.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ChangelogEntry(
    String version,
    String date,
    String updateVersions,
    String updateDescription,
    List<String> content,
    List<String> simpleUpdates
) {} 