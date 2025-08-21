package com.tologram.service;

import com.tologram.dto.PostDto;
import com.tologram.dto.UserDto;
import com.tologram.exception.ResourceNotFoundException;
import com.tologram.model.Post;
import com.tologram.model.User;
import com.tologram.repository.PostRepository;
import com.tologram.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;
    private static final Pattern HASHTAG_PATTERN = Pattern.compile("#\\w+");

    public Page<PostDto> getFeed(String userId, Pageable pageable) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<String> followingIds = new ArrayList<>(user.getFollowing());
        followingIds.add(userId); // Include user's own posts

        Page<Post> posts = postRepository.findByUserIdInOrderByCreatedAtDesc(followingIds, pageable);
        
        return posts.map(post -> {
            User postUser = userRepository.findById(post.getUserId()).orElse(null);
            UserDto userDto = postUser != null ? UserDto.fromUser(postUser) : null;
            boolean isLiked = post.isLikedBy(userId);
            return PostDto.fromPost(post, userDto, isLiked);
        });
    }

    public PostDto getPostById(String postId, String userId) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        
        User postUser = userRepository.findById(post.getUserId())
            .orElseThrow(() -> new ResourceNotFoundException("Post user not found"));
        
        UserDto userDto = UserDto.fromUser(postUser);
        boolean isLiked = userId != null && post.isLikedBy(userId);
        
        return PostDto.fromPost(post, userDto, isLiked);
    }

    public Page<PostDto> getUserPosts(String userId, String currentUserId, Pageable pageable) {
        Page<Post> posts = postRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
        
        User postUser = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        UserDto userDto = UserDto.fromUser(postUser);
        
        return posts.map(post -> {
            boolean isLiked = currentUserId != null && post.isLikedBy(currentUserId);
            return PostDto.fromPost(post, userDto, isLiked);
        });
    }

    @Transactional
    public PostDto createPost(String userId, String caption, MultipartFile file) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String fileUrl = null;
        if (file != null && !file.isEmpty()) {
            fileUrl = fileStorageService.storeFile(file);
        }

        Post post = new Post();
        post.setUserId(userId);
        post.setCaption(caption);
        post.setImageUrl(fileUrl);
        post.setHashtags(extractHashtags(caption));

        Post savedPost = postRepository.save(post);
        UserDto userDto = UserDto.fromUser(user);
        
        log.info("Post created by user: {}", user.getUsername());
        return PostDto.fromPost(savedPost, userDto, false);
    }

    @Transactional
    public PostDto updatePost(String postId, String userId, String caption) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        if (!post.getUserId().equals(userId)) {
            throw new IllegalArgumentException("You can only edit your own posts");
        }

        post.setCaption(caption);
        post.setHashtags(extractHashtags(caption));

        Post savedPost = postRepository.save(post);
        
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        UserDto userDto = UserDto.fromUser(user);
        
        log.info("Post updated: {}", postId);
        return PostDto.fromPost(savedPost, userDto, false);
    }

    @Transactional
    public void deletePost(String postId, String userId) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        if (!post.getUserId().equals(userId)) {
            throw new IllegalArgumentException("You can only delete your own posts");
        }

        postRepository.delete(post);
        log.info("Post deleted: {}", postId);
    }

    @Transactional
    public void likePost(String postId, String userId) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        post.addLike(userId);
        postRepository.save(post);
        
        log.debug("Post liked: {} by user: {}", postId, userId);
    }

    @Transactional
    public void unlikePost(String postId, String userId) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        post.removeLike(userId);
        postRepository.save(post);
        
        log.debug("Post unliked: {} by user: {}", postId, userId);
    }

    private List<String> extractHashtags(String text) {
        List<String> hashtags = new ArrayList<>();
        if (text != null) {
            Matcher matcher = HASHTAG_PATTERN.matcher(text);
            while (matcher.find()) {
                hashtags.add(matcher.group().toLowerCase());
            }
        }
        return hashtags;
    }
}