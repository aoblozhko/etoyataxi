package com.ostdlabs.etoyataxi.providers.impl.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

public class MangoStatRequest {

    @JsonProperty("vpbx_api_key")
    private String vpbxApiKey;

    private String sign;

    private MangoStatRequestJson json;

    public MangoStatRequest(String vpbxApiKey, String sign, String dateFrom, String dateTo) {
        this.vpbxApiKey = vpbxApiKey;
        this.sign = sign;
        this.json = new MangoStatRequestJson(dateFrom, dateTo);
    }

    public String getVpbxApiKey() {
        return vpbxApiKey;
    }

    public void setVpbxApiKey(String vpbxApiKey) {
        this.vpbxApiKey = vpbxApiKey;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public MangoStatRequestJson getJson() {
        return json;
    }

    public void setJson(MangoStatRequestJson json) {
        this.json = json;
    }
}
