package com.example.snsproject.repository;

import com.example.snsproject.model.entity.LikeEntity;
import com.example.snsproject.model.entity.PostEntity;
import com.example.snsproject.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface LikeEntityRepository extends JpaRepository<LikeEntity, Long> {
    Optional<LikeEntity> findByUserAndPost(UserEntity user, PostEntity post);

    int countByPost(PostEntity post);

    @Transactional
    @Modifying
    @Query("UPDATE LikeEntity entity SET deleted_at = NOW() where entity.post = :post")
    void deleteAllByPost(@Param("post") PostEntity postEntity);
}
