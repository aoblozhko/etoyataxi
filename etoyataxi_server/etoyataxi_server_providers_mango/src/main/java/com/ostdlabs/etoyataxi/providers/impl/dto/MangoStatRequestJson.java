package com.ostdlabs.etoyataxi.providers.impl.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MangoStatRequestJson {

    @JsonProperty("date_from")
    private String dateFrom;

    @JsonProperty("date_to")
    private String dateTo;

    private String fields = "records,start,finish,from_extension,from_number,to_extension,to_number,disconnect_reason";

    public MangoStatRequestJson(String dateFrom, String dateTo) {
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }
}
