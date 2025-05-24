package com.odgoods.authservice.domain.auth.dto;

import com.odgoods.authservice.domain.auth.model.CountryCode;
import com.odgoods.authservice.domain.auth.model.ProductCategory;
import com.odgoods.authservice.domain.auth.validation.OnCreate;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileRequest {

    @Enumerated(EnumType.STRING)
    @Column
    private CountryCode countryCode;

    @Column
    @Pattern(
            regexp = "\\+?[0-9\\-]{7,15}",
            message = "Phone number must be valid and contain 7 to 15 digits"
    )
    private String phoneNumber;

    @Column(nullable = false)
    @NotBlank(groups = OnCreate.class, message = "Contact email is required")
    @Email(message = "Please enter a valid email address")
    private String contactEmail;

    @Column
    @NotBlank(groups = OnCreate.class, message = "Company name is required")
    @Size(max = 100, message = "Company name must be less than 100 characters")
    private String companyName;

    @Column
    @NotBlank(groups = OnCreate.class, message = "Company address is required")
    @Size(max = 255, message = "Company address must be less than 255 characters")
    private String companyAddress;

    @Column
    @Size(max = 255, message = "Company website must be less than 255 characters")
    private String companyWebsite;

    @Column
    @Size(max = 255, message = "Company logo URL must be less than 255 characters")
    private String companyLogoUrl;

    @Column(length = 1000)
    @Size(max = 1000, message = "Company description must be less than 1000 characters")
    private String companyDescription;

    @ElementCollection
    @CollectionTable(name = "profile_social_links", joinColumns = @JoinColumn(name = "profile_id"))
    @Column(name = "social_link")
    private List<@Size(max = 255, message = "Each social media link must be less than 255 characters") String> companySocialMedia;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(groups = OnCreate.class, message = "Company category is required")
    private ProductCategory companyCategory;
}
