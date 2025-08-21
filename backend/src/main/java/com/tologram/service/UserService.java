package com.tologram.service;

import com.tologram.dto.UserDto;
import com.tologram.exception.ResourceNotFoundException;
import com.tologram.exception.UserAlreadyExistsException;
import com.tologram.model.User;
import com.tologram.repository.PostRepository;
import com.tologram.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final FileStorageService fileStorageService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsernameOrEmail(username, username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    public UserDto getUserById(String userId, String currentUserId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        boolean isFollowing = currentUserId != null && user.getFollowers().contains(currentUserId);
        int postsCount = (int) postRepository.countByUserId(userId);
        
        return UserDto.fromUser(user, isFollowing, postsCount);
    }

    public UserDto getCurrentUser(String userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        int postsCount = (int) postRepository.countByUserId(userId);
        return UserDto.fromUser(user, false, postsCount);
    }

    @Transactional
    public UserDto updateProfile(String userId, UserDto updateRequest, MultipartFile profilePicture) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (updateRequest.getUsername() != null && !updateRequest.getUsername().equals(user.getUsername())) {
            if (userRepository.existsByUsername(updateRequest.getUsername())) {
                throw new UserAlreadyExistsException("Username is already taken");
            }
            user.setUsername(updateRequest.getUsername());
        }

        if (updateRequest.getEmail() != null && !updateRequest.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(updateRequest.getEmail())) {
                throw new UserAlreadyExistsException("Email is already registered");
            }
            user.setEmail(updateRequest.getEmail());
        }

        if (updateRequest.getBio() != null) {
            user.setBio(updateRequest.getBio());
        }

        if (profilePicture != null && !profilePicture.isEmpty()) {
            String profilePictureUrl = fileStorageService.storeFile(profilePicture);
            user.setProfilePictureUrl(profilePictureUrl);
        }

        User savedUser = userRepository.save(user);
        int postsCount = (int) postRepository.countByUserId(userId);
        
        log.info("User profile updated: {}", savedUser.getUsername());
        return UserDto.fromUser(savedUser, false, postsCount);
    }

    @Transactional
    public void followUser(String followerId, String followeeId) {
        if (followerId.equals(followeeId)) {
            throw new IllegalArgumentException("Cannot follow yourself");
        }

        User follower = userRepository.findById(followerId)
            .orElseThrow(() -> new ResourceNotFoundException("Follower not found"));
        
        User followee = userRepository.findById(followeeId)
            .orElseThrow(() -> new ResourceNotFoundException("User to follow not found"));

        if (!follower.getFollowing().contains(followeeId)) {
            follower.getFollowing().add(followeeId);
            followee.getFollowers().add(followerId);
            
            userRepository.save(follower);
            userRepository.save(followee);
            
            log.info("User {} followed user {}", follower.getUsername(), followee.getUsername());
        }
    }

    @Transactional
    public void unfollowUser(String followerId, String followeeId) {
        User follower = userRepository.findById(followerId)
            .orElseThrow(() -> new ResourceNotFoundException("Follower not found"));
        
        User followee = userRepository.findById(followeeId)
            .orElseThrow(() -> new ResourceNotFoundException("User to unfollow not found"));

        follower.getFollowing().remove(followeeId);
        followee.getFollowers().remove(followerId);
        
        userRepository.save(follower);
        userRepository.save(followee);
        
        log.info("User {} unfollowed user {}", follower.getUsername(), followee.getUsername());
    }

    public List<UserDto> searchUsers(String query, String currentUserId) {
        List<User> users = userRepository.findByUsernameContainingIgnoreCase(query);
        
        return users.stream()
            .map(user -> {
                boolean isFollowing = currentUserId != null && user.getFollowers().contains(currentUserId);
                int postsCount = (int) postRepository.countByUserId(user.getId());
                return UserDto.fromUser(user, isFollowing, postsCount);
            })
            .collect(Collectors.toList());
    }
}