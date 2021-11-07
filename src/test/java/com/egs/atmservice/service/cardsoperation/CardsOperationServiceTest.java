package com.egs.atmservice.service.cardsoperation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;

import com.egs.atmservice.config.cardsession.ValidatedCard;
import com.egs.atmservice.exception.ATMException;

@TestPropertySource(
        properties = {
                "check.balance.operation.code=CHECK_BALANCE",
                "check.balance.service.url=http://localhost:8081/api/cards/operation/checkBalance"
        }
)
@SpringBootTest(classes = CardsOperationServiceImpl.class)
public class CardsOperationServiceTest {

    @MockBean
    private ValidatedCard validatedCard;

    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private CardsOperationServiceImpl cardsOperationService;

    @Test
    public void testGetBalanceException() {
        Mockito.when(validatedCard.getCardNumber()).thenReturn("123");
        Mockito.when(validatedCard.getAllowedActions()).thenReturn(Arrays.asList("DEPOSIT", "WITHDRAWAL"));

        Assertions.assertThrows(ATMException.class, () -> cardsOperationService.getBalance());
    }

    @Test
    public void testGetBalance() {
        Mockito.when(validatedCard.getCardNumber()).thenReturn("123");
        Mockito.when(validatedCard.getAllowedActions()).thenReturn(Collections.singletonList("CHECK_BALANCE"));
        Mockito.when(validatedCard.getCookies()).thenReturn(new ArrayList<>());

        long amount = 100;
        Mockito.when(restTemplate.exchange(Mockito.eq("http://localhost:8081/api/cards/operation/checkBalance?cardNumber=123"), Mockito.eq(HttpMethod.GET),
                Mockito.any(), Mockito.eq(Long.class))).thenReturn(ResponseEntity.of(Optional.of(amount)));

        Assertions.assertEquals(amount, cardsOperationService.getBalance());
    }
}
