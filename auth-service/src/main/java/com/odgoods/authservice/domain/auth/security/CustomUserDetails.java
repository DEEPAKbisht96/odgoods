package com.odgoods.authservice.domain.auth.security;

import java.util.Collection;
import java.util.List;

import com.odgoods.authservice.domain.auth.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import lombok.Data;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Data
public class CustomUserDetails implements UserDetails {

    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(user.getRole().name()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    public User getUser() {
        return user;
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // can be customized based on `user` fields
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // can be customized based on `user` fields
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // can be customized based on `user` fields
    }

    @Override
    public boolean isEnabled() {
        return true; // can be customized based on `user` fields
    }

}
