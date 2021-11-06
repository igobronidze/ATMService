package com.egs.atmservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.egs.atmservice.config.cardsession.ValidatedCard;
import com.egs.atmservice.controller.model.IdentifyCardResponse;
import com.egs.atmservice.exception.ATMException;
import com.egs.atmservice.integration.CardAuthRequest;
import com.egs.atmservice.integration.CardAuthResponse;
import com.egs.atmservice.integration.GetCardResponse;

@Service
public class CardsAuthSessionServiceImpl implements CardsAuthSessionService {

    @Value("${get.card.by.number.service.url}")
    private String getCardByNumberServiceUrl;

    @Value("${auth.card.service.url}")
    private String authCardServiceUrl;

    @Value("${close.session.service.url}")
    private String closeSessionService;

    private final RestTemplate restTemplate;

    private final ValidatedCard validatedCard;

    @Autowired
    public CardsAuthSessionServiceImpl(RestTemplate restTemplate, ValidatedCard validatedCard) {
        this.restTemplate = restTemplate;
        this.validatedCard = validatedCard;
    }

    @Override
    public IdentifyCardResponse identifyCard(String cardNumber) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getCardByNumberServiceUrl)
                .queryParam("cardNumber", cardNumber);

        GetCardResponse getCardResponse = restTemplate.getForObject(builder.toUriString(), GetCardResponse.class);

        IdentifyCardResponse identifyCardResponse = new IdentifyCardResponse();
        identifyCardResponse.setUserPersonalId(getCardResponse.getUserPersonalId());
        identifyCardResponse.setAuthMethod(getCardResponse.getAuthMethod());

        validatedCard.setCardNumber(cardNumber);

        return identifyCardResponse;
    }

    @Override
    public void cleanIdentifiedCardSession() {
        validatedCard.setCardNumber(null);
        validatedCard.setAmount(0);

        restTemplate.postForObject(closeSessionService, null, Void.class);
    }

    @Override
    public String cardAuthorization(String code) {
        if (validatedCard.getCardNumber() == null) {
            throw new ATMException("Card is not validated");
        }

        CardAuthRequest cardAuthRequest = new CardAuthRequest();
        cardAuthRequest.setCardNumber(validatedCard.getCardNumber());
        cardAuthRequest.setCode(code);
        HttpEntity<CardAuthRequest> entity = new HttpEntity<>(cardAuthRequest);

        CardAuthResponse cardAuthResponse = restTemplate.postForObject(authCardServiceUrl, entity, CardAuthResponse.class);
        validatedCard.setAmount(cardAuthResponse.getAmount());
        validatedCard.setAllowedActions(cardAuthResponse.getAllowedActions());

        return "Successful authorization";
    }
}
