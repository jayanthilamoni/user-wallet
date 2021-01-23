package com.wallet.entities;

import org.springframework.lang.NonNull;

import javax.persistence.*;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long uid;

    @NonNull
    @Column(name = "user_name")
    public String userName;

    @NonNull
    public String password;

    @OneToOne
    @JoinColumn(name = "wallet_id")
    public Wallet wallet;
}
