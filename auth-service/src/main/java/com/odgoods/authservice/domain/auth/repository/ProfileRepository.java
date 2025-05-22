package com.odgoods.authservice.domain.auth.repository;

import com.odgoods.authservice.domain.auth.entity.MerchantProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ProfileRepository extends JpaRepository<MerchantProfile, Long> {

    Optional<MerchantProfile> findByUserId(Long userId);
}
