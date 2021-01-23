package com.wallet.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication(scanBasePackages = {"com.wallet.entities"
        ,"com.wallet.app"})
@EntityScan(basePackages = "com.wallet.entities")
public class UserWalletApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserWalletApplication.class, args);
    }

}
