package com.egs.atmservice.config;

import java.io.IOException;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.DefaultResponseErrorHandler;

import com.egs.atmservice.exception.ATMException;

public class ATMIntegrationErrorHandler extends DefaultResponseErrorHandler {

    private final Logger logger = LoggerFactory.getLogger(ATMIntegrationErrorHandler.class);

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        String responseMessage = StreamUtils.copyToString(response.getBody(), Charset.defaultCharset());
        logger.warn("Integration error: " + responseMessage);
        throw new ATMException(responseMessage);
    }
}