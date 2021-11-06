package com.egs.atmservice.config.cardsession;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class ValidatedCardBean {

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public ValidatedCard getValidatedCardSingleton() {
        return new ValidatedCard();
    }
}