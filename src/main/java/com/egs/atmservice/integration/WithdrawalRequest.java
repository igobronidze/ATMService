package com.egs.atmservice.integration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WithdrawalRequest {

    private String cardNumber;

    private long amount;
}
