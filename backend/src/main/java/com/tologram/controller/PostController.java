package com.tologram.controller;

import com.tologram.dto.PostDto;
import com.tologram.model.User;
import com.tologram.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Posts", description = "Post management APIs")
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @Operation(summary = "Get user feed")
    @GetMapping("/feed")
    public ResponseEntity<Page<PostDto>> getFeed(@AuthenticationPrincipal User user,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PostDto> feed = postService.getFeed(user.getId(), pageable);
        return ResponseEntity.ok(feed);
    }

    @Operation(summary = "Get post by ID")
    @GetMapping("/{postId}")
    public ResponseEntity<PostDto> getPost(@PathVariable String postId,
                                           @AuthenticationPrincipal User user) {
        PostDto post = postService.getPostById(postId, user != null ? user.getId() : null);
        return ResponseEntity.ok(post);
    }

    @Operation(summary = "Get user posts")
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<PostDto>> getUserPosts(@PathVariable String userId,
                                                      @AuthenticationPrincipal User currentUser,
                                                      @RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PostDto> posts = postService.getUserPosts(userId, currentUser != null ? currentUser.getId() : null, pageable);
        return ResponseEntity.ok(posts);
    }

    @Operation(summary = "Create a new post")
    @PostMapping
    public ResponseEntity<PostDto> createPost(@AuthenticationPrincipal User user,
                                              @RequestParam(required = false) String caption,
                                              @RequestParam("file") MultipartFile file) {
        PostDto post = postService.createPost(user.getId(), caption, file);
        return ResponseEntity.ok(post);
    }

    @Operation(summary = "Update post")
    @PutMapping("/{postId}")
    public ResponseEntity<PostDto> updatePost(@PathVariable String postId,
                                              @AuthenticationPrincipal User user,
                                              @RequestParam String caption) {
        PostDto post = postService.updatePost(postId, user.getId(), caption);
        return ResponseEntity.ok(post);
    }

    @Operation(summary = "Delete post")
    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable String postId,
                                        @AuthenticationPrincipal User user) {
        postService.deletePost(postId, user.getId());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Like post")
    @PostMapping("/{postId}/like")
    public ResponseEntity<?> likePost(@PathVariable String postId,
                                      @AuthenticationPrincipal User user) {
        postService.likePost(postId, user.getId());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Unlike post")
    @DeleteMapping("/{postId}/like")
    public ResponseEntity<?> unlikePost(@PathVariable String postId,
                                        @AuthenticationPrincipal User user) {
        postService.unlikePost(postId, user.getId());
        return ResponseEntity.ok().build();
    }
}