package com.sparta.trillionnewspeedproject.repository;

import com.sparta.trillionnewspeedproject.entity.Comment;
import com.sparta.trillionnewspeedproject.entity.CommentLike;
import com.sparta.trillionnewspeedproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
	Optional<CommentLike> findByUserAndComment(User user, Comment comment);
}
