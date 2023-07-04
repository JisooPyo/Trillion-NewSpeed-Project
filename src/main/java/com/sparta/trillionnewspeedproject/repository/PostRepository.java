package com.sparta.trillionnewspeedproject.repository;

import com.sparta.trillionnewspeedproject.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
