package com.odgoods.authservice.domain.auth.service;

import com.odgoods.authservice.domain.auth.dto.ProfileRequest;
import com.odgoods.authservice.domain.auth.dto.ProfileResponse;
import com.odgoods.authservice.domain.auth.entity.MerchantProfile;
import com.odgoods.authservice.domain.auth.entity.User;
import com.odgoods.authservice.domain.auth.mapper.ProfileMapper;
import com.odgoods.authservice.domain.auth.repository.ProfileRepository;
import com.odgoods.authservice.domain.auth.repository.UserRepository;
import org.springframework.stereotype.Service;


@Service
public class ProfileServiceImpl implements ProfileService{

    private final UserRepository userRepository;
    private final ProfileMapper profileMapper = new ProfileMapper();
    private final ProfileRepository profileRepository;

    public ProfileServiceImpl(UserRepository userRepository, ProfileRepository profileRepository) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
    }

    @Override
    public ProfileResponse createProfile(ProfileRequest profileRequest, Long userId) {


        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        MerchantProfile merchantProfile = profileMapper.toEntity(profileRequest);
        merchantProfile.setUser(user);

        MerchantProfile savedMerchantProfile = profileRepository.save(merchantProfile);

        return profileMapper.toResponse(savedMerchantProfile);
    }

    @Override
    public ProfileResponse getProfile(Long userId) {

        MerchantProfile merchantProfile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        return profileMapper.toResponse(merchantProfile);
    }


}
