package com.egs.atmservice.service.cardsoperation;

import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.egs.atmservice.config.cardsession.ValidatedCard;
import com.egs.atmservice.exception.ATMException;
import com.egs.atmservice.integration.DepositRequest;
import com.egs.atmservice.integration.WithdrawalRequest;

@Service
public class CardsOperationServiceImpl implements CardsOperationService {

    private final static Logger logger = LoggerFactory.getLogger(CardsOperationServiceImpl.class);

    @Value("${check.balance.operation.code}")
    private String checkBalanceOperationCode;
    @Value("${deposit.operation.code}")
    private String depositOperationCode;
    @Value("${withdrawal.operation.code}")
    private String withdrawalOperationCode;

    @Value("${check.balance.service.url}")
    private String checkBalanceServiceUrl;
    @Value("${deposit.service.url}")
    private String depositServiceUrl;
    @Value("${withdrawal.service.url}")
    private String withdrawalServiceUrl;

    private final ValidatedCard validatedCard;

    private final RestTemplate restTemplate;

    @Autowired
    public CardsOperationServiceImpl(ValidatedCard validatedCard, RestTemplate restTemplate) {
        this.validatedCard = validatedCard;
        this.restTemplate = restTemplate;
    }

    @Override
    public long getBalance() {
        validateIfOperationIsAllowed(checkBalanceOperationCode);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(checkBalanceServiceUrl)
                .queryParam("cardNumber", validatedCard.getCardNumber());

        HttpEntity<String> entity = new HttpEntity<>(getHttpHeaders());

        return restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, Long.class).getBody();
    }

    @Override
    public void deposit(long amount) {
        validateIfOperationIsAllowed(depositOperationCode);

        DepositRequest depositRequest = new DepositRequest();
        depositRequest.setAmount(amount);
        depositRequest.setCardNumber(validatedCard.getCardNumber());

        HttpEntity<DepositRequest> httpEntity = new HttpEntity<>(depositRequest, getHttpHeaders());

        String message = restTemplate.postForObject(depositServiceUrl, httpEntity, String.class);
        logger.info("Deposit result: " + message);
    }

    @Override
    public void withdrawal(long amount) {
        validateIfOperationIsAllowed(withdrawalOperationCode);

        WithdrawalRequest withdrawalRequest = new WithdrawalRequest();
        withdrawalRequest.setAmount(amount);
        withdrawalRequest.setCardNumber(validatedCard.getCardNumber());

        HttpEntity<WithdrawalRequest> httpEntity = new HttpEntity<>(withdrawalRequest, getHttpHeaders());

        String message = restTemplate.postForObject(withdrawalServiceUrl, httpEntity, String.class);
        logger.info("Withdrawal result: " + message);
    }

    private void validateIfOperationIsAllowed(String operationCode) {
        if (validatedCard.getCardNumber() == null) {
            throw new ATMException("Card is not identified");
        }
        if (validatedCard.getAllowedActions() == null) {
            throw new ATMException("Card is not authorized");
        }
        if (!validatedCard.getAllowedActions().contains(operationCode)) {
            throw new ATMException("Operation is not allowed: " + operationCode);
        }
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", validatedCard.getCookies().stream().collect(Collectors.joining(";")));
        return headers;
    }
}
