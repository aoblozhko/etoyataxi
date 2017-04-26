package com.ostdlabs.etoyataxi.providers.impl.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MangoStatResponseMessageEntry {

    private List<String> records;

    private String start;

    private String finish;

    @JsonProperty("from_extension")
    private String fromExtension;

    @JsonProperty("from_number")
    private String fromNumber;

    @JsonProperty("to_extension")
    private String toExtension;

    @JsonProperty("to_number")
    private String toNumber;

    @JsonProperty("disconnect_reason")
    private String disconnectReason;

    public MangoStatResponseMessageEntry() {
    }


    public MangoStatResponseMessageEntry(String[] values) {
        List<String> recordIds = Arrays.asList(values[0].split(","));
        this.records = recordIds;
        this.start = values[1];
        this.finish = values[2];
        this.fromExtension = values[3];
        this.fromNumber = values[4];
        this.toExtension = values[5];
        this.toNumber = values[6];
        this.disconnectReason = values[7];
    }

    public List<String> getRecords() {
        return records;
    }

    public void setRecords(List<String> records) {
        this.records = records;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getFinish() {
        return finish;
    }

    public void setFinish(String finish) {
        this.finish = finish;
    }

    public String getFromExtension() {
        return fromExtension;
    }

    public void setFromExtension(String fromExtension) {
        this.fromExtension = fromExtension;
    }

    public String getFromNumber() {
        return fromNumber;
    }

    public void setFromNumber(String fromNumber) {
        this.fromNumber = fromNumber;
    }

    public String getToExtension() {
        return toExtension;
    }

    public void setToExtension(String toExtension) {
        this.toExtension = toExtension;
    }

    public String getToNumber() {
        return toNumber;
    }

    public void setToNumber(String toNumber) {
        this.toNumber = toNumber;
    }

    public String getDisconnectReason() {
        return disconnectReason;
    }

    public void setDisconnectReason(String disconnectReason) {
        this.disconnectReason = disconnectReason;
    }

}
