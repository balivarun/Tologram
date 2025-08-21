package com.tologram.dto;

import com.tologram.model.Post;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
public class PostDto {
    private String id;
    private UserDto user;
    private String imageUrl;
    private String videoUrl;
    private String caption;
    private List<String> hashtags;
    private int likesCount;
    private int commentsCount;
    private boolean liked;
    private Instant createdAt;
    private Instant updatedAt;
    
    public static PostDto fromPost(Post post, UserDto user, boolean isLiked) {
        PostDto dto = new PostDto();
        dto.setId(post.getId());
        dto.setUser(user);
        dto.setImageUrl(post.getImageUrl());
        dto.setVideoUrl(post.getVideoUrl());
        dto.setCaption(post.getCaption());
        dto.setHashtags(post.getHashtags());
        dto.setLikesCount(post.getLikesCount());
        dto.setCommentsCount(post.getCommentsCount());
        dto.setLiked(isLiked);
        dto.setCreatedAt(post.getCreatedAt());
        dto.setUpdatedAt(post.getUpdatedAt());
        return dto;
    }
}