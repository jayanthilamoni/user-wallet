package com.wallet.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST,reason = "Please login to perform action")
public class AnonymousUserException extends Exception{
    public AnonymousUserException() {
        super("Please login to perform action");
    }
}
