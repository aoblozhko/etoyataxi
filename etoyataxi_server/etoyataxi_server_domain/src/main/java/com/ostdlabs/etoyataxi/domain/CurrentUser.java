package com.ostdlabs.etoyataxi.domain;

import org.springframework.security.core.authority.AuthorityUtils;

import java.util.stream.Collectors;

public class CurrentUser extends org.springframework.security.core.userdetails.User {

    private User user;

    public CurrentUser(User user) {
        super(user.getEmail(), user.getPasswordHash(),
                AuthorityUtils.commaSeparatedStringToAuthorityList(user.getRoles()
                    .stream().map(Object::toString).collect(Collectors.joining(",")))
        );
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public Long getId() {
        return user.getId();
    }

}