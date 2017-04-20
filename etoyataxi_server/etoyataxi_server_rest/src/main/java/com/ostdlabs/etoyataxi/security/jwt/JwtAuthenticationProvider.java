package com.ostdlabs.etoyataxi.security.jwt;


import com.ostdlabs.etoyataxi.service.CustomUserDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;

public class JwtAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private JwtUtil jwtUtil = new JwtUtil();

    @Autowired
    private CustomUserDetailsService userService;

    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
        String token = jwtAuthenticationToken.getToken();

        Claims claims = jwtUtil.parseToken(token);

        if (claims == null) {
            throw new JwtTokenMalformedException("JWT token is not valid");
        }
        if (claims.get("username") != null) {
            return userService.loadUserByUsername(String.valueOf(claims.get("username")));
        } else {
            return new JwtAnonymousUserDetails();
        }
    }

}