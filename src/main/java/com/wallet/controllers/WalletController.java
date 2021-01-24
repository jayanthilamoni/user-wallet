package com.wallet.controllers;

import com.wallet.entities.Transaction;
import com.wallet.entities.User;
import com.wallet.entities.Wallet;
import com.wallet.enums.TransactionStatus;
import com.wallet.exceptions.*;
import com.wallet.forms.TransactionForm;
import com.wallet.forms.WalletForm;
import com.wallet.services.UserService;
import com.wallet.services.WalletService;
import org.hibernate.Hibernate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

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
    public Transaction transferAmount(@RequestBody TransactionForm transactionForm) throws UserNotFoundException, InsufficientBalanceException, AnonymousUserException, SenderSameAsCurrentUserException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();
        User fromUser = null;
        if(principal instanceof org.springframework.security.core.userdetails.User) {
            org.springframework.security.core.userdetails.User principalUser = (org.springframework.security.core.userdetails.User) principal;
            fromUser = userService.findUserByUserName(principalUser.getUsername());
        }
        if(fromUser==null){
            throw new AnonymousUserException();
        }
        User toUser = userService.findUserByUserName(transactionForm.getToUser());
        if(toUser==null){
            throw new UserNotFoundException();
        }
        if(fromUser.getUid().equals(toUser.getUid())){
            throw new SenderSameAsCurrentUserException();
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

    @GetMapping("/all-transactions")
    public List<Transaction> getAllTransactions() throws AnonymousUserException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();
        if(principal instanceof org.springframework.security.core.userdetails.User) {
            org.springframework.security.core.userdetails.User principalUser = (org.springframework.security.core.userdetails.User) principal;
            User user = userService.findUserByUserName(principalUser.getUsername());
            return walletService.getAllTransactionOfUser(user);
        }
        throw new AnonymousUserException();
    }

    @GetMapping("/show-balance")
    public BigDecimal getWallet() throws AnonymousUserException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();
        if(principal instanceof org.springframework.security.core.userdetails.User) {
            org.springframework.security.core.userdetails.User principalUser = (org.springframework.security.core.userdetails.User) principal;
            User user = userService.findUserByUserName(principalUser.getUsername());
            return user.getWallet().getBalance();
        }
        throw new AnonymousUserException();
    }
}
