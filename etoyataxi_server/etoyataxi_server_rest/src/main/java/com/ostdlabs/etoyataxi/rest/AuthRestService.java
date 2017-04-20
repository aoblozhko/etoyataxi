package com.ostdlabs.etoyataxi.rest;


import com.ostdlabs.etoyataxi.dto.auth.TokenRequest;
import com.ostdlabs.etoyataxi.dto.auth.TokenResponse;
import com.ostdlabs.etoyataxi.dto.auth.UserLoginRequest;

import com.ostdlabs.etoyataxi.dto.auth.UserRegistrationRequest;
import com.ostdlabs.etoyataxi.security.jwt.AuthService;
import com.ostdlabs.etoyataxi.service.CustomUserDetailsService;
import com.ostdlabs.etoyataxi.domain.User;

import org.dozer.Mapper;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

import javax.servlet.ServletException;
import javax.ws.rs.Consumes;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


@Path("/auth")
@Component
public class AuthRestService {

    Mapper mapper;

    AuthService authService;

    CustomUserDetailsService userService;

    public AuthRestService(Mapper mapper, AuthService authService, CustomUserDetailsService userService) {
        this.mapper = mapper;
        this.authService = authService;
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Path("/token")
    public TokenResponse getToken(TokenRequest tokenRequest) {
        return new TokenResponse(Jwts.builder().setSubject(tokenRequest.username)
                .claim("username", tokenRequest.username)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, "secretkey").compact());
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public TokenResponse login(UserLoginRequest loginRequest)
            throws ServletException {

        authService.authenticate(loginRequest);

        final Claims claims = Jwts.parser().setSigningKey("secretkey")
                .parseClaimsJws(loginRequest.getToken()).getBody();

        return new TokenResponse(Jwts.builder().setSubject(loginRequest.getUsername())
                .claim("username", loginRequest.getUsername())
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, "secretkey").compact());
    }


    @RequestMapping(value = "register", method = RequestMethod.POST)
    public TokenResponse register(UserRegistrationRequest registrationRequest)
            throws ServletException {

        final Claims claims = Jwts.parser().setSigningKey("secretkey")
                .parseClaimsJws(registrationRequest.getToken()).getBody();
        String deviceId = claims.get("deviceId").toString();

        User user = userService.create(registrationRequest.getUsername(), registrationRequest.getPassword());

        UserLoginRequest loginRequest = mapper.map(registrationRequest, UserLoginRequest.class);

        authService.authenticate(loginRequest);
        return new TokenResponse(Jwts.builder().setSubject(deviceId)
                .claim("username", user.getUsername())
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, "secretkey").compact());
    }
}
