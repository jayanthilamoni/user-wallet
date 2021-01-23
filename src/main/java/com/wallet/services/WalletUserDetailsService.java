package com.wallet.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class WalletUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Autowired
    public WalletUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        com.wallet.entities.User user = userService.findUserByUserName(userName);
        return new User(user.getUserName(),user.getPassword(),
                true,true,true,true, Collections.singletonList(new SimpleGrantedAuthority("USER")));
    }
}
