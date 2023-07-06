package com.sparta.trillionnewspeedproject.dto;

import com.sparta.trillionnewspeedproject.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto extends ApiResponseDto {
   	private Long commentId;
	private String username;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;
	private String commentContents;
	private long likeCnt;

	public CommentResponseDto(Comment comment) {
		this.commentId = comment.getCommentId();
		this.username = comment.getUser().getUsername();
		this.createdAt = comment.getCreatedAt();
		this.modifiedAt = comment.getModifiedAt();
		this.commentContents = comment.getCommentContents();
		this.likeCnt = comment.getLikeCnt();
	}
}
