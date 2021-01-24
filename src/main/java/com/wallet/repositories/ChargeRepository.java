package com.wallet.repositories;

import com.wallet.entities.Charge;
import com.wallet.enums.ChargeType;
import com.wallet.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChargeRepository extends JpaRepository<Charge,Long> {
    Charge getChargeByChargeTypeAndTransactionType(ChargeType chargeType, TransactionType transactionType);
}
