package com.ostdlabs.etoyataxi.dto;


import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ProviderDataJson implements Serializable {

    private List<Map<String, Object>> entries;

    public List<Map<String, Object>> getEntries() {
        return entries;
    }

    public void setEntries(List<Map<String, Object>> entries) {
        this.entries = entries;
    }
}
