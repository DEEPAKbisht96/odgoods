package com.odgoods.authservice.domain.auth.dto;

import com.odgoods.authservice.domain.auth.model.CountryCode;
import com.odgoods.authservice.domain.auth.model.ProductCategory;

import java.util.List;

public record ProfileResponse(
        Long id,
        CountryCode countryCode,
        String companyName,
        String companyAddress,
        String companyDescription,
        String companyLogoUrl,
        String companyWebsite,
        List<String> companySocialMedia,
        ProductCategory companyCategory
) {
}
