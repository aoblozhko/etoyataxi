package com.ostdlabs.etoyataxi.dto;



import org.joda.time.DateTime;

import java.util.Map;


public class ProviderDataTO {
    private long id;

    private DateTime dateTime;

    private Map<String, Object> data;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
