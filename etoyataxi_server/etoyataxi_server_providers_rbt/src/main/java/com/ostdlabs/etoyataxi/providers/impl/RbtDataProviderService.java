package com.ostdlabs.etoyataxi.providers.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.ostdlabs.etoyataxi.domain.Provider;
import com.ostdlabs.etoyataxi.domain.ProviderRepository;
import com.ostdlabs.etoyataxi.domain.ProviderSettingRepository;
import com.ostdlabs.etoyataxi.providers.DataProviderService;
import com.ostdlabs.etoyataxi.providers.IDataProviderService;
import com.ostdlabs.etoyataxi.providers.impl.dto.RbtStatResponse;
import com.ostdlabs.etoyataxi.providers.impl.dto.RbtStatResponseRow;
import com.ostdlabs.etoyataxi.providers.impl.dto.RbtStatResultData;


import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.MediaType;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;


@Service
@DependsOn("dataProviderService")
public class RbtDataProviderService extends DataProviderService implements IDataProviderService, BeanNameAware {

    private static final Logger log = LoggerFactory.getLogger(RbtDataProviderService.class);


    private static final String DATE_FROM_PATTERN = "yyyy-MM-dd";

    private RestTemplate restTemplate;

    private ObjectMapper jsonMapper = null;

    private String beanName = null;

    private Provider provider = null;

    public RbtDataProviderService(ProviderRepository providerRepository, ProviderSettingRepository providerSettingRepository) {
        super(providerRepository, providerSettingRepository);
    }

    @PostConstruct
    public void setup() {
        provider = providerRepository.findFirstByDriverBean(beanName);

        loadProviderSettings(provider.getName());
        setupRestTemplate();
    }

    private void setupRestTemplate() {
        this.restTemplate = new RestTemplate();
        Jaxb2RootElementHttpMessageConverter messageConverter = new Jaxb2RootElementHttpMessageConverter();
        List<MediaType> mediaTypes = new ArrayList<>();
        mediaTypes.add(MediaType.TEXT_HTML);
        messageConverter.setSupportedMediaTypes(mediaTypes);
        this.restTemplate.getMessageConverters().add(messageConverter);
    }

    @Override
    public Map<Long, String> fetchData() {
        Map<String, String> requestParameters = new HashMap<>();
        requestParameters.put("baseUrl", providerSettings.get("baseUrl"));
        requestParameters.put("key", providerSettings.get("key"));
        requestParameters.put("format", providerSettings.get("table"));
        String dateFrom = DateTime.now().toString(DATE_FROM_PATTERN);
        if (provider.getLastUpdate() != null) {
            dateFrom = provider.getLastUpdate().toString(DATE_FROM_PATTERN);
        }
        requestParameters.put("dateFrom", dateFrom);

        RbtStatResponse result = restTemplate.getForObject("{baseUrl}v3/stat?key={key}&format={format}&dateFrom={dateFrom}",
                RbtStatResponse.class, requestParameters);

        Map<Long, String> extractedData = new HashMap<>();

        for (RbtStatResponseRow row : result.getRows()) {
            if (row.col != null) {
                Long millis = DateTime.parse(row.col[0]).getMillis();
                extractedData.put(millis, formatResultDataRow(row));
            }
        }

        return extractedData;
    }

    private String formatResultDataRow(RbtStatResponseRow row) {
        RbtStatResultData resultData = new RbtStatResultData(row.col);
        try {
            return getJsonMapper().writeValueAsString(resultData);
        } catch (JsonProcessingException e) {
            log.error("error serilzing rtb provider data", e);
        }
        return null;
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
