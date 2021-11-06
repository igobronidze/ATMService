package com.egs.atmservice.integration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepositRequest {

    private String cardNumber;

    private long amount;
}
