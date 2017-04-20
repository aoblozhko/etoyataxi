package com.ostdlabs.etoyataxi.providers.impl.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

public class MangoStatResultRequest {

    @JsonProperty("vpbx_api_key")
    private String vpbxApiKey;

    private String sign;

    private MangoStatResultRequestJson json;

    public MangoStatResultRequest(String vpbxApiKey, String sign, String key) {
        this.vpbxApiKey = vpbxApiKey;
        this.sign = sign;
        this.json = new MangoStatResultRequestJson(key);
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

    public MangoStatResultRequestJson getJson() {
        return json;
    }

    public void setJson(MangoStatResultRequestJson json) {
        this.json = json;
    }
}
