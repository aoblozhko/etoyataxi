package com.ostdlabs.etoyataxi.scheduled;


import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


import com.ostdlabs.etoyataxi.domain.Provider;
import com.ostdlabs.etoyataxi.domain.ProviderData;
import com.ostdlabs.etoyataxi.domain.ProviderDataRepository;
import com.ostdlabs.etoyataxi.domain.ProviderRepository;
import com.ostdlabs.etoyataxi.providers.IDataProviderService;

import java.util.List;
import java.util.Map;

@Service
public class ProviderUpdateService {

    private static final Logger log = LoggerFactory.getLogger(ProviderUpdateService.class);

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
            long updateTimestamp = provider.getLastUpdate().getMillis() / 1000;
            long nowTimestamp = DateTime.now().getMillis() / 1000;
            if ((updateTimestamp + provider.getCheckInterval()) < nowTimestamp) { // time to update provider data
                IDataProviderService providerDriver = (IDataProviderService) applicationContext.getBean(provider.getDriverBean());
                Map<Long, String> data = providerDriver.fetchData();
                if (data != null) {
                    for (long millis : data.keySet()) {
                        ProviderData providerData = new ProviderData();
                        providerData.setDateTime(new DateTime(millis));
                        String dataString = data.get(millis);
                        if (dataString != null && !"null".equals(dataString)) {
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
    }


}
