package com.ostdlabs.etoyataxi.security.jwt;


import com.ostdlabs.etoyataxi.dto.auth.UserLoginRequest;
import com.ostdlabs.etoyataxi.domain.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;


@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    AuthenticationManager authenticationManager;

    public void authenticate(HttpServletRequest request) {

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(request.getParameter("username"), request.getParameter("password"));
        request.getSession();
        try {
            Authentication auth = authenticationManager.authenticate(token);

            SecurityContextHolder.getContext().setAuthentication(auth);
        } catch(Exception e){
            e.printStackTrace();
        }

    }

    public void authenticate(UserLoginRequest request) {

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
        try {
            Authentication auth = authenticationManager.authenticate(token);

            SecurityContextHolder.getContext().setAuthentication(auth);
        } catch(Exception e){
            e.printStackTrace();
        }

    }

    public User getCurrentUser() {
        User result = null;
        if (SecurityContextHolder.getContext().getAuthentication() != null &&
                SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof User) {
            result = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        return result;
    }
}
