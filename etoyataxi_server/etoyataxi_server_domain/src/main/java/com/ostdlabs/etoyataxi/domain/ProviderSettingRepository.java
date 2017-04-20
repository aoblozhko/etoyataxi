package com.ostdlabs.etoyataxi.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProviderSettingRepository extends PagingAndSortingRepository<ProviderSetting, Long> {


    @Query("SELECT ps FROM ProviderSetting ps WHERE ps.provider.name = :providerName")
    public List<ProviderSetting> findByProviderName(@Param("providerName") String providerName);
}
