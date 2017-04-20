package com.ostdlabs.etoyataxi.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProviderDataRepository extends PagingAndSortingRepository<ProviderData, Long> {

    @Query("SELECT pd FROM ProviderData pd WHERE pd.provider.name = :name")
    public List<ProviderData> findByProviderName(@Param("name") String name);
}
