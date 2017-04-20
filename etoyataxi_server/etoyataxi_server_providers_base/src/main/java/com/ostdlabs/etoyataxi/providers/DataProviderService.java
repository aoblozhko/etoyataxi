package com.ostdlabs.etoyataxi.providers;


import com.ostdlabs.etoyataxi.domain.ProviderRepository;
import com.ostdlabs.etoyataxi.domain.ProviderSetting;
import com.ostdlabs.etoyataxi.domain.ProviderSettingRepository;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

@Service
public class DataProviderService implements IDataProviderService {

    private static final Logger log = LoggerFactory.getLogger(DataProviderService.class);

    protected  Map<String, String> providerSettings = new HashMap<>();

    protected ProviderRepository providerRepository;

    protected  ProviderSettingRepository providerSettingRepository;

    @Inject public DataProviderService(ProviderRepository providerRepository, ProviderSettingRepository providerSettingRepository) {
        this.providerRepository = providerRepository;
        this.providerSettingRepository = providerSettingRepository;
    }

    public void loadProviderSettings(String providerName) {
        List<ProviderSetting> providerSettingList = providerSettingRepository.findByProviderName(providerName);
        for (ProviderSetting providerSetting : providerSettingList) {
            providerSettings.put(providerSetting.getName(), providerSetting.getValue());
        }
    }


    @Override
    public Map<Long, String> fetchData() {
        return null;
    }

}
