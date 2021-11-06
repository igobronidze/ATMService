package com.egs.atmservice.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.egs.atmservice.exception.ATMException;

@ControllerAdvice
public class CustomExceptionController {

    @ExceptionHandler(value = ATMException.class)
    public ResponseEntity<Object> exception(ATMException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
