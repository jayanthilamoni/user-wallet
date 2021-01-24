package com.wallet.controllers;

import com.wallet.entities.User;
import com.wallet.entities.Wallet;
import com.wallet.exceptions.AnonymousUserException;
import com.wallet.forms.WalletForm;
import com.wallet.services.UserService;
import com.wallet.services.WalletService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/wallet")
public class WalletController {

    private final WalletService walletService;

    private final UserService userService;

    public WalletController(WalletService walletService, UserService userService) {
        this.walletService = walletService;
        this.userService = userService;
    }

    @PostMapping("/add-money")
    public Wallet addMoneyToWallet(@RequestBody WalletForm amount) throws AnonymousUserException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();
        if(principal instanceof org.springframework.security.core.userdetails.User){
            org.springframework.security.core.userdetails.User principalUser = (org.springframework.security.core.userdetails.User) principal;
            User user = userService.findUserByUserName(principalUser.getUsername());
            BigDecimal addValue = new BigDecimal(amount.getAmount());
            return walletService.addMoney(addValue,user);
        }
        throw new AnonymousUserException();
    }
}
