package com.ostdlabs.etoyataxi.rest;


import com.ostdlabs.etoyataxi.domain.ProviderData;
import com.ostdlabs.etoyataxi.domain.ProviderDataRepository;
import com.ostdlabs.etoyataxi.domain.User;
import com.ostdlabs.etoyataxi.dto.auth.TokenRequest;
import com.ostdlabs.etoyataxi.dto.auth.TokenResponse;
import com.ostdlabs.etoyataxi.dto.auth.UserLoginRequest;
import com.ostdlabs.etoyataxi.dto.auth.UserRegistrationRequest;
import com.ostdlabs.etoyataxi.security.jwt.AuthService;
import com.ostdlabs.etoyataxi.service.CustomUserDetailsService;

import org.dozer.Mapper;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


@Path("/api/data")
@Component
public class DataRestService {

    Mapper mapper;

    AuthService authService;

    CustomUserDetailsService userService;

    ProviderDataRepository providerDataRepository;

    public DataRestService(Mapper mapper, AuthService authService, CustomUserDetailsService userService,
                           ProviderDataRepository providerDataRepository) {
        this.mapper = mapper;
        this.authService = authService;
        this.providerDataRepository = providerDataRepository;
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Path("/{providerName}/stat")
    public List<ProviderData> getStat(@PathParam("providerName") String providerName) {
        List<ProviderData> providerData = providerDataRepository.findByProviderName(providerName);
        return providerData;
    }

}
