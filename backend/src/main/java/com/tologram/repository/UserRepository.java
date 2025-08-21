package com.tologram.repository;

import com.tologram.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
    
    Optional<User> findByUsernameOrEmail(String username, String email);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
    
    @Query("{'username': {'$regex': ?0, '$options': 'i'}}")
    List<User> findByUsernameContainingIgnoreCase(String username);
    
    @Query("{'$or': [" +
           "{'username': {'$regex': ?0, '$options': 'i'}}, " +
           "{'email': {'$regex': ?0, '$options': 'i'}}" +
           "]}")
    List<User> searchByUsernameOrEmail(String searchTerm);
}