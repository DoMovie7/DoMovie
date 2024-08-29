package com.red.domovie.domain.repository.hometheater;

import com.red.domovie.domain.entity.hometheater.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByHomeTheaterId(Long homeTheaterId);
}
