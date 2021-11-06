package com.egs.atmservice.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.egs.atmservice.controller.model.IdentifyCardResponse;
import com.egs.atmservice.service.CardsAuthSessionService;
import com.egs.atmservice.service.CardsAuthSessionServiceImpl;

@RestController
@RequestMapping("api/cards/auth")
public class CardsAuthSessionController {

    private final CardsAuthSessionService cardsAuthSessionService;

    @Autowired
    public CardsAuthSessionController(CardsAuthSessionServiceImpl cardsService) {
        this.cardsAuthSessionService = cardsService;
    }

    @PostMapping("identifyCard")
    public IdentifyCardResponse identifyCard(@RequestParam(value = "cardNumber") String cardNumber) {
        return cardsAuthSessionService.identifyCard(cardNumber);
    }

    @PostMapping("cleanIdentifiedSession")
    public void cleanIdentifiedSession() {
        cardsAuthSessionService.cleanIdentifiedCardSession();
    }

    @PostMapping("cardAuthorization")
    public void cardAuthorization(@RequestParam(value = "code") String code) {
        cardsAuthSessionService.cardAuthorization(code);
    }
}
