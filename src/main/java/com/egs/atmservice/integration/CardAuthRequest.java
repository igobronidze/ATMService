package com.egs.atmservice.integration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardAuthRequest {

    private String cardNumber;

    private String code;
}
