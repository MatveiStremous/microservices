package com.example.bookservice.service;

import com.example.bookservice.dto.ErrorResponse;
import com.example.bookservice.exception.BusinessException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

@RequiredArgsConstructor
public class RestTemplateErrorHandler implements ResponseErrorHandler {
    private final ObjectMapper objectMapper;

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return !response.getStatusCode().is2xxSuccessful();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        throw new BusinessException((HttpStatus) response.getStatusCode(), objectMapper.readValue(response.getBody(), ErrorResponse.class).getMessage());
    }
}
