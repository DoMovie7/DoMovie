package com.red.domovie.domain.repository.hometheater;

import com.red.domovie.domain.entity.hometheater.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findByHomeTheaterId(Long homeTheaterId);
}
