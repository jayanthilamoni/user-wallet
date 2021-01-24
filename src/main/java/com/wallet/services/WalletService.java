package com.wallet.services;

import com.wallet.entities.Transaction;
import com.wallet.entities.User;
import com.wallet.entities.Wallet;
import com.wallet.enums.TransactionStatus;
import com.wallet.enums.TransactionType;
import com.wallet.exceptions.InsufficientBalanceException;
import com.wallet.exceptions.NoTransactionWithIdException;
import com.wallet.exceptions.UserNotFoundException;
import com.wallet.repositories.TransactionRepository;
import com.wallet.repositories.UserRepository;
import com.wallet.repositories.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class WalletService {
    private final WalletRepository repository;

    private final TransactionRepository transactionRepository;

    private final UserRepository userRepository;

    public WalletService(WalletRepository repository, TransactionRepository transactionRepository, UserRepository userRepository) {
        this.repository = repository;
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
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
        //Transaction initiated
        Transaction creditTransaction = getTransaction(fromUser,toUser,amount,TransactionType.CREDIT);
        Transaction debitTransaction = getTransaction(toUser,fromUser,amount,TransactionType.DEBIT);
        return transferMoney(amount,fromUser,toUser,creditTransaction,debitTransaction);
    }

    private Transaction transferMoney(BigDecimal amount, User fromUser, User toUser, Transaction creditTransaction, Transaction debitTransaction) throws InsufficientBalanceException {
        creditTransaction.setTransactionStatus(TransactionStatus.INITIATED);
        debitTransaction.setTransactionStatus(TransactionStatus.INITIATED);
        transactionRepository.save(creditTransaction);
        transactionRepository.save(debitTransaction);
        //Balance check
        if(fromUser.getWallet().getBalance().compareTo(amount)<0){
            creditTransaction.setTransactionStatus(TransactionStatus.ABORTED);
            debitTransaction.setTransactionStatus(TransactionStatus.ABORTED);
            transactionRepository.save(creditTransaction);
            transactionRepository.save(debitTransaction);
            throw new InsufficientBalanceException();
        }
        Wallet fromUserWallet = fromUser.getWallet();
        Wallet toUserWallet = toUser.getWallet();
        fromUserWallet.setBalance(fromUserWallet.getBalance().subtract(amount));
        toUserWallet.setBalance(toUserWallet.getBalance().add(amount));
        repository.save(fromUserWallet);
        repository.save(toUserWallet);
        creditTransaction.setTransactionStatus(TransactionStatus.SUCCESS);
        debitTransaction.setTransactionStatus(TransactionStatus.SUCCESS);
        transactionRepository.save(creditTransaction);
        transactionRepository.save(debitTransaction);
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

    public Transaction getTransactionStatus(String transactionId){
        Long id = Long.valueOf(transactionId);
        return transactionRepository.getTransactionById(id);
    }

    public Transaction reverseTransaction(String transactionId) throws NoTransactionWithIdException, InsufficientBalanceException {
        Long id = Long.valueOf(transactionId);
        Transaction transaction = transactionRepository.getTransactionById(id);
        if(transaction==null){
            throw new NoTransactionWithIdException();
        }
        User fromUser = transaction.getFromUser();
        User toUser = userRepository.findUserByUserName(transaction.getToUser());
        Transaction creditReversal = getTransaction(fromUser,toUser,transaction.getAmount(),TransactionType.DEBIT);
        Transaction debitReversal = getTransaction(toUser,fromUser,transaction.getAmount(),TransactionType.CREDIT);
        return transferMoney(transaction.getAmount(),toUser,fromUser,creditReversal,debitReversal);
    }

    public List<Transaction> getAllTransactionOfUser(User user){
        return transactionRepository.getAllByFromUser(user);
    }
}
