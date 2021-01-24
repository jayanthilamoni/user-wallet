package com.wallet.entities;

import com.wallet.enums.ChargeType;
import com.wallet.enums.TransactionType;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class Charge {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Column(name = "transaction")
    public TransactionType transactionType;

    @Column(name = "charge_type")
    public ChargeType chargeType;

    @Column(name = "charge")
    public BigDecimal chargePercentage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public ChargeType getChargeType() {
        return chargeType;
    }

    public void setChargeType(ChargeType chargeType) {
        this.chargeType = chargeType;
    }

    public BigDecimal getChargePercentage() {
        return chargePercentage;
    }

    public void setChargePercentage(BigDecimal chargePercentage) {
        this.chargePercentage = chargePercentage;
    }
}
