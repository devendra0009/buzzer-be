package com.davendra.buzzer.repositories;

import com.davendra.buzzer.entity.ChatModel;
import com.davendra.buzzer.entity.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepo extends JpaRepository<ChatModel, Long> {
    public Page<ChatModel> findByUsersId(Long userId, Pageable pageable);

    // use member of instead of in -> since *IN* checks if value in our entity present in the list you provided whereas *MEMBER OF* checks if value you provided present in some collection in our entity
    @Query("""
                SELECT c FROM ChatModel c 
                WHERE SIZE(c.users) = :#{#userIds.size()}
                AND (SELECT COUNT(u) FROM c.users u WHERE u.id IN :userIds) = :#{#userIds.size()}
            """)
    ChatModel findChatByExactUserIds(@Param("userIds") List<Long> userIds);

}