package com.cursor.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChangelogEntry {
    private String version;
    private String date;
    private String updateVersions;
    private String updateDescription;
    private List<String> content;
    private List<String> simpleUpdates;

    public ChangelogEntry() {
    }

    public ChangelogEntry(String version, String date, String updateVersions, String updateDescription,
                         List<String> content, List<String> simpleUpdates) {
        this.version = version;
        this.date = date;
        this.updateVersions = updateVersions;
        this.updateDescription = updateDescription;
        this.content = content;
        this.simpleUpdates = simpleUpdates;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUpdateVersions() {
        return updateVersions;
    }

    public void setUpdateVersions(String updateVersions) {
        this.updateVersions = updateVersions;
    }

    public String getUpdateDescription() {
        return updateDescription;
    }

    public void setUpdateDescription(String updateDescription) {
        this.updateDescription = updateDescription;
    }

    public List<String> getContent() {
        return content;
    }

    public void setContent(List<String> content) {
        this.content = content;
    }

    public List<String> getSimpleUpdates() {
        return simpleUpdates;
    }

    public void setSimpleUpdates(List<String> simpleUpdates) {
        this.simpleUpdates = simpleUpdates;
    }
} 