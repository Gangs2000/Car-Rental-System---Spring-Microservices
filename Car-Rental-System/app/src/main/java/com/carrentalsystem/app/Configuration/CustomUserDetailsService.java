package com.carrentalsystem.app.Configuration;

import com.carrentalsystem.app.Model.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired UserAccountRepository userAccountRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserAccount>  userAccount=userAccountRepository.findById(username);
        if(userAccount.isEmpty())
            throw new UsernameNotFoundException("Given email ID is not exist..");
        return new User(userAccount.get().getEmailId(), userAccount.get().getPassword(), new LinkedList<>());
    }
}
