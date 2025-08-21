package com.tologram.controller;

import com.tologram.dto.CommentDto;
import com.tologram.model.User;
import com.tologram.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Comments", description = "Comment management APIs")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "Get post comments")
    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<CommentDto>> getPostComments(@PathVariable String postId) {
        List<CommentDto> comments = commentService.getPostComments(postId);
        return ResponseEntity.ok(comments);
    }

    @Operation(summary = "Add comment to post")
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentDto> addComment(@PathVariable String postId,
                                                 @RequestBody CommentRequest request,
                                                 @AuthenticationPrincipal User user) {
        CommentDto comment = commentService.addComment(postId, user.getId(), request.getText());
        return ResponseEntity.ok(comment);
    }

    @Operation(summary = "Update comment")
    @PutMapping("/comments/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable String commentId,
                                                    @RequestBody CommentRequest request,
                                                    @AuthenticationPrincipal User user) {
        CommentDto comment = commentService.updateComment(commentId, user.getId(), request.getText());
        return ResponseEntity.ok(comment);
    }

    @Operation(summary = "Delete comment")
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable String commentId,
                                           @AuthenticationPrincipal User user) {
        commentService.deleteComment(commentId, user.getId());
        return ResponseEntity.ok().build();
    }

    public static class CommentRequest {
        private String text;

        public String getText() { return text; }
        public void setText(String text) { this.text = text; }
    }
}