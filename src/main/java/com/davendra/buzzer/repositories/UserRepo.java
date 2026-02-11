package com.davendra.buzzer.repositories;

import com.davendra.buzzer.entity.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<UserModel, Long> {
    Optional<UserModel> findByEmail(String email);

    Optional<UserModel> findByUserName(String username);

    Optional<UserModel> findByPhone(String phone);

    @Query("SELECT u FROM UserModel u WHERE u.firstName LIKE %:query% OR u.email LIKE %:query% or u.lastName LIKE %:query%")
    Page<UserModel> searchByKeyword(String query, Pageable pageable);

    @Query("SELECT u FROM UserModel u " +
            "ORDER BY " +
            "CASE WHEN :sortBy = 'followers' THEN SIZE(u.followers) ELSE 0 END DESC, " +
            "CASE WHEN :sortBy = 'posts' THEN SIZE(u.posts) ELSE 0 END DESC")
    Page<UserModel> findUsersSorted(@Param("sortBy") String sortBy, Pageable pageable);


}


//            "CASE WHEN :sortBy = 'oldest' THEN u.createdAt END ASC, " +
//                    "CASE WHEN :sortBy = 'latest' THEN u.createdAt END DESC"

//match a specific query from a column