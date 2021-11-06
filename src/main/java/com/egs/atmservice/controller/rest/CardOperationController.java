package com.egs.atmservice.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.egs.atmservice.service.CardOperationService;
import com.egs.atmservice.service.CardsOperationServiceImpl;

@RestController
@RequestMapping("api/cards/operation")
public class CardOperationController {

    private final CardOperationService cardOperationService;

    @Autowired
    public CardOperationController(CardsOperationServiceImpl cardOperationService) {
        this.cardOperationService = cardOperationService;
    }

    @PostMapping("checkBalance")
    public long checkBalance() {
        return cardOperationService.getBalance();
    }

    @PostMapping("deposit")
    public void deposit(@RequestParam(value = "amount") long amount) {
        cardOperationService.deposit(amount);
    }

    @PostMapping("withdrawal")
    public void withdrawal(@RequestParam(value = "amount") long amount) {
        cardOperationService.withdrawal(amount);
    }
}
