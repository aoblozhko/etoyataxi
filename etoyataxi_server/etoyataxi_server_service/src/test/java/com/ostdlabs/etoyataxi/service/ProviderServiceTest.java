package com.ostdlabs.etoyataxi.service;


import org.aspectj.lang.annotation.After;

import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(SpringRunner.class)
@ContextConfiguration(locations = "classpath:serviceContext-test.xml")
public class ProviderServiceTest {

    @Autowired
    ProviderService providerService;


    @Test
    public void testUnserializeDataField() throws Exception {
        Map<String, Object> data = providerService.unserializeDataField("rbt",
                "{\"entries\": [{\"start\": \"1493009003\", \"finish\": \"1493009032\", \"records\": \"[]\", \"to_number\": \"84956651599\", \"from_number\": \"sip:taxi1@vpbx400072064.mangosip.ru\", \"to_extension\": \"\", \"from_extension\": \"1001\", \"disconnect_reason\": \"1120\"}]}");
        MatcherAssert.assertThat(data, is(notNullValue()));
    }


}

