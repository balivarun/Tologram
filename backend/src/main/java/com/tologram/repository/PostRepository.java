package com.tologram.repository;

import com.tologram.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends MongoRepository<Post, String> {
    
    Page<Post> findByUserIdOrderByCreatedAtDesc(String userId, Pageable pageable);
    
    @Query("{'userId': {'$in': ?0}}")
    Page<Post> findByUserIdInOrderByCreatedAtDesc(List<String> userIds, Pageable pageable);
    
    List<Post> findByUserIdOrderByCreatedAtDesc(String userId);
    
    @Query("{'hashtags': {'$in': ?0}}")
    Page<Post> findByHashtagsInOrderByCreatedAtDesc(List<String> hashtags, Pageable pageable);
    
    @Query("{'caption': {'$regex': ?0, '$options': 'i'}}")
    Page<Post> findByCaptionContainingIgnoreCaseOrderByCreatedAtDesc(String caption, Pageable pageable);
    
    long countByUserId(String userId);
    
    @Query(value = "{}", sort = "{'createdAt': -1}")
    Page<Post> findAllOrderByCreatedAtDesc(Pageable pageable);
}