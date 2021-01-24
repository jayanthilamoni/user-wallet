package com.wallet.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST,reason = "No Transaction with the ID found")
public class NoTransactionWithIdException extends Exception{
    public NoTransactionWithIdException() {
        super("No Transaction with the ID found");
    }
}
