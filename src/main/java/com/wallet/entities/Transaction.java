package com.wallet.entities;

import com.wallet.enums.TransactionType;

import javax.persistence.*;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "transaction_type")
    private TransactionType transactionType;

    @ManyToOne(targetEntity = Wallet.class)
    private Wallet wallet;

    public Transaction(Long id, TransactionType transactionType, Wallet wallet) {
        this.id = id;
        this.transactionType = transactionType;
        this.wallet = wallet;
    }

    public Transaction() {
    }

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

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }
}
