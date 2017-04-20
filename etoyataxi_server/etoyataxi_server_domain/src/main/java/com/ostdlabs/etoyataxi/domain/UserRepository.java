package com.ostdlabs.etoyataxi.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.id = :id")
    public User findFirstById(@Param("id") long id);

    @Query("SELECT u FROM User u WHERE u.email = :email")
    public User findFirstByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.username = :username")
    public User findFirstByUsername(@Param("username") String username);
}
