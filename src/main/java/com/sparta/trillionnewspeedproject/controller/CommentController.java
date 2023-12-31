package com.sparta.trillionnewspeedproject.controller;

import com.sparta.trillionnewspeedproject.dto.ApiResponseDto;
import com.sparta.trillionnewspeedproject.dto.CommentRequestDto;
import com.sparta.trillionnewspeedproject.dto.CommentResponseDto;
import com.sparta.trillionnewspeedproject.security.UserDetailsImpl;
import com.sparta.trillionnewspeedproject.service.CommentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.RejectedExecutionException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class CommentController {
	private final CommentService commentService;

	// 선택한 게시글에 대한 모든 댓글 조회
	@GetMapping("/{postId}/comments")
	public ResponseEntity<List<CommentResponseDto>> getCommentsByPostId(@PathVariable Long postId) {
		List<CommentResponseDto> responseDtos = commentService.getCommentsByPostId(postId);
		return ResponseEntity.ok().body(responseDtos);
	}

	// 댓글 작성
	@PostMapping("/{postId}/comments")
	public ResponseEntity<ApiResponseDto> createComment(@PathVariable Long postId, @RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
		try {
			CommentResponseDto responseDto = commentService.createComment(postId, requestDto, userDetails.getUser());
			return ResponseEntity.ok().body(responseDto);
		}
		// postId가 존재하지 않을 때 오류 메시지 반환
		catch (EntityNotFoundException notFoundException) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ApiResponseDto(notFoundException.getMessage(), HttpStatus.NOT_FOUND.value()));
		}
	}

	// 선택한 댓글 수정
	@PutMapping("/{postId}/comments/{commentId}")
	public ResponseEntity<ApiResponseDto> updateComment(@PathVariable Long postId, @PathVariable Long commentId, @RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
		// 오류가 나지 않을 경우 해당 댓글 수정
		try {
			CommentResponseDto result = commentService.updateComment(postId, commentId, requestDto, userDetails.getUser());
			return ResponseEntity.ok().body(result);
		}
		// postId 받은 것과 comment DB에 저장된 postId가 다를 경우, 댓글이 존재하지 않는 경우 오류 메시지 반환
		catch (EntityNotFoundException notFoundException) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ApiResponseDto(notFoundException.getMessage(), HttpStatus.NOT_FOUND.value()));
		}
		// 다른 유저가 수정을 시도할 경우 오류 메시지 반환
		catch (RejectedExecutionException rejectedExecutionException) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ApiResponseDto(rejectedExecutionException.getMessage(), HttpStatus.BAD_REQUEST.value()));
		}
	}

	// 선택한 댓글 삭제
	@DeleteMapping("/{postId}/comments/{commentId}")
	public ResponseEntity<ApiResponseDto> deleteComment(@PathVariable Long postId, @PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
		// 오류가 나지 않을 경우 해당 댓글 삭제
		try {
			commentService.deleteComment(postId, commentId, userDetails.getUser());
			return ResponseEntity.ok().body(new ApiResponseDto("해당 댓글의 삭제를 완료했습니다.", HttpStatus.OK.value()));
		}
		// postId 받은 것과 comment DB에 저장된 postId가 다를 경우, 댓글이 존재하지 않을 경우 오류 메시지 반환
		catch (EntityNotFoundException notFoundException) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ApiResponseDto(notFoundException.getMessage(), HttpStatus.NOT_FOUND.value()));
		}
		// 다른 유저가 삭제를 시도할 경우 오류 메시지 반환
		catch (RejectedExecutionException rejectedExecutionException) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ApiResponseDto(rejectedExecutionException.getMessage(), HttpStatus.BAD_REQUEST.value()));
		}
	}

	// 선택한 댓글 좋아요 추가
	@PostMapping("/{postId}/comments/{commentId}/like")
	public ResponseEntity<ApiResponseDto> commentInsertLike(@PathVariable Long postId, @PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
		// 오류가 나지 않을 경우 해당 댓글 좋아요 추가
		try {
			CommentResponseDto responseDto = commentService.commentInsertLike(postId, commentId, userDetails.getUser());
			return ResponseEntity.ok().body(responseDto);
		}
		// postId 받은 것과 comment DB에 저장된 postId가 다를 경우, 댓글이 존재하지 않을 경우 오류 메시지 반환
		catch (EntityNotFoundException notFoundException) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ApiResponseDto(notFoundException.getMessage(), HttpStatus.NOT_FOUND.value()));
		}
		// 작성한 유저/관리자가 좋아요를 시도할 경우 오류 메시지 반환
		catch (RejectedExecutionException rejectedExecutionException) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ApiResponseDto(rejectedExecutionException.getMessage(), HttpStatus.BAD_REQUEST.value()));
		}
		// 사용자가 이미 좋아요를 누른 경우 오류 메시지 반환
		catch (DataIntegrityViolationException dataIntegrityViolationException) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(new ApiResponseDto(dataIntegrityViolationException.getMessage(), HttpStatus.CONFLICT.value()));
		}
	}

	// 선택한 댓글 좋아요 취소
	@DeleteMapping("/{postId}/comments/{commentId}/like")
	public ResponseEntity<ApiResponseDto> commentDeleteLike(@PathVariable Long postId, @PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
		// 오류가 나지 않을 경우 해당 댓글 좋아요 취소
		try {
			CommentResponseDto responseDto = commentService.commentDeleteLike(postId, commentId, userDetails.getUser());
			return ResponseEntity.ok().body(responseDto);
		}
		// postId 받은 것과 comment DB에 저장된 postId가 다를 경우, 댓글이 존재하지 않을 경우 오류 메시지 반환
		catch (EntityNotFoundException notFoundException) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ApiResponseDto(notFoundException.getMessage(), HttpStatus.NOT_FOUND.value()));
		}
		// 작성한 유저/관리자가 좋아요를 시도할 경우 오류 메시지 반환
		catch (RejectedExecutionException rejectedExecutionException) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ApiResponseDto(rejectedExecutionException.getMessage(), HttpStatus.BAD_REQUEST.value()));
		}
		// 사용자가 좋아요를 누른 적이 없는 경우 오류 메시지 반환
		catch (NoSuchElementException noSuchElementException) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(new ApiResponseDto(noSuchElementException.getMessage(), HttpStatus.CONFLICT.value()));
		}
	}
}
