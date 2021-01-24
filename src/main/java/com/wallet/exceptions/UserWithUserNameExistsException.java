package com.wallet.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "This User name is taken please use a different one")
public class UserWithUserNameExistsException extends Exception{
    public UserWithUserNameExistsException() {
        super("This User name is taken please use a different one");
    }
}
