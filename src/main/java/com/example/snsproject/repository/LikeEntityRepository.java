package com.example.snsproject.repository;

import com.example.snsproject.model.entity.LikeEntity;
import com.example.snsproject.model.entity.PostEntity;
import com.example.snsproject.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeEntityRepository extends JpaRepository<LikeEntity, Long> {
    Optional<LikeEntity> findByUserAndPost(UserEntity user, PostEntity post);

    int countByPost(PostEntity post);
}
