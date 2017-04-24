package com.ostdlabs.etoyataxi.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProviderRepository extends PagingAndSortingRepository<Provider, Long> {

    @Query("SELECT p FROM Provider p WHERE p.enabled = :enabled")
    public List<Provider> findByEnabled(@Param("enabled") boolean enabled);

    @Query("SELECT p FROM Provider p WHERE p.name = :name")
    public Provider findFirstByName(@Param("name") String name);

    @Query("SELECT p FROM Provider p WHERE p.driverBean = :driverBean")
    public Provider findFirstByDriverBean(@Param("driverBean") String driverBean);
}
