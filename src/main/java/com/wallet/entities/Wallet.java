package com.wallet.entities;

import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @NonNull
    public BigDecimal balance;

    @Column(name = "creation-date")
    public Date createdDate;

    @Column(name="last-modified")
    @Temporal(TemporalType.TIMESTAMP)
    public Date lastModified;

    @OneToMany
    @JoinColumn(name = "transaction_id")
    public List<Transaction> transactions;
}
