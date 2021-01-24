package com.wallet.services;

import com.wallet.entities.Transaction;
import com.wallet.entities.User;
import com.wallet.entities.Wallet;
import com.wallet.enums.TransactionType;
import com.wallet.exceptions.InsufficientBalanceException;
import com.wallet.repositories.TransactionRepository;
import com.wallet.repositories.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class WalletService {
    private final WalletRepository repository;

    private final TransactionRepository transactionRepository;

    public WalletService(WalletRepository repository, TransactionRepository transactionRepository) {
        this.repository = repository;
        this.transactionRepository = transactionRepository;
    }

    public Wallet saveWallet(Wallet wallet){
        return repository.save(wallet);
    }

    public Wallet addMoney(BigDecimal amount, User user){
        Wallet wallet = user.getWallet();
        wallet.setBalance(wallet.getBalance().add(amount));
        return repository.save(wallet);
    }

    @Transactional(rollbackFor = Exception.class)
    public Transaction transferMoney(BigDecimal amount,User fromUser, User toUser) throws InsufficientBalanceException {
        if(fromUser.getWallet().getBalance().compareTo(amount)<0){
            throw new InsufficientBalanceException();
        }
        Wallet fromUserWallet = fromUser.getWallet();
        Wallet toUserWallet = toUser.getWallet();
        fromUserWallet.setBalance(fromUserWallet.getBalance().subtract(amount));
        toUserWallet.setBalance(toUserWallet.getBalance().add(amount));
        repository.save(fromUserWallet);
        repository.save(toUserWallet);
        Transaction creditTransaction = getTransaction(fromUser,toUser,amount,TransactionType.CREDIT);
        transactionRepository.save(creditTransaction);
        Transaction debtTransaction = getTransaction(toUser,fromUser,amount,TransactionType.DEBIT);
        transactionRepository.save(debtTransaction);
        return creditTransaction;
    }

    private Transaction getTransaction(User fromUser,User toUser,BigDecimal amount,TransactionType transactionType){
        Transaction transaction = new Transaction();
        transaction.setTransactionType(transactionType);
        transaction.setFromUser(fromUser);
        transaction.setToUser(toUser.getUserName());
        transaction.setAmount(amount);
        return transaction;
    }
}
