package com.tologram.controller;

import com.tologram.dto.UserDto;
import com.tologram.model.User;
import com.tologram.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Users", description = "User management APIs")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "Get current user profile")
    @GetMapping("/profile")
    public ResponseEntity<UserDto> getCurrentUser(@AuthenticationPrincipal User user) {
        UserDto userDto = userService.getCurrentUser(user.getId());
        return ResponseEntity.ok(userDto);
    }

    @Operation(summary = "Get user by ID")
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable String userId, 
                                               @AuthenticationPrincipal User currentUser) {
        UserDto userDto = userService.getUserById(userId, currentUser != null ? currentUser.getId() : null);
        return ResponseEntity.ok(userDto);
    }

    @Operation(summary = "Update user profile")
    @PutMapping("/profile")
    public ResponseEntity<UserDto> updateProfile(@AuthenticationPrincipal User user,
                                                @RequestPart(value = "user", required = false) UserDto updateRequest,
                                                @RequestPart(value = "profilePicture", required = false) MultipartFile profilePicture) {
        UserDto updatedUser = userService.updateProfile(user.getId(), updateRequest, profilePicture);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "Follow a user")
    @PostMapping("/{userId}/follow")
    public ResponseEntity<?> followUser(@PathVariable String userId, 
                                        @AuthenticationPrincipal User currentUser) {
        userService.followUser(currentUser.getId(), userId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Unfollow a user")
    @DeleteMapping("/{userId}/follow")
    public ResponseEntity<?> unfollowUser(@PathVariable String userId, 
                                          @AuthenticationPrincipal User currentUser) {
        userService.unfollowUser(currentUser.getId(), userId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Search users")
    @GetMapping("/search")
    public ResponseEntity<List<UserDto>> searchUsers(@RequestParam String q, 
                                                     @AuthenticationPrincipal User currentUser) {
        List<UserDto> users = userService.searchUsers(q, currentUser != null ? currentUser.getId() : null);
        return ResponseEntity.ok(users);
    }
}