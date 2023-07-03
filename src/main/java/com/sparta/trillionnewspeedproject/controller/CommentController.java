package com.sparta.trillionnewspeedproject.controller;

import com.sparta.trillionnewspeedproject.dto.CommentRequestDto;
import com.sparta.trillionnewspeedproject.dto.CommentResponseDto;
import com.sparta.trillionnewspeedproject.dto.ResponseMessageDto;
import com.sparta.trillionnewspeedproject.exception.ErrorCode;
import com.sparta.trillionnewspeedproject.exception.GlobalExceptionHandler;
import com.sparta.trillionnewspeedproject.security.UserDetailsImpl;
import com.sparta.trillionnewspeedproject.service.CommentService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class CommentController {
	private final CommentService commentService;
	private final GlobalExceptionHandler globalExceptionHandler;

	// 선택한 게시글에 대한 모든 댓글 조회
	@GetMapping("/{postId}/comments")
	public List<CommentResponseDto> getCommentsByPostId(@PathVariable Long postId) {
		return commentService.getCommentsByPostId(postId);
	}

	// 댓글 작성
	@PostMapping("/{postId}/comments")
	public CommentResponseDto createComment(@PathVariable Long postId, @RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
		return commentService.createComment(postId, requestDto, userDetails.getUser());
	}

	// 선택한 댓글 수정
	@PutMapping("/{postId}/comments/{commentId}")
	public Object updateComment(@PathVariable Long postId, @PathVariable Long commentId, @RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletResponse response) {
		CommentResponseDto responseDto = commentService.updateComment(postId, commentId, requestDto, userDetails.getUser(), response);

		// 다른 유저가 수정을 시도할 경우 오류 메시지 반환
		if (response.getStatus() == 400) {
			return globalExceptionHandler.badRequestException(ErrorCode.USER_ONLY_ERROR);

		// postId 받은 것과 comment DB에 저장된 postId가 다를 경우 오류 메시지 반환
		} else if (response.getStatus() == 404) {
			return globalExceptionHandler.badRequestException(ErrorCode.NOT_FOUND_ERROR);

		// 오류가 나지 않을 경우 해당 댓글 수정
		} else {
			return responseDto;
		}
	}

	// 선택한 댓글 삭제
	@DeleteMapping("/{postId}/comments/{commentId}")
	public Object deleteComment(@PathVariable Long postId, @PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletResponse response) {
		commentService.deleteComment(postId, commentId, userDetails.getUser(), response);

		// 다른 유저가 삭제를 시도할 경우 오류 메시지 반환
		if (response.getStatus() == 400) {
			return globalExceptionHandler.badRequestException(ErrorCode.USER_ONLY_ERROR);

		// postId 받은 것과 comment DB에 저장된 postId가 다를 경우 오류 메시지 반환
		} else if (response.getStatus() == 404) {
			return globalExceptionHandler.badRequestException(ErrorCode.NOT_FOUND_ERROR);

		// 오류가 나지 않을 경우 해당 댓글 삭제
		} else {
			return new ResponseMessageDto("해당 댓글의 삭제를 완료했습니다.", response.getStatus());
		}
	}

	// 선택한 댓글 좋아요
	@PutMapping("/{postId}/comments/{commentId}/likes")
	public Object commentLike(@PathVariable Long postId, @PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletResponse response) {
		CommentResponseDto responseDto = commentService.commentLike(postId, commentId, userDetails.getUser(), response);

		// 작성한 유저가 좋아요를 시도할 경우 오류 메시지 반환
		if (response.getStatus() == 400) {
			return globalExceptionHandler.badRequestException(ErrorCode.USER_NOT_ERROR);

		// postId 받은 것과 comment DB에 저장된 postId가 다를 경우 오류 메시지 반환
		} else if (response.getStatus() == 404) {
			return globalExceptionHandler.badRequestException(ErrorCode.NOT_FOUND_ERROR);

		// 오류가 나지 않을 경우 해당 댓글 좋아요 추가
		} else {
			return responseDto;
		}
	}
}
