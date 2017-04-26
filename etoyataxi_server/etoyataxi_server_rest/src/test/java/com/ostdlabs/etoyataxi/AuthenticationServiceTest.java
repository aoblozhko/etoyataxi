package com.ostdlabs.etoyataxi;

import com.ostdlabs.etoyataxi.dto.EtoYaTaxiAuthResponse;
import com.ostdlabs.etoyataxi.providers.impl.dto.MangoStatResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import static com.ostdlabs.etoyataxi.Main.main;

/*
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
*/

@RunWith(SpringRunner.class)
@ContextConfiguration(locations = "classpath:serviceContext-test.xml")
public class AuthenticationServiceTest {

    RestTemplate restTemplate = new RestTemplate();

    @Test
    public void testToken() throws Exception {

        main(new String[] {});

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        //map.add("vpbx_api_key", providerSettings.get("vpbx_api_key"));

        ResponseEntity<EtoYaTaxiAuthResponse> statRequest = restTemplate.postForEntity("http://localhost:8083/services/auth/token",
                request, EtoYaTaxiAuthResponse.class);
        //MatcherAssert.assertThat(tokenUtils.validateToken(token,result), is(false));
    }

}