package com.wallet.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.wallet")
public class UserWalletApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserWalletApplication.class, args);
    }

}
