package com.odgoods.authservice.domain.auth.controller;

import com.odgoods.authservice.common.exception.StatusBasedException.ConflictException;
import com.odgoods.authservice.domain.auth.dto.ProfileRequest;
import com.odgoods.authservice.domain.auth.dto.ProfileResponse;
import com.odgoods.authservice.domain.auth.service.ProfileService;
import com.odgoods.authservice.domain.auth.validation.OnCreate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/profile")
@Tag(name = "Profile Management", description = "Manage user profile creation, retrieval, and updates")
@SecurityRequirement(name = "bearerAuth")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @Operation(
            summary = "Create a new profile",
            description = "Creates a profile for the authenticated user. Fails if a profile already exists."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Profile created successfully",
                    content = @Content(schema = @Schema(implementation = ProfileResponse.class))),
            @ApiResponse(responseCode = "409", description = "Profile already exists for the user"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<ProfileResponse> createProfile(
            @Parameter(description = "Authenticated user's ID", required = true, example = "123")
            @RequestHeader("X-User-Id") String userId,
            @Validated(OnCreate.class) @RequestBody ProfileRequest profileRequest) {

        if (profileService.getProfile(Long.parseLong(userId)) != null) {
            throw new ConflictException("profile already exists for user please update");
        }

        ProfileResponse profileResponse = profileService.createProfile(profileRequest, Long.parseLong(userId));

        return ResponseEntity.status(HttpStatus.CREATED).body(profileResponse);
    }

    @Operation(
            summary = "Get user profile",
            description = "Retrieves the profile of the authenticated user."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ProfileResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden - ID mismatch"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{user_id}")
    public ResponseEntity<ProfileResponse> getProfile(
            @Parameter(description = "Authenticated user's ID", required = true, example = "123")
            @RequestHeader("X-User-Id") String userIdHeader,
            @Parameter(description = "User ID from path", required = true, example = "123")
            @PathVariable("user_id") String userIdPath
    ) {
        try {
            Long headerId = Long.parseLong(userIdHeader);
            Long pathId = Long.parseLong(userIdPath);

            if (!headerId.equals(pathId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }

            ProfileResponse profileResponse = profileService.getProfile(headerId);
            return ResponseEntity.ok(profileResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(
            summary = "Update user profile",
            description = "Updates the authenticated user's profile. Only allowed if path and header IDs match."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile updated successfully",
                    content = @Content(schema = @Schema(implementation = ProfileResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden - ID mismatch"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{user_id}")
    public ResponseEntity<ProfileResponse> updateProfile(
            @Parameter(description = "Authenticated user's ID", required = true, example = "123")
            @RequestHeader("X-User-Id") String userIdHeader,
            @Parameter(description = "User ID from path", required = true, example = "123")
            @PathVariable("user_id") String userIdPath,
            @Validated @RequestBody ProfileRequest profileRequest
    ) {
        try {
            Long headerId = Long.parseLong(userIdHeader);
            Long pathId = Long.parseLong(userIdPath);

            if (!headerId.equals(pathId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }

            ProfileResponse updatedProfile = profileService.updateProfile(profileRequest, headerId);
            return ResponseEntity.ok(updatedProfile);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
