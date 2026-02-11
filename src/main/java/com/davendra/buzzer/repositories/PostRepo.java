package com.davendra.buzzer.repositories;

import com.davendra.buzzer.entity.PostModel;
import com.davendra.buzzer.entity.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepo extends JpaRepository<PostModel, Long> {
    Page<PostModel> findByUserId(Long userId, Pageable pageable);

    Page<PostModel> findBySavedBy(UserModel userId, Pageable pageable);

}
