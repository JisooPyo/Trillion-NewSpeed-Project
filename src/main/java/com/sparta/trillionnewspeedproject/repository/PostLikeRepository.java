package com.sparta.trillionnewspeedproject.repository;

import com.sparta.trillionnewspeedproject.entity.Post;
import com.sparta.trillionnewspeedproject.entity.PostLike;
import com.sparta.trillionnewspeedproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostLikeRepository extends JpaRepository <PostLike, Long> {
    Optional<PostLike> findByUserAndPost(User user, Post post);
}
