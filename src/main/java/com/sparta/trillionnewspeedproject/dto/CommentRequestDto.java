package com.sparta.trillionnewspeedproject.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CommentRequestDto {
	@NotBlank
	private String commentContents;
}
