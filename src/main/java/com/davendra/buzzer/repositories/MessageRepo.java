package com.davendra.buzzer.repositories;

import com.davendra.buzzer.entity.MessageModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepo extends JpaRepository<MessageModel, Long> {
    public Page<MessageModel> findByChatId(Long chatId, Pageable pageable);
}
