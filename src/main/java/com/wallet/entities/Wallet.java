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

    @Column(name = "creation_date")
    public Date createdDate;

    @Column(name="last_modified")
    @Temporal(TemporalType.TIMESTAMP)
    public Date lastModified;

    @OneToOne(mappedBy = "wallet")
    public User user;

    @OneToMany
    public List<Transaction> transactions;
}
