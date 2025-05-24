package com.odgoods.authservice.domain.auth.service;

import com.odgoods.authservice.common.exception.StatusBasedException.ConflictException;
import com.odgoods.authservice.domain.auth.entity.User;
import com.odgoods.authservice.domain.auth.repository.UserRepository;
import com.odgoods.authservice.domain.auth.security.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ConflictException("User not found with email: " + email));

        return new CustomUserDetails(user);
    }

}
