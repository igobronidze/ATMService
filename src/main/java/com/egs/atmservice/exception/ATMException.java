package com.egs.atmservice.exception;

public class ATMException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ATMException(String message) {
        super(message);
    }
}
