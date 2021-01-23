package com.wallet.entities;

import com.wallet.enums.TransactionType;

import javax.persistence.*;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Column(name = "transaction_type")
    public TransactionType transactionType;

    @ManyToOne(targetEntity = Wallet.class)
    public Wallet wallet;
}
