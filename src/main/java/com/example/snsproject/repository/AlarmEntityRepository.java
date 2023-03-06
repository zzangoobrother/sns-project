package com.example.snsproject.repository;

import com.example.snsproject.model.entity.AlarmEntity;
import com.example.snsproject.model.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmEntityRepository extends JpaRepository<AlarmEntity, Long> {
    Page<AlarmEntity> findAllByUser(UserEntity user, Pageable pageable);
}
