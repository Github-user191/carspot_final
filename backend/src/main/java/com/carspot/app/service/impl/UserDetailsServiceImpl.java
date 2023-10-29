package com.carspot.app.service.impl;

import com.carspot.app.entity.User;
import com.carspot.app.exception.exceptions.AccountNotVerifiedException;
import com.carspot.app.exception.exceptions.AuthenticationException;
import com.carspot.app.exception.exceptions.InvalidTokenException;
import com.carspot.app.repository.UserRepository;
import com.carspot.app.security.MyUserDetails;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String emailAddress) throws UsernameNotFoundException {

        User user = userRepository.findUserByEmailAddress(emailAddress);


        if(user == null) {
            throw new AuthenticationException("Invalid credentials");
        }
        if(!user.isEmailVerified()) {
            throw new AccountNotVerifiedException("Your account is not verified. Please check your email for a confirmation link");
        }

        return MyUserDetails.createUser(user);
    }

}
