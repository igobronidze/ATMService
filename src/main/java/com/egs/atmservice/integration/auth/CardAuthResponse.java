package com.egs.atmservice.integration.auth;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardAuthResponse {

    private String cardNumber;

    private long amount;

    private List<String> allowedActions = new ArrayList<>();
}
