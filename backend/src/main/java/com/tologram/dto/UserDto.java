package com.tologram.dto;

import com.tologram.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
public class UserDto {
    private String id;
    private String username;
    private String email;
    private String profilePictureUrl;
    private String bio;
    private int followersCount;
    private int followingCount;
    private int postsCount;
    private boolean isFollowing;
    private Instant createdAt;
    
    public static UserDto fromUser(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setProfilePictureUrl(user.getProfilePictureUrl());
        dto.setBio(user.getBio());
        dto.setFollowersCount(user.getFollowersCount());
        dto.setFollowingCount(user.getFollowingCount());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }
    
    public static UserDto fromUser(User user, boolean isFollowing, int postsCount) {
        UserDto dto = fromUser(user);
        dto.setFollowing(isFollowing);
        dto.setPostsCount(postsCount);
        return dto;
    }
}