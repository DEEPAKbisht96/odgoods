package com.odgoods.authservice.domain.auth.entity;

import com.odgoods.authservice.domain.auth.model.CountryCode;
import com.odgoods.authservice.domain.auth.model.ProductCategory;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "profiles")
public class MerchantProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CountryCode countryCode;

    @Column(nullable = false)
    @Pattern(regexp = "\\+?[0-9\\-]{7,15}", message = "Invalid phone number format")
    private String phoneNumber;

    @Column(nullable = false)
    @NotBlank(message = "Contact email is required")
    @Email(message = "Invalid email format")
    private String contactEmail;

    @Column(nullable = false)
    @NotBlank(message = "Company name is required")
    @Size(max = 100)
    private String companyName;

    @Column(nullable = false)
    @NotBlank(message = "Company address is required")
    @Size(max = 255)
    private String companyAddress;

    @Column
    @Size(max = 255)
    private String companyWebsite;

    @Column
    private String companyLogoUrl;

    @Column(length = 1000)
    private String companyDescription;

    @ElementCollection
    @CollectionTable(name = "profile_social_links", joinColumns = @JoinColumn(name = "profile_id"))
    @Column(name = "social_link")
    private List<@Size(max = 255) String> companySocialMedia;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Company category is required")
    private ProductCategory companyCategory;
}

