package com.sparta.trillionnewspeedproject.dto;

import com.sparta.trillionnewspeedproject.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {
	private Long id;
	private String username;
	private LocalDateTime createdAt;
	private String comment;
	private int likes;

	public CommentResponseDto(Comment comment) {
		this.id = comment.getId();
		this.username = comment.getUser().getUsername();
		this.createdAt = comment.getCreatedAt();
		this.comment = comment.getComment();
		this.likes = comment.getLikes();
	}
}
