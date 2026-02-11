package com.davendra.buzzer.repositories;

import com.davendra.buzzer.entity.CommentModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepo extends JpaRepository<CommentModel, Long> {
    public Page<CommentModel> findByCommentedById(Long commentById, Pageable pageable);
    public Page<CommentModel> findByPostId(Long postId, Pageable pageable);
}
