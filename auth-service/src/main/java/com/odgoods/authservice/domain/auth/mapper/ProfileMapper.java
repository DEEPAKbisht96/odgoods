package com.odgoods.authservice.domain.auth.mapper;


import com.odgoods.authservice.domain.auth.dto.ProfileRequest;
import com.odgoods.authservice.domain.auth.dto.ProfileResponse;
import com.odgoods.authservice.domain.auth.entity.MerchantProfile;
import org.springframework.stereotype.Component;

@Component
public class ProfileMapper {

    public MerchantProfile toEntity(ProfileRequest profileRequest){

        MerchantProfile merchantProfile = new MerchantProfile();
        merchantProfile.setCompanyAddress(profileRequest.getCompanyAddress());
        merchantProfile.setCompanyCategory(profileRequest.getCompanyCategory());
        merchantProfile.setCompanyDescription(profileRequest.getCompanyDescription());
        merchantProfile.setCompanyName(profileRequest.getCompanyName());
        merchantProfile.setCompanyLogoUrl(profileRequest.getCompanyLogoUrl());
        merchantProfile.setCompanyWebsite(profileRequest.getCompanyWebsite());
        merchantProfile.setCountryCode(profileRequest.getCountryCode());
        merchantProfile.setContactEmail(profileRequest.getContactEmail());
        merchantProfile.setPhoneNumber(profileRequest.getPhoneNumber());
        merchantProfile.setCompanySocialMedia(profileRequest.getCompanySocialMedia());

        return merchantProfile;

    }

    public ProfileResponse toResponse(MerchantProfile merchantProfile){
        return new ProfileResponse(
                merchantProfile.getId(),
                merchantProfile.getCountryCode(),
                merchantProfile.getCompanyName(),
                merchantProfile.getCompanyAddress(),
                merchantProfile.getCompanyDescription(),
                merchantProfile.getCompanyLogoUrl(),
                merchantProfile.getCompanyWebsite(),
                merchantProfile.getCompanySocialMedia(),
                merchantProfile.getCompanyCategory()
        );
    }

    public MerchantProfile updateEntity(MerchantProfile merchantProfile, ProfileRequest profileRequest){
        if(profileRequest.getCompanyAddress() != null) merchantProfile.setCompanyAddress(profileRequest.getCompanyAddress());
        if(profileRequest.getCompanyCategory() != null) merchantProfile.setCompanyCategory(profileRequest.getCompanyCategory());
        if(profileRequest.getCompanyDescription() != null) merchantProfile.setCompanyDescription(profileRequest.getCompanyDescription());
        if(profileRequest.getCompanyName() != null) merchantProfile.setCompanyName(profileRequest.getCompanyName());
        if(profileRequest.getCompanyLogoUrl() != null) merchantProfile.setCompanyLogoUrl(profileRequest.getCompanyLogoUrl());
        if(profileRequest.getCompanyWebsite() != null) merchantProfile.setCompanyWebsite(profileRequest.getCompanyWebsite());
        if(profileRequest.getCountryCode() != null) merchantProfile.setCountryCode(profileRequest.getCountryCode());
        if(profileRequest.getContactEmail() != null) merchantProfile.setContactEmail(profileRequest.getContactEmail());
        if(profileRequest.getPhoneNumber() != null) merchantProfile.setPhoneNumber(profileRequest.getPhoneNumber());
        if(profileRequest.getCompanySocialMedia() != null) merchantProfile.setCompanySocialMedia(profileRequest.getCompanySocialMedia());

        return merchantProfile;
    }

}
