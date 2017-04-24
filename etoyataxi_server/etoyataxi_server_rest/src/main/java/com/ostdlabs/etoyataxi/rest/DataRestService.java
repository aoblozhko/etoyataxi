package com.ostdlabs.etoyataxi.rest;


import com.ostdlabs.etoyataxi.domain.ProviderData;
import com.ostdlabs.etoyataxi.domain.ProviderDataRepository;
import com.ostdlabs.etoyataxi.domain.User;
import com.ostdlabs.etoyataxi.dto.ProviderDataTO;
import com.ostdlabs.etoyataxi.dto.auth.TokenRequest;
import com.ostdlabs.etoyataxi.dto.auth.TokenResponse;
import com.ostdlabs.etoyataxi.dto.auth.UserLoginRequest;
import com.ostdlabs.etoyataxi.dto.auth.UserRegistrationRequest;
import com.ostdlabs.etoyataxi.security.jwt.AuthService;
import com.ostdlabs.etoyataxi.service.CustomUserDetailsService;
import com.ostdlabs.etoyataxi.service.ProviderService;

import org.dozer.Mapper;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
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

    ProviderService providerService;

    @Inject public DataRestService(Mapper mapper, AuthService authService, CustomUserDetailsService userService,
                           ProviderDataRepository providerDataRepository, ProviderService providerService) {
        this.mapper = mapper;
        this.authService = authService;
        this.userService = userService;
        this.providerDataRepository = providerDataRepository;
        this.providerService = providerService;
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Path("/{providerName}/stat")
    public List<ProviderDataTO> getStat(@PathParam("providerName") String providerName) {
        List<ProviderData> providerDataList = providerDataRepository.findByProviderName(providerName);

        List<ProviderDataTO> providerDataTOS = new ArrayList<>();
        for (ProviderData providerData : providerDataList) {
            ProviderDataTO providerDataTO = new ProviderDataTO();
            providerDataTO.setId(providerData.getId());
            providerDataTO.setDateTime(providerData.getDateTime());
            Map<String, Object> data = providerService.unserializeDataField(providerName, providerData.getData());
            providerDataTO.setData(data);
            providerDataTOS.add(providerDataTO);
        }
        return providerDataTOS;
    }

}
