package com.odgoods.authservice.domain.auth.service;

import com.odgoods.authservice.domain.auth.dto.ProfileRequest;
import com.odgoods.authservice.domain.auth.dto.ProfileResponse;

public interface ProfileService {

    ProfileResponse createProfile(ProfileRequest profileRequest, Long userId);

    ProfileResponse getProfile(Long userId);
}
