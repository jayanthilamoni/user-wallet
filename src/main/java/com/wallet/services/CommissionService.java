package com.wallet.services;

import com.wallet.enums.TransactionType;

import java.math.BigDecimal;

public interface CommissionService {
    BigDecimal getPlatformCharge(TransactionType transactionType);
    BigDecimal getPlatformCommission(TransactionType transactionType);
}
