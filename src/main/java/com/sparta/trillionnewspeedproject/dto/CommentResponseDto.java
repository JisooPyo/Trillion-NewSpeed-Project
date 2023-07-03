package com.sparta.trillionnewspeedproject.dto;

import com.sparta.trillionnewspeedproject.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {
	private Long id;
	private String username;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;
	private String contents;
	private int likes;

	public CommentResponseDto(Comment comment) {
		this.id = comment.getId();
		this.username = comment.getUser().getUsername();
		this.createdAt = comment.getCreatedAt();
		this.modifiedAt = comment.getModifiedAt();
		this.contents = comment.getContents();
		this.likes = comment.getLikes();
	}
}
