package com.egs.atmservice.service.cardsauth;

import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.egs.atmservice.config.cardsession.ValidatedCard;
import com.egs.atmservice.controller.model.IdentifyCardResponse;
import com.egs.atmservice.exception.ATMException;
import com.egs.atmservice.integration.auth.CardAuthRequest;
import com.egs.atmservice.integration.auth.CardAuthResponse;
import com.egs.atmservice.integration.GetCardResponse;

@Service
public class CardsAuthSessionServiceImpl implements CardsAuthSessionService {

    private static final Logger logger = LoggerFactory.getLogger(CardsAuthSessionServiceImpl.class);

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

        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", validatedCard.getCookies().stream().collect(Collectors.joining(";")));
        HttpEntity<String> entity = new HttpEntity<>(headers);

        restTemplate.postForObject(closeSessionService, entity, Void.class);
    }

    @Override
    public String cardAuthorization(String code) {
        if (validatedCard.getCardNumber() == null) {
            logger.warn("Card is not identified");
            throw new ATMException("Card is not identified");
        }

        CardAuthRequest cardAuthRequest = new CardAuthRequest();
        cardAuthRequest.setCardNumber(validatedCard.getCardNumber());
        cardAuthRequest.setCode(code);
        HttpEntity<CardAuthRequest> entity = new HttpEntity<>(cardAuthRequest);

        ResponseEntity<CardAuthResponse> responseEntity = restTemplate.postForEntity(authCardServiceUrl, entity, CardAuthResponse.class);
        CardAuthResponse cardAuthResponse = responseEntity.getBody();
        validatedCard.setAmount(cardAuthResponse.getAmount());
        validatedCard.setAllowedActions(cardAuthResponse.getAllowedActions());

        validatedCard.setCookies(responseEntity.getHeaders().get("Set-Cookie"));

        logger.info("Successful authorization");
        return "Successful authorization";
    }
}
