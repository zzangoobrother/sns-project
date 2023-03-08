package com.example.snsproject.repository;

import com.example.snsproject.model.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostEntityRepository extends JpaRepository<PostEntity, Long> {
    Page<PostEntity> findAllByUserId(Long userId, Pageable pageable);
}
