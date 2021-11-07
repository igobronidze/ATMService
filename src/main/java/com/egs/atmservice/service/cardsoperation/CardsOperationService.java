package com.egs.atmservice.service.cardsoperation;

public interface CardsOperationService {

    long getBalance();

    void deposit(long amount);

    void withdrawal(long amount);
}
