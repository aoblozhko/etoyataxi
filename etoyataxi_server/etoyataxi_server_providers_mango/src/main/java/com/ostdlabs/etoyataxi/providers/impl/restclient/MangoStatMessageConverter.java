package com.ostdlabs.etoyataxi.providers.impl.restclient;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.ostdlabs.etoyataxi.providers.impl.dto.MangoStatResponseMessage;
import com.ostdlabs.etoyataxi.providers.impl.dto.MangoStatResponseMessageEntry;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;


public class MangoStatMessageConverter extends AbstractHttpMessageConverter<MangoStatResponseMessage> {

    private ObjectMapper jsonMapper = null;

    public static final MediaType MEDIA_TYPE = new MediaType("text", "csv", Charset.forName("utf-8"));

    public MangoStatMessageConverter() {
        super(MEDIA_TYPE);
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    protected MangoStatResponseMessage readInternal(Class<? extends MangoStatResponseMessage> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        MangoStatResponseMessage message = new MangoStatResponseMessage();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputMessage.getBody()));
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            String[] values = line.split(";");

            MangoStatResponseMessageEntry entry = new MangoStatResponseMessageEntry(values);
            List<String> records = getJsonMapper().readValue(values[0], getJsonMapper().getTypeFactory().constructCollectionType(List.class, String.class));
            entry.setRecords(records);
            message.getEntries().add(entry);
        }
        return message;
    }

    @Override
    protected void writeInternal(MangoStatResponseMessage message, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        throw new IOException("Writing not implemented");
    }


    private ObjectMapper getJsonMapper() {
        if (jsonMapper == null) {
            jsonMapper = new ObjectMapper();
            jsonMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            jsonMapper.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false);
            jsonMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        }
        return jsonMapper;
    }


}
