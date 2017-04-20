package com.ostdlabs.etoyataxi.providers.impl;

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



public class MangoStatMessageConverter extends AbstractHttpMessageConverter<MangoStatResponseMessage> {

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
            message.getEntries().add(entry);
        }
        return message;
    }

    @Override
    protected void writeInternal(MangoStatResponseMessage message, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        throw new IOException("Writing not implemented");
    }

}
