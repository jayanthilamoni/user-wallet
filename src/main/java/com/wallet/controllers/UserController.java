package com.wallet.controllers;

import com.wallet.entities.User;
import com.wallet.entities.Wallet;
import com.wallet.exceptions.UserWithUserNameExistsException;
import com.wallet.forms.UserForm;
import com.wallet.services.UserService;
import com.wallet.services.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;

@RestController
public class UserController {

    private final UserService userService;

    private final WalletService walletService;

    @Autowired
    public UserController(UserService userService, WalletService walletService) {
        this.userService = userService;
        this.walletService = walletService;
    }

    @GetMapping("/")
    public String get(){
        return "Welcome";
    }
    @PutMapping("/signup")
    public User createUser(@RequestBody UserForm user) throws UserWithUserNameExistsException {
        User existingUser = userService.findUserByUserName(user.getUsername());
        if(existingUser!=null){
            throw new UserWithUserNameExistsException();
        }
        User newUser = new User(user.getUsername(),user.getPassword());
        Wallet wallet = new Wallet(new BigDecimal(0),new Date());
        walletService.saveWallet(wallet);
        newUser.setWallet(wallet);
        return userService.saveUser(newUser);
    }
}
