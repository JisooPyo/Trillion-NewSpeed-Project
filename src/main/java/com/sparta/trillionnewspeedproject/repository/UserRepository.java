package com.sparta.trillionnewspeedproject.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.trillionnewspeedproject.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    // 단건을 조회할때는 Optional
}