package com.wallet.repositories;

import com.wallet.entities.Transaction;
import com.wallet.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    Transaction getTransactionById(Long id);
    List<Transaction> getAllByFromUser(User user);
}
