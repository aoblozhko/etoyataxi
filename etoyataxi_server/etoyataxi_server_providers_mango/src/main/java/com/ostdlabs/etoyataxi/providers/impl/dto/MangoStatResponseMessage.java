package com.ostdlabs.etoyataxi.providers.impl.dto;


import java.util.ArrayList;
import java.util.List;

public class MangoStatResponseMessage {

    private List<MangoStatResponseMessageEntry> entries = new ArrayList<>();

    public List<MangoStatResponseMessageEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<MangoStatResponseMessageEntry> entries) {
        this.entries = entries;
    }
}
