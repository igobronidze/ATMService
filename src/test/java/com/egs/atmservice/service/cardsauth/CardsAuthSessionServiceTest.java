package com.egs.atmservice.service.cardsauth;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;

import com.egs.atmservice.config.cardsession.ValidatedCard;
import com.egs.atmservice.controller.model.IdentifyCardResponse;
import com.egs.atmservice.integration.GetCardResponse;

@SpringBootTest(classes = CardsAuthSessionServiceImpl.class)
@TestPropertySource(
        properties = {
                "get.card.by.number.service.url=http://localhost:8081/api/cards/getByCardNumber"
        }
)
public class CardsAuthSessionServiceTest {

    @MockBean
    private ValidatedCard validatedCard;

    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private CardsAuthSessionServiceImpl cardsAuthSessionService;

    @Test
    public void testIdentifyCard() {
        GetCardResponse getCardResponse = new GetCardResponse();
        getCardResponse.setCardNumber("123");
        getCardResponse.setUserPersonalId("0101");
        getCardResponse.setAuthMethod("PIN");
        Mockito.when(restTemplate.getForObject("http://localhost:8081/api/cards/getByCardNumber?cardNumber=123", GetCardResponse.class))
                .thenReturn(getCardResponse);

        IdentifyCardResponse identifyCardResponse = cardsAuthSessionService.identifyCard("123");
        Assertions.assertEquals("0101", identifyCardResponse.getUserPersonalId());
        Assertions.assertEquals("PIN", identifyCardResponse.getAuthMethod());
    }
}
