package com.ostdlabs.etoyataxi.security.jwt;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public JwtAuthenticationFilter() {
        super("/**");
    }

    @Override
    protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
        return true;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        String header = request.getHeader("Authorization");

        if (request.getServletPath().startsWith("/api")
                && (header == null || !header.startsWith("Bearer "))) {
            throw new JwtTokenMissingException("No JWT token found in request headers");
        }

        if (header != null) {
            String authToken = header.substring(7);

            JwtAuthenticationToken authRequest = new JwtAuthenticationToken(null, null, authToken);

            return getAuthenticationManager().authenticate(authRequest);
        } else {
            return new JwtAnonymousAuth();
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);

        chain.doFilter(request, response);
    }
}