package com.ostdlabs.etoyataxi.providers.impl.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

public class MangoCallRecordRequestJson {

    @JsonProperty("recording_id")
    private String recordingId;

    private String action = "download";

    public MangoCallRecordRequestJson(String recordingId) {
        this.recordingId = recordingId;
    }

    public String getRecordingId() {
        return recordingId;
    }

    public void setRecordingId(String recordingId) {
        this.recordingId = recordingId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
