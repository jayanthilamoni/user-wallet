package com.wallet.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication(scanBasePackages = {"com.wallet.app","com.wallet.services",
        "com.wallet.controllers"})
@EntityScan(basePackages = "com.wallet.entities")
@EnableJpaRepositories("com.wallet.repositories")
public class UserWalletApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserWalletApplication.class, args);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
