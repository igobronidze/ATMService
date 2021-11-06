package com.egs.atmservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateBean {

    @Value("${services.connection.timeout}")
    private Integer erpServicesConnectionTimeout;

    @Value("${services.read.timeout}")
    private Integer erpServicesReadTimeout;

    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(erpServicesConnectionTimeout);
        factory.setReadTimeout(erpServicesReadTimeout);

        RestTemplate restTemplate = new RestTemplate(factory);
        restTemplate.setErrorHandler(new ATMIntegrationErrorHandler());
        return restTemplate;
    }
}
