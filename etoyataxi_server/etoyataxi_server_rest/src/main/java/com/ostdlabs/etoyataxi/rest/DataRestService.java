package com.ostdlabs.etoyataxi.rest;


import com.ostdlabs.etoyataxi.domain.ProviderData;
import com.ostdlabs.etoyataxi.domain.ProviderDataMangoCallRecord;
import com.ostdlabs.etoyataxi.domain.ProviderDataMangoCallRecordRepository;
import com.ostdlabs.etoyataxi.domain.ProviderDataRepository;
import com.ostdlabs.etoyataxi.dto.ProviderDataTO;
import com.ostdlabs.etoyataxi.security.jwt.AuthService;
import com.ostdlabs.etoyataxi.service.CustomUserDetailsService;
import com.ostdlabs.etoyataxi.service.ProviderService;

import org.dozer.Mapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


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
