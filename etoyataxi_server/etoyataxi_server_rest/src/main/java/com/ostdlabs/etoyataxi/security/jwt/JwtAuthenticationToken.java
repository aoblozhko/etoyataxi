package com.ostdlabs.etoyataxi.security.jwt;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;


public class JwtAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private String token;

    public JwtAuthenticationToken(Object principal, Object credentials, String token) {
        super(principal, credentials);
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }
}
