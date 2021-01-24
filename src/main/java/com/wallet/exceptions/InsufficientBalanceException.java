package com.wallet.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST,reason = "Insufficient wallet in balance")
public class InsufficientBalanceException extends Exception{
    public InsufficientBalanceException() {
        super("Insufficient wallet in balance");
    }
}
