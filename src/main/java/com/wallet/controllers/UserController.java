package com.wallet.controllers;

import com.wallet.entities.User;
import com.wallet.forms.UserForm;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @PutMapping("/signup")
    public User createUser(@RequestBody UserForm user){
        //TODO : User controller methods
        return null;
    }
}
