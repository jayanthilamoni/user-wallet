package com.wallet.repositories;

import com.wallet.entities.Transaction;
import com.wallet.enums.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    Transaction getTransactionById(Long id);
}
