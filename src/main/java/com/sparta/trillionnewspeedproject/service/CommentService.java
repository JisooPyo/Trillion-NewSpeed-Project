package com.sparta.trillionnewspeedproject.service;

import com.sparta.trillionnewspeedproject.dto.CommentRequestDto;
import com.sparta.trillionnewspeedproject.dto.CommentResponseDto;
import com.sparta.trillionnewspeedproject.entity.Comment;
import com.sparta.trillionnewspeedproject.entity.Post;
import com.sparta.trillionnewspeedproject.entity.User;
import com.sparta.trillionnewspeedproject.repository.CommentRepository;
import com.sparta.trillionnewspeedproject.repository.PostRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

	private final CommentRepository commentRepository;
	private final PostRepository postRepository;

	// 선택한 게시글에 대한 댓글 전체 조회
	public List<CommentResponseDto> getCommentsByPostId(Long postId) {
		return commentRepository.findAllByPost_idOrderByCreatedAtDesc(postId).stream().map(CommentResponseDto::new).toList();
	}

	// 댓글 작성
	public CommentResponseDto createComment(Long postId, CommentRequestDto requestDto, User user) {
		Post post = findPost(postId);
		Comment comment = new Comment(post, requestDto, user);
		Comment saveComment = commentRepository.save(comment);
		CommentResponseDto commentResponseDto = new CommentResponseDto(saveComment);
		return commentResponseDto;
	}

	// 선택한 댓글 수정
	@Transactional
	public CommentResponseDto updateComment(Long postId, Long commentId, CommentRequestDto requestDto, User user, HttpServletResponse response) {

		// postId 받은 것과 comment DB에 저장된 postId가 다를 경우 오류 코드 반환
		if (postId != findComment(commentId).getPost().getId()) {
			response.setStatus(404);
			return null;

		// 다른 유저가 수정을 시도할 경우 오류 코드 반환
		} else if (!checkUser(commentId, user)) {
			response.setStatus(400);
			return null;

		// 오류가 나지 않을 경우 해당 댓글 수정
		} else {
			findComment(commentId).update(requestDto);
			CommentResponseDto commentResponseDto = new CommentResponseDto(findComment(commentId));
			return commentResponseDto;
		}
	}

	// 선택한 댓글 삭제
	public void deleteComment(Long postId, Long commentId, @AuthenticationPrincipal User user, HttpServletResponse response) {

		// postId 받은 것과 comment DB에 저장된 postId가 다를 경우 오류 코드 반환
		if (postId != findComment(commentId).getPost().getId()) {
			response.setStatus(404);

		// 다른 유저가 삭제를 시도할 경우 오류 코드 반환
		} else if (!checkUser(commentId, user)) {
			response.setStatus(400);

		// 오류가 나지 않을 경우 해당 댓글 삭제
		} else {
			commentRepository.delete(findComment(commentId));
		}
	}

	// 선택한 댓글 좋아요 기능 추가
	public CommentResponseDto commentLike(Long postId, Long commentId, User user, HttpServletResponse response) {

		// postId 받은 것과 comment DB에 저장된 postId가 다를 경우 오류 코드 반환
		if (postId != findComment(commentId).getPost().getId()) {
			response.setStatus(404);
			return null;

		// 작성자가 좋아요를 시도할 경우 오류 코드 반환
		} else if (checkUser(commentId, user)) {
			response.setStatus(400);
			return null;

		// 오류가 나지 않을 경우 해당 댓글 좋아요 추가
		} else {
			Comment comment = findComment(commentId);
			comment.updateLikes();
			Comment savedComment = commentRepository.save(comment);
			CommentResponseDto commentResponseDto = new CommentResponseDto(savedComment);
			return commentResponseDto;
		}
	}

	// id에 따른 댓글 찾기
	private Comment findComment(Long commentId) {
		return commentRepository.findById(commentId).orElseThrow(() ->
				new IllegalArgumentException("선택한 댓글은 존재하지 않습니다.")
		);
	}

	// id에 따른 게시글 찾기
	private Post findPost(Long postId) {
		return postRepository.findById(postId).orElseThrow(() ->
				new IllegalArgumentException("선택한 게시글은 존재하지 않습니다.")
		);
	}

	// 선택한 댓글의 사용자가 맞는지 혹은 관리자인지 확인하기
	private boolean checkUser(Long selectId, User user) {
		Comment comment = findComment(selectId);
		if (comment.getUser().getUserId().equals(user.getUserId()) || user.getRole().getAuthority().equals("ADMIN")) {
			return true;
		} else {
			return false;
		}
	}
}