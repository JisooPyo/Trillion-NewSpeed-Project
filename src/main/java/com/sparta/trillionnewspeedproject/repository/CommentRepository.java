package com.sparta.trillionnewspeedproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.trillionnewspeedproject.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}