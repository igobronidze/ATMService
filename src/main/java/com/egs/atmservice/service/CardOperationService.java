package com.egs.atmservice.service;

public interface CardOperationService {

    long getBalance();

    void deposit(long amount);

    void withdrawal(long amount);
}
