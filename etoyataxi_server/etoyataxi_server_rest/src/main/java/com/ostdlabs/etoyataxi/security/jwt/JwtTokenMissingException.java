package com.ostdlabs.etoyataxi.security.jwt;


import org.springframework.security.core.AuthenticationException;

public class JwtTokenMissingException extends AuthenticationException {


    public JwtTokenMissingException(String msg, Throwable t) {
        super(msg, t);
    }

    public JwtTokenMissingException(String msg) {
        super(msg);
    }
}
