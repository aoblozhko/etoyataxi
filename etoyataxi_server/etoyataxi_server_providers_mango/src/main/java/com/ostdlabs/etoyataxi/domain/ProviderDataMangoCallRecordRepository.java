package com.ostdlabs.etoyataxi.domain;

import com.ostdlabs.etoyataxi.domain.ProviderDataMangoCallRecord;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface ProviderDataMangoCallRecordRepository extends PagingAndSortingRepository<ProviderDataMangoCallRecord, Long> {

    @Query("SELECT pdmcr FROM ProviderDataMangoCallRecord pdmcr WHERE pdmcr.recordId = :recordId")
    public ProviderDataMangoCallRecord findFirstByRecordId(@Param("recordId") String recordId);
}
