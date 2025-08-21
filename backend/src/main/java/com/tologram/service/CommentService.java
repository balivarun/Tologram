package com.tologram.service;

import com.tologram.dto.CommentDto;
import com.tologram.dto.UserDto;
import com.tologram.exception.ResourceNotFoundException;
import com.tologram.model.Comment;
import com.tologram.model.Post;
import com.tologram.model.User;
import com.tologram.repository.CommentRepository;
import com.tologram.repository.PostRepository;
import com.tologram.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public List<CommentDto> getPostComments(String postId) {
        List<Comment> comments = commentRepository.findByPostIdOrderByCreatedAtAsc(postId);
        
        return comments.stream()
            .map(comment -> {
                User user = userRepository.findById(comment.getUserId()).orElse(null);
                UserDto userDto = user != null ? UserDto.fromUser(user) : null;
                return CommentDto.fromComment(comment, userDto);
            })
            .collect(Collectors.toList());
    }

    @Transactional
    public CommentDto addComment(String postId, String userId, String text) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Comment comment = new Comment();
        comment.setPostId(postId);
        comment.setUserId(userId);
        comment.setText(text);

        Comment savedComment = commentRepository.save(comment);
        
        // Update post comments count
        post.incrementCommentsCount();
        postRepository.save(post);
        
        UserDto userDto = UserDto.fromUser(user);
        
        log.info("Comment added to post: {} by user: {}", postId, user.getUsername());
        return CommentDto.fromComment(savedComment, userDto);
    }

    @Transactional
    public CommentDto updateComment(String commentId, String userId, String text) {
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

        if (!comment.getUserId().equals(userId)) {
            throw new IllegalArgumentException("You can only edit your own comments");
        }

        comment.setText(text);
        Comment savedComment = commentRepository.save(comment);
        
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        UserDto userDto = UserDto.fromUser(user);
        
        log.info("Comment updated: {}", commentId);
        return CommentDto.fromComment(savedComment, userDto);
    }

    @Transactional
    public void deleteComment(String commentId, String userId) {
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

        if (!comment.getUserId().equals(userId)) {
            throw new IllegalArgumentException("You can only delete your own comments");
        }

        // Update post comments count
        Post post = postRepository.findById(comment.getPostId())
            .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        
        post.decrementCommentsCount();
        postRepository.save(post);
        
        commentRepository.delete(comment);
        log.info("Comment deleted: {}", commentId);
    }
}