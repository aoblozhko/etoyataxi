package com.ostdlabs.etoyataxi.scheduled;


import org.joda.time.DateTime;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


import com.ostdlabs.etoyataxi.domain.Provider;
import com.ostdlabs.etoyataxi.domain.ProviderData;
import com.ostdlabs.etoyataxi.domain.ProviderDataRepository;
import com.ostdlabs.etoyataxi.domain.ProviderRepository;
import com.ostdlabs.etoyataxi.providers.IDataProviderService;
import com.ostdlabs.etoyataxi.providers.impl.RbtDataProviderService;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

@Service
public class ProviderUpdateService {

    ApplicationContext applicationContext;

    ProviderRepository providerRepository;

    ProviderDataRepository providerDataRepository;


    public ProviderUpdateService(ApplicationContext applicationContext, ProviderRepository providerRepository,
                                 ProviderDataRepository providerDataRepository) {
        this.applicationContext = applicationContext;
        this.providerRepository = providerRepository;
        this.providerDataRepository = providerDataRepository;
    }

    @Scheduled(cron = "${scheduled.providers.crontab}")
    public void updateProviderData() {
        List<Provider> enabledProviders = providerRepository.findByEnabled(true);
        for (Provider provider : enabledProviders) {
            IDataProviderService rbt = (IDataProviderService) applicationContext.getBean(provider.getDriverBean());
            Map<Long, String> data = rbt.fetchData();
            if (data != null) {
                for (long millis : data.keySet()) {
                    ProviderData providerData = new ProviderData();
                    providerData.setDateTime(new DateTime(millis));
                    String dataString = data.get(millis);
                    providerData.setData(dataString);
                    providerData.setProvider(provider);
                    providerDataRepository.save(providerData);
                    provider.setLastUpdate(DateTime.now());
                    providerRepository.save(provider);
                }
            }
        }
    }
}
