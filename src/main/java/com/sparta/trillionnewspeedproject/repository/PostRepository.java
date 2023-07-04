package com.sparta.trillionnewspeedproject.repository;

import com.sparta.trillionnewspeedproject.entity.Posts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Posts, Long> {
}
