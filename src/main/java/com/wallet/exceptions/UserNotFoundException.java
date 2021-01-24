package com.wallet.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST,reason = "User does not exist")
public class UserNotFoundException extends Exception{
    public UserNotFoundException() {
        super("User does not exist");
    }
}
