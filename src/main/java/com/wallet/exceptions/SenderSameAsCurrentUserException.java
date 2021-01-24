package com.wallet.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST,reason = "Sender same as current exception")
public class SenderSameAsCurrentUserException extends Exception{
    public SenderSameAsCurrentUserException() {
        super("Sender same as current exception");
    }
}
