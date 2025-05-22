package com.odgoods.authservice.domain.auth.controller;


import com.odgoods.authservice.domain.auth.dto.ProfileRequest;
import com.odgoods.authservice.domain.auth.service.ProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @PostMapping
    public ResponseEntity<ProfileResponse> createProfile(
            @RequestHeader("X-User-Id") String userId,
            @Validated @RequestBody ProfileRequest profileRequest) {

        System.out.println("userId: " + userId);
        System.out.println("profileRequest: " + profileRequest);

        ProfileResponse profileResponse = profileService.createProfile(profileRequest, Long.parseLong(userId));

        return ResponseEntity.status(HttpStatus.OK).body(profileResponse);
    }


    @GetMapping("/{user_id}")
    public ResponseEntity<ProfileResponse> getProfile(
            @RequestHeader("X-User-Id") String userIdHeader,
            @PathVariable("user_id") String userIdPath
    ) {
        // Convert to Long (you can add try-catch for format safety)
        Long headerId = Long.parseLong(userIdHeader);
        Long pathId = Long.parseLong(userIdPath);

        // Check if they match
        if (!headerId.equals(pathId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        ProfileResponse profileResponse = profileService.getProfile(headerId);
        return ResponseEntity.ok(profileResponse);
    }

}
