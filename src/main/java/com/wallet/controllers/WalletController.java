package com.wallet.controllers;

import com.wallet.entities.Transaction;
import com.wallet.entities.User;
import com.wallet.entities.Wallet;
import com.wallet.enums.TransactionStatus;
import com.wallet.exceptions.AnonymousUserException;
import com.wallet.exceptions.InsufficientBalanceException;
import com.wallet.exceptions.NoTransactionWithIdException;
import com.wallet.exceptions.UserNotFoundException;
import com.wallet.forms.TransactionForm;
import com.wallet.forms.WalletForm;
import com.wallet.services.UserService;
import com.wallet.services.WalletService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/transfer-money")
    public Transaction transferAmount(@RequestBody TransactionForm transactionForm) throws UserNotFoundException, InsufficientBalanceException {
        User fromUser = userService.findUserByUserName(transactionForm.getFromUser());
        User toUser = userService.findUserByUserName(transactionForm.getToUser());
        if(fromUser==null || toUser==null){
            throw new UserNotFoundException();
        }
        BigDecimal amount = new BigDecimal(transactionForm.getAmount());
        return walletService.transferMoney(amount,fromUser,toUser);
    }

    @GetMapping("/transaction-status/{id}")
    public TransactionStatus getTransactionStatus(@PathVariable String id){
        return walletService.getTransactionStatus(id).getTransactionStatus();
    }

    @DeleteMapping("/reverse-transaction/{id}")
    public Transaction reverseTransaction(@PathVariable String id) throws InsufficientBalanceException, NoTransactionWithIdException {
        return walletService.reverseTransaction(id);
    }
}
