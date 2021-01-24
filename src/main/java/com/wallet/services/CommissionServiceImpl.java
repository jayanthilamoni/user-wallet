package com.wallet.services;

import com.wallet.enums.TransactionType;

import java.math.BigDecimal;

public class CommissionServiceImpl implements CommissionService{
    @Override
    public BigDecimal getPlatformCharge(TransactionType transactionType) {
        return null;
    }

    @Override
    public BigDecimal getPlatformCommission(TransactionType transactionType) {
        return null;
    }
}
