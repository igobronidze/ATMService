package com.egs.atmservice.integration.operation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepositRequest {

    private String cardNumber;

    private long amount;
}
