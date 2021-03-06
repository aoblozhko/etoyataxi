package com.ostdlabs.etoyataxi.security.jwt;

import org.springframework.security.core.AuthenticationException;

public class JwtTokenMalformedException extends AuthenticationException {

    public JwtTokenMalformedException(String msg, Throwable t) {
        super(msg, t);
    }

    public JwtTokenMalformedException(String msg) {
        super(msg);
    }
}
