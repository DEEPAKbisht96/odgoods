package com.odgoods.authservice.domain.auth.repository;

import java.util.Optional;

import com.odgoods.authservice.domain.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);
}
