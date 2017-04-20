package com.ostdlabs.etoyataxi.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends PagingAndSortingRepository<Role, Long> {

    @Query("SELECT u FROM User u WHERE u.name = :name")
    public Role findFirstByName(@Param("name") String name);
}
