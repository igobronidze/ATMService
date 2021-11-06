package com.egs.atmservice.service;

import com.egs.atmservice.controller.model.IdentifyCardResponse;

public interface CardsAuthSessionService {

    IdentifyCardResponse identifyCard(String cardNumber);

    void cleanIdentifiedCardSession();

    String cardAuthorization(String code);
}
