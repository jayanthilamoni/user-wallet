package com.wallet.controllers;

import com.wallet.entities.User;
import com.wallet.forms.UserForm;
import com.wallet.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/signup")
    public User createUser(@RequestBody UserForm user){
        return userService.saveUser(new User(user.getUsername(),user.getPassword()));
    }
}
