package com.sparta.trillionnewspeedproject.service;

import com.sparta.trillionnewspeedproject.dto.CommentRequestDto;
import com.sparta.trillionnewspeedproject.dto.CommentResponseDto;
import com.sparta.trillionnewspeedproject.entity.Comment;
import com.sparta.trillionnewspeedproject.entity.Post;
import com.sparta.trillionnewspeedproject.entity.User;
import com.sparta.trillionnewspeedproject.entity.UserRoleEnum;
import com.sparta.trillionnewspeedproject.repository.CommentRepository;
import com.sparta.trillionnewspeedproject.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.RejectedExecutionException;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final PostService postService;
    private final CommentRepository commentRepository;

    public CommentResponseDto createComment(CommentRequestDto requestDto, User user) {
        Post post = postService.findPost(requestDto.getPostId());
        Comment comment = new Comment(requestDto.getBody());
        comment.setUser(user);
        comment.setPost(post);

        var savedComment = commentRepository.save(comment);

        return new CommentResponseDto(savedComment);
    }

    public void deleteComment(Long id, User user) {
        Comment comment = commentRepository.findById(id).orElseThrow();

        // 요청자가 운영자 이거나 댓글 작성자(post.user) 와 요청자(user) 가 같은지 체크
        if (!user.getRole().equals(UserRoleEnum.ADMIN) && !comment.getUser().equals(user)) {
            throw new RejectedExecutionException();
        }

        commentRepository.delete(comment);
    }

    @Transactional // entity 가 수정이 일어나거나 삭제가 발생하면 그 부분이 애너테이션 걸어둔 메서드가 종료되는 시점에 반영되도록!!
    // 중간에 수정해도 중간에는 반영이 되지 않는다!
    public CommentResponseDto updateComment(Long id, CommentRequestDto requestDto, User user) {
        Comment comment = commentRepository.findById(id).orElseThrow();

        // 요청자가 운영자 이거나 댓글 작성자(post.user) 와 요청자(user) 가 같은지 체크
        if (!user.getRole().equals(UserRoleEnum.ADMIN) && !comment.getUser().equals(user)) {
            throw new RejectedExecutionException();
        }

        comment.setBody(requestDto.getBody());

        return new CommentResponseDto(comment);
    }

}