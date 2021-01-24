package com.wallet.services;

import com.wallet.entities.Charge;
import com.wallet.entities.Transaction;
import com.wallet.entities.User;
import com.wallet.entities.Wallet;
import com.wallet.enums.ChargeType;
import com.wallet.enums.TransactionStatus;
import com.wallet.enums.TransactionType;
import com.wallet.exceptions.InsufficientBalanceException;
import com.wallet.exceptions.NoTransactionWithIdException;
import com.wallet.repositories.ChargeRepository;
import com.wallet.repositories.TransactionRepository;
import com.wallet.repositories.UserRepository;
import com.wallet.repositories.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
public class WalletService {
    private final WalletRepository repository;

    private final TransactionRepository transactionRepository;

    private final UserRepository userRepository;

    private final ChargeRepository chargeRepository;

    public WalletService(WalletRepository repository, TransactionRepository transactionRepository, UserRepository userRepository, ChargeRepository chargeRepository) {
        this.repository = repository;
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.chargeRepository = chargeRepository;
    }

    public void saveWallet(Wallet wallet){
        repository.save(wallet);
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
        BigDecimal amountToDebit = amount.add(calculateCharges(amount,ChargeType.CHARGE,TransactionType.DEBIT))
                .add(calculateCharges(amount,ChargeType.COMMISSION,TransactionType.DEBIT));
        BigDecimal amountToCredit = amount.subtract(calculateCharges(amount,ChargeType.CHARGE,TransactionType.CREDIT))
                .subtract(calculateCharges(amount,ChargeType.COMMISSION,TransactionType.CREDIT));
        fromUserWallet.setBalance(fromUserWallet.getBalance().subtract(amountToDebit));
        toUserWallet.setBalance(toUserWallet.getBalance().add(amountToCredit));
        repository.save(fromUserWallet);
        repository.save(toUserWallet);
        creditTransaction.setAmount(amountToDebit);
        debitTransaction.setAmount(amountToCredit);
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

    @Transactional(rollbackFor = Exception.class)
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

    public BigDecimal calculateCharges(BigDecimal amount,ChargeType chargeType, TransactionType transactionType){
        Charge charge = chargeRepository.getChargeByChargeTypeAndTransactionType(chargeType,transactionType);
        return amount.multiply(charge.getChargePercentage()).divide(new BigDecimal(100), RoundingMode.HALF_DOWN);
    }

}
