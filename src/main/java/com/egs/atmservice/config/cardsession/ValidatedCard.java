package com.egs.atmservice.config.cardsession;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidatedCard {

    private String cardNumber;

    private long amount;

    private List<String> allowedActions;

    private List<String> cookies = new ArrayList<>();
}
