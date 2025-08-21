package com.tologram.service;

import com.tologram.dto.*;
import com.tologram.exception.AuthenticationException;
import com.tologram.exception.UserAlreadyExistsException;
import com.tologram.model.User;
import com.tologram.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(RegisterRequest request, HttpServletRequest httpRequest) {
        log.debug("Registering new user: {}", request.getUsername());

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("Username is already taken");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email is already registered");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        
        User savedUser = userRepository.save(user);
        
        // Authenticate the user and create session
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            savedUser, null, savedUser.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        // Store authentication in session
        HttpSession session = httpRequest.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, 
                            SecurityContextHolder.getContext());
        
        log.info("User registered successfully: {}", savedUser.getUsername());
        return new AuthResponse("session", UserDto.fromUser(savedUser));
    }

    public AuthResponse login(AuthRequest request, HttpServletRequest httpRequest) {
        log.debug("Authenticating user: {}", request.getUsername());

        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // Store authentication in session
            HttpSession session = httpRequest.getSession(true);
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, 
                                SecurityContextHolder.getContext());

            User user = userRepository.findByUsernameOrEmail(request.getUsername(), request.getUsername())
                .orElseThrow(() -> new AuthenticationException("User not found"));

            log.info("User authenticated successfully: {}", user.getUsername());
            return new AuthResponse("session", UserDto.fromUser(user));

        } catch (Exception e) {
            log.warn("Authentication failed for user: {}", request.getUsername());
            throw new AuthenticationException("Invalid username or password");
        }
    }
    
    public void logout(HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();
        log.info("User logged out successfully");
    }
}