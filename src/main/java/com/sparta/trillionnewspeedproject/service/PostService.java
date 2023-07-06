package com.sparta.trillionnewspeedproject.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.RejectedExecutionException;
import java.util.stream.Collectors;

import com.sparta.trillionnewspeedproject.dto.UserProfileResponseDto;
import com.sparta.trillionnewspeedproject.repository.UserRepository;
import com.sparta.trillionnewspeedproject.entity.PostLike;
import com.sparta.trillionnewspeedproject.repository.PostLikeRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.trillionnewspeedproject.dto.PostListResponseDto;
import com.sparta.trillionnewspeedproject.dto.PostRequestDto;
import com.sparta.trillionnewspeedproject.dto.PostResponseDto;
import com.sparta.trillionnewspeedproject.entity.Post;
import com.sparta.trillionnewspeedproject.entity.User;
import com.sparta.trillionnewspeedproject.entity.UserRoleEnum;
import com.sparta.trillionnewspeedproject.repository.PostRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final UserRepository userRepository;

    public PostResponseDto createPost(PostRequestDto requestDto, User user) {
        User targetUser = findUser(user.getUserId());
        if (targetUser != null) {
            Post post = new Post(requestDto);
            post.setUser(targetUser);
            postRepository.save(post);
            return new PostResponseDto(post);
        } else {
            throw new IllegalArgumentException("해당 사용자는 존재하지 않습니다.");
        }

    }

    public PostListResponseDto getPosts() {
        List<PostResponseDto> postList = postRepository.findAll().stream()
                .map(PostResponseDto::new)
                .collect(Collectors.toList());

        return new PostListResponseDto(postList);
    }

    public PostResponseDto getPostById(Long id) {

        Post post = findPost(id);
        return new PostResponseDto(post);
    }

    public void deletePost(Long id, User user) {
        Post post = findPost(id);

        User targetUser = findUser(user.getUserId());
        if (targetUser != null) {
            // 게시글 작성자(post.user) 와 요청자(user) 가 같은지 또는 Admin 인지 체크 (아니면 예외발생)
            if (!(targetUser.getRole().equals(UserRoleEnum.ADMIN) || post.getUser().equals(targetUser))) {
                throw new RejectedExecutionException();
            }
        } else {
            throw new IllegalArgumentException("해당 사용자는 존재하지 않습니다.");
        }

        postRepository.delete(post);
    }

    @Transactional
    public PostResponseDto updatePost(Long id, PostRequestDto requestDto, User user) {
        Post post = findPost(id);
        User targetUser = findUser(user.getUserId());

        if (targetUser != null) {
            // 게시글 작성자(post.user) 와 요청자(user) 가 같은지 또는 Admin 인지 체크 (아니면 예외발생)
            if (!(targetUser.getRole().equals(UserRoleEnum.ADMIN) || post.getUser().equals(targetUser))) {
                throw new RejectedExecutionException();
            }
        } else {
            throw new IllegalArgumentException("해당 사용자는 존재하지 않습니다.");
        }
        // 게시글 작성자(post.user) 와 요청자(user) 가 같은지 또는 Admin 인지 체크 (아니면 예외발생)
        post.setTitle(requestDto.getTitle());
        post.setContent(requestDto.getContent());

        return new PostResponseDto(post);
    }

    public Post findPost(long id) {
        return postRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 게시글은 존재하지 않습니다.")
        );
    }

    //userId로 User 찾기
    private User findUser(String userId) {
        return userRepository.findByUserId(userId).orElseThrow(() ->
                new IllegalArgumentException("선택한 유저는 존재하지 않습니다.")
        );
    }

    @Transactional
    // 선택한 댓글 좋아요 기능 추가
    public PostResponseDto postInsertLike(Long postId, User user) {
        Post post = findPost(postId);
        // 작성자가 좋아요를 시도할 경우 오류 코드 반환
        if (checkUser(postId, user)) {
            throw new AccessDeniedException("작성자는 좋아요를 누를 수 없습니다.");
        }
        // 좋아요를 이미 누른 경우 오류 코드 반환
        if (findPostLike(user, post) != null) {
            throw new DataIntegrityViolationException("좋아요를 이미 누르셨습니다.");
        }
        // 오류가 나지 않을 경우 해당 댓글 좋아요 추가
        postLikeRepository.save(new PostLike(user, post));
        post.insertLikeCnt();
        PostResponseDto postResponseDto = new PostResponseDto(postRepository.save(post));
        return postResponseDto;
    }

    // 선택한 댓글 좋아요 취소
    public PostResponseDto postDeleteLike(Long postId, User user) {
        Post post = findPost(postId);
        // 작성자가 좋아요를 시도할 경우 오류 코드 반환
        if (checkUser(postId, user)) {
            throw new AccessDeniedException("작성자는 좋아요를 누를 수 없습니다.");
        }
        // 좋아요를 이미 누른 경우 오류 코드 반환
        if (findPostLike(user, post) == null) {
            throw new NoSuchElementException("좋아요를 누르시지 않았습니다.");
        }
        // 오류가 나지 않을 경우 해당 댓글 좋아요 추가
        postLikeRepository.delete(findPostLike(user, post));
        post.deleteLikeCnt();
        PostResponseDto postResponseDto = new PostResponseDto(postRepository.save(post));
        return postResponseDto;
    }

    // 사용자와 댓글에 따른 좋아요 찾기
    private PostLike findPostLike(User user, Post post) {
        return postLikeRepository.findByUserAndPost(user,post).orElse(null);
    }

    // 선택한 게시글의 사용자가 맞는지 혹은 관리자인지 확인하기
    private boolean checkUser(Long selectId, User user) {
        Post post = findPost(selectId);
        if (post.getUser().getUserId().equals(user.getUserId()) || user.getRole().getAuthority().equals("ADMIN")) {
            return true;
        } else {
            return false;
        }
    }
}