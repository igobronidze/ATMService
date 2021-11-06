package com.egs.atmservice.integration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetCardResponse {

    private long id;

    private String cardNumber;

    private String authMethod;

    private String userPersonalId;

    private long amount;
}
