package com.davendra.buzzer.repositories;

import com.davendra.buzzer.entity.ReelModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReelsRepo extends JpaRepository<ReelModel,Long> {
    public List<ReelModel> findByUserId(Long userId);
}
