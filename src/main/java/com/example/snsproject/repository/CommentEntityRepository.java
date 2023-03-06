package com.example.snsproject.repository;

import com.example.snsproject.model.entity.CommentEntity;
import com.example.snsproject.model.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentEntityRepository extends JpaRepository<CommentEntity, Long> {
    Page<CommentEntity> findAllByPost(PostEntity post, Pageable pageable);
}
