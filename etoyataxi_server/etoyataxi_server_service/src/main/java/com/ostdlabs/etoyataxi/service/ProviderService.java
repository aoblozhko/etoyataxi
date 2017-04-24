package com.ostdlabs.etoyataxi.service;


import com.ostdlabs.etoyataxi.domain.Provider;
import com.ostdlabs.etoyataxi.domain.ProviderRepository;
import com.ostdlabs.etoyataxi.providers.IDataProviderService;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Map;

import javax.inject.Inject;

@Service
public class ProviderService {


    ApplicationContext applicationContext;

    ProviderRepository providerRepository;

    @Inject
    public ProviderService(ApplicationContext applicationContext, ProviderRepository providerRepository) {
        this.applicationContext = applicationContext;
        this.providerRepository = providerRepository;
    }

    public Map<String, Object> unserializeDataField(String providerName, String dataString) {
        Provider provider = providerRepository.findFirstByName(providerName);
        IDataProviderService providerDriver = (IDataProviderService) applicationContext.getBean(provider.getDriverBean());
        return providerDriver.unserializeDataField(dataString);

    }

}
