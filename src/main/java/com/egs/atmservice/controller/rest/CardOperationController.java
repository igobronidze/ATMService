package com.egs.atmservice.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.egs.atmservice.service.cardsoperation.CardsOperationService;
import com.egs.atmservice.service.cardsoperation.CardsOperationServiceImpl;

@RestController
@RequestMapping("api/cards/operation")
public class CardOperationController {

    private final CardsOperationService cardsOperationService;

    @Autowired
    public CardOperationController(CardsOperationServiceImpl cardOperationService) {
        this.cardsOperationService = cardOperationService;
    }

    @PostMapping("checkBalance")
    public long checkBalance() {
        return cardsOperationService.getBalance();
    }

    @PostMapping("deposit")
    public void deposit(@RequestParam(value = "amount") long amount) {
        cardsOperationService.deposit(amount);
    }

    @PostMapping("withdrawal")
    public void withdrawal(@RequestParam(value = "amount") long amount) {
        cardsOperationService.withdrawal(amount);
    }
}
