package com.egs.atmservice.controller.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IdentifyCardResponse {

    private String userPersonalId;

    private String authMethod;
}
