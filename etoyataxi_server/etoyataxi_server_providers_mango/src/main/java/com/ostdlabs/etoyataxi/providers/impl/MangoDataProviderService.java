package com.ostdlabs.etoyataxi.providers.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.ostdlabs.etoyataxi.domain.Provider;
import com.ostdlabs.etoyataxi.domain.ProviderDataMangoCallRecord;
import com.ostdlabs.etoyataxi.domain.ProviderDataMangoCallRecordRepository;
import com.ostdlabs.etoyataxi.domain.ProviderRepository;
import com.ostdlabs.etoyataxi.domain.ProviderSettingRepository;
import com.ostdlabs.etoyataxi.providers.DataProviderService;
import com.ostdlabs.etoyataxi.providers.IDataProviderService;

import com.ostdlabs.etoyataxi.providers.impl.dto.MangoCallRecordRequestJson;
import com.ostdlabs.etoyataxi.providers.impl.dto.MangoStatRequestJson;
import com.ostdlabs.etoyataxi.providers.impl.dto.MangoStatResponse;
import com.ostdlabs.etoyataxi.providers.impl.dto.MangoStatResponseMessage;
import com.ostdlabs.etoyataxi.providers.impl.dto.MangoStatResponseMessageEntry;
import com.ostdlabs.etoyataxi.providers.impl.dto.MangoStatResultRequestJson;
import com.ostdlabs.etoyataxi.providers.impl.restclient.MangoStatMessageConverter;

import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;


@Service
@DependsOn("dataProviderService")
public class MangoDataProviderService extends DataProviderService implements IDataProviderService, BeanNameAware {

    private static final Logger log = LoggerFactory.getLogger(MangoDataProviderService.class);

    private RestTemplate restTemplate;

    private ObjectMapper jsonMapper = null;

    private String beanName = null;

    private Provider provider = null;

    private ProviderDataMangoCallRecordRepository providerDataMangoCallRepository;

    private TaskScheduler scheduler = new ConcurrentTaskScheduler();

    @Inject
    public MangoDataProviderService(ProviderRepository providerRepository, ProviderSettingRepository providerSettingRepository,
                                    ProviderDataMangoCallRecordRepository providerDataMangoCallRepository) {
        super(providerRepository, providerSettingRepository);
        this.providerDataMangoCallRepository = providerDataMangoCallRepository;
    }

    @PostConstruct
    public void setup() {
        provider = providerRepository.findFirstByDriverBean(beanName);

        loadProviderSettings(provider.getName());
        setupRestTemplate();
    }

    private void setupRestTemplate() {
        this.restTemplate = new RestTemplate();
        MangoStatMessageConverter mangoMessageConverter = new MangoStatMessageConverter();
        this.restTemplate.getMessageConverters().add(mangoMessageConverter);

    }

    @Override
    public Map<Long, String> fetchData() {

        ResponseEntity<MangoStatResponse> statRequest = requestStat();

        String resultKey = statRequest.getBody().getKey();

        ResponseEntity<MangoStatResponseMessage> response = requestStatResult(resultKey);

        String jsonString = "";

        try {
            jsonString = getJsonMapper().writeValueAsString(response.getBody());
        } catch (JsonProcessingException e) {
            log.error("wrror converting response to json", e);
        }

        Map<Long, String> extractedData = new HashMap<>();
        extractedData.put(DateTime.now().getMillis(), jsonString);

        return extractedData;
    }

    private ResponseEntity<MangoStatResponse> requestStat() {

        Map<String, String> requestParameters = new HashMap<>();
        requestParameters.put("baseUrl", providerSettings.get("baseUrl"));

        DateTime dateFrom = DateTime.now();
        if (provider.getLastUpdate() != null) {
            dateFrom = provider.getLastUpdate();
        }
        DateTime dateTo = dateFrom.plusDays(1);

        String dateFromParam = String.valueOf(dateFrom.getMillis() / 1000);
        String dateToParam = String.valueOf(dateTo.getMillis() / 1000);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("vpbx_api_key", providerSettings.get("vpbx_api_key"));

        MangoStatRequestJson statRequestJson = new MangoStatRequestJson(dateFromParam, dateToParam);
        String jsonString = "";

        try {
            jsonString = getJsonMapper().writeValueAsString(statRequestJson);
        } catch (JsonProcessingException e) {
            log.error("error serializing mango provider stat request params", e);
        }
        map.add("json", jsonString);

        String sign = getSign(providerSettings.get("vpbx_api_key") + jsonString + providerSettings.get("sign"));

        map.add("sign", sign);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        ResponseEntity<MangoStatResponse> statRequest = restTemplate.postForEntity("{baseUrl}stats/request",
                request, MangoStatResponse.class, requestParameters);

        return statRequest;
    }

    private String getSign(String contents) {
        String sign = "";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(contents.getBytes("UTF-8"));
            byte[] digest = md.digest();
            sign = String.format("%064x", new java.math.BigInteger(1, digest));
        } catch (Exception e) {
            log.error("Error creating sign key for mango provider", e);
        }
        return sign;
    }

    private ResponseEntity<MangoStatResponseMessage> requestStatResult(String resultKey) {

        Map<String, String> requestParameters = new HashMap<>();
        requestParameters.put("baseUrl", providerSettings.get("baseUrl"));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> requestBodyMap = new LinkedMultiValueMap<>();
        requestBodyMap.add("vpbx_api_key", providerSettings.get("vpbx_api_key"));

        MangoStatResultRequestJson statResultRequestJson = new MangoStatResultRequestJson(resultKey);

        String resultJsonString = "";
        try {
            resultJsonString = getJsonMapper().writeValueAsString(statResultRequestJson);
        } catch (JsonProcessingException e) {
            log.error("error serializing mango provider stat request params", e);
        }
        requestBodyMap.add("json", resultJsonString);

        String sign = getSign(providerSettings.get("vpbx_api_key") + resultJsonString + providerSettings.get("sign"));

        requestBodyMap.add("sign", sign);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(requestBodyMap, headers);

        ResponseEntity<MangoStatResponseMessage> response = restTemplate.postForEntity("{baseUrl}stats/result", request , MangoStatResponseMessage.class, requestParameters);

        List<MangoStatResponseMessageEntry> entries = response.getBody().getEntries();

        checkAndDownloadRecords(entries);

        return response;
    }

    public void checkAndDownloadRecords(List<MangoStatResponseMessageEntry> entries) {

        for (MangoStatResponseMessageEntry entry : entries) {
            List<String> records = entry.getRecords();
            for (String recordId : records) {
                ProviderDataMangoCallRecord record = providerDataMangoCallRepository.findFirstByRecordId(recordId);
                if (record == null) {
                    scheduler.schedule(new Runnable() {
                        @Override
                        public void run() {
                            fetchRecord(recordId);
                        }
                    }, DateTime.now().plusSeconds(5).toDate());
                }
            }
        }
    }

    public void fetchRecord(String recordId) {

        Map<String, String> requestParameters = new HashMap<>();
        requestParameters.put("baseUrl", providerSettings.get("baseUrl"));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> requestBodyMap = new LinkedMultiValueMap<>();
        requestBodyMap.add("vpbx_api_key", providerSettings.get("vpbx_api_key"));

        MangoCallRecordRequestJson recordRequestJson = new MangoCallRecordRequestJson(recordId);

        String resultJsonString = "";
        try {
            resultJsonString = getJsonMapper().writeValueAsString(recordRequestJson);
        } catch (JsonProcessingException e) {
            log.error("error serializing mango provider record request params", e);
        }
        requestBodyMap.add("json", resultJsonString);

        String sign = getSign(providerSettings.get("vpbx_api_key") + resultJsonString + providerSettings.get("sign"));

        requestBodyMap.add("sign", sign);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(requestBodyMap, headers);

        ResponseEntity<MangoStatResponseMessage> linkResponse = restTemplate.postForEntity("{baseUrl}/vpbx/queries/recording/post", request , MangoStatResponseMessage.class, requestParameters);
        String location = linkResponse.getHeaders().getFirst("Location");

        HttpEntity<MultiValueMap<String, String>> contentRequest = new HttpEntity<>(requestBodyMap, headers);

        ResponseEntity<Resource> contentResponse = restTemplate.exchange(location, HttpMethod.GET, contentRequest, Resource.class);

        InputStream responseInputStream;

        try {

            responseInputStream = contentResponse.getBody().getInputStream();
            ProviderDataMangoCallRecord record = new ProviderDataMangoCallRecord();
            byte[] data = IOUtils.toByteArray(responseInputStream);
            record.setContentType(contentResponse.getHeaders().getFirst("Content-Type"));
            record.setData(data);

            providerDataMangoCallRepository.save(record);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, Object> unserializeDataField(String dataString) {

        MangoStatResponseMessage resultData = null;
        Map<String, Object> unserialized = new HashMap<>();

        try {
            resultData = getJsonMapper().readValue(dataString, MangoStatResponseMessage.class);
        } catch (IOException e) {
            log.error("Error userializing data field", e);
        }
        if (resultData != null) {
            unserialized.put("entries", resultData.getEntries());
        }
        return unserialized;
    }

    private ObjectMapper getJsonMapper() {
        if (jsonMapper == null) {
            jsonMapper = new ObjectMapper();
            jsonMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            jsonMapper.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false);
            jsonMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        }
        return jsonMapper;
    }

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }

}
