package com.wallet.services;

import com.wallet.entities.User;
import com.wallet.entities.Wallet;
import com.wallet.repositories.WalletRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class WalletService {
    private final WalletRepository repository;

    public WalletService(WalletRepository repository) {
        this.repository = repository;
    }

    public Wallet saveWallet(Wallet wallet){
        return repository.save(wallet);
    }

    public Wallet addMoney(BigDecimal amount, User user){
        Wallet wallet = user.getWallet();
        wallet.setBalance(wallet.getBalance().add(amount));
        return repository.save(wallet);
    }
}
