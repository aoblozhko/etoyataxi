package com.ostdlabs.etoyataxi.providers;



import com.ostdlabs.etoyataxi.domain.ProviderData;

import java.util.Map;

public interface IDataProviderService {
    Map<Long, String> fetchData();

    Map<String, Object> unserializeDataField(String dataString);
}
