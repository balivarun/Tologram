package com.tologram.dto;

import com.tologram.model.Comment;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
public class CommentDto {
    private String id;
    private String postId;
    private UserDto user;
    private String text;
    private String parentCommentId;
    private Instant createdAt;
    private Instant updatedAt;
    
    public static CommentDto fromComment(Comment comment, UserDto user) {
        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setPostId(comment.getPostId());
        dto.setUser(user);
        dto.setText(comment.getText());
        dto.setParentCommentId(comment.getParentCommentId());
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setUpdatedAt(comment.getUpdatedAt());
        return dto;
    }
}