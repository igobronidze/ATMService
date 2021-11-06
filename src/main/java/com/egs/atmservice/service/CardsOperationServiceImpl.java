package com.egs.atmservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.egs.atmservice.config.cardsession.ValidatedCard;
import com.egs.atmservice.exception.ATMException;
import com.egs.atmservice.integration.DepositRequest;
import com.egs.atmservice.integration.WithdrawalRequest;

@Service
public class CardsOperationServiceImpl implements CardOperationService {

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
        return restTemplate.getForObject(builder.toUriString(), Long.class);
    }

    @Override
    public void deposit(long amount) {
        validateIfOperationIsAllowed(depositOperationCode);

        DepositRequest depositRequest = new DepositRequest();
        depositRequest.setAmount(amount);
        depositRequest.setCardNumber(validatedCard.getCardNumber());

        String message = restTemplate.postForObject(depositServiceUrl, depositRequest, String.class);
    }

    @Override
    public void withdrawal(long amount) {
        validateIfOperationIsAllowed(withdrawalOperationCode);

        WithdrawalRequest withdrawalRequest = new WithdrawalRequest();
        withdrawalRequest.setAmount(amount);
        withdrawalRequest.setCardNumber(validatedCard.getCardNumber());

        String message = restTemplate.postForObject(withdrawalServiceUrl, withdrawalRequest, String.class);
    }

    private void validateIfOperationIsAllowed(String operationCode) {
        if (validatedCard.getCardNumber() == null) {
            throw new ATMException("Card is not validated");
        }
        if (validatedCard.getAllowedActions() == null) {
            throw new ATMException("Card is not authorized");
        }
        if (!validatedCard.getAllowedActions().contains(operationCode)) {
            throw new ATMException("Operation is not allowed: " + operationCode);
        }
    }
}
