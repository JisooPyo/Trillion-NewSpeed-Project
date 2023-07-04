package com.sparta.trillionnewspeedproject.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
	USER_ONLY_ERROR(HttpStatus.BAD_REQUEST, "작성자만 삭제/수정할 수 있습니다."),
	USER_NOT_ERROR(HttpStatus.BAD_REQUEST, "작성자는 좋아요를 누를 수 없습니다."),
	NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, "주소가 잘못되어 해당 페이지를 찾을 수 없습니다."),

	USERID_EXIST_ERROR(HttpStatus.BAD_REQUEST, "이미 존재하는 ID입니다."),
	EMAIL_EXIST_ERROR(HttpStatus.BAD_REQUEST, "이미 존재하는 회원의 email입니다.");




	private final HttpStatus status;
	private final String message;


	public HttpStatus getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}
	ErrorCode(HttpStatus status, String message) {
		this.status = status;
		this.message = message;
	}
}
