package com.sparta.trillionnewspeedproject.repository;

import com.sparta.trillionnewspeedproject.entity.Comment;
import com.sparta.trillionnewspeedproject.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
	List<Comment> findAllByPostOrderByCreatedAtDesc(Post post);
}
