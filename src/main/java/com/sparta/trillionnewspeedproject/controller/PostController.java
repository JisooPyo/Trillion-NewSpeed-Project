package com.sparta.trillionnewspeedproject.controller;

import com.sparta.trillionnewspeedproject.dto.ApiResponseDto;
import com.sparta.trillionnewspeedproject.dto.PostListResponseDto;
import com.sparta.trillionnewspeedproject.dto.PostRequestDto;
import com.sparta.trillionnewspeedproject.dto.PostResponseDto;
import com.sparta.trillionnewspeedproject.security.UserDetailsImpl;
import com.sparta.trillionnewspeedproject.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.concurrent.RejectedExecutionException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 게시글 작성
    @PostMapping("/posts")
    public ResponseEntity<PostResponseDto> createPost(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody PostRequestDto requestDto) {
        PostResponseDto result = postService.createPost(requestDto, userDetails.getUser());

        return ResponseEntity.status(201).body(result);
    }


    // 게시글 전체 조회
    @GetMapping("/posts")
    public ResponseEntity<PostListResponseDto> getPosts() {
        PostListResponseDto result = postService.getPosts();

        return ResponseEntity.ok().body(result);
    }


    // 특정 게시글 조회
    @GetMapping("/posts/{postid}")
    public ResponseEntity<PostResponseDto> getPostById(@PathVariable Long postid) {
        PostResponseDto result = postService.getPostById(postid);

        return ResponseEntity.ok().body(result);
    }


    // 게시글 수정
    @PutMapping("/posts/{postid}")
    public ResponseEntity<ApiResponseDto> updatePost(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long postid, @RequestBody PostRequestDto requestDto) {
        try {
            PostResponseDto result = postService.updatePost(postid, requestDto, userDetails.getUser());
            return ResponseEntity.ok().body(result);
        } catch (RejectedExecutionException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto("작성자만 수정 할 수 있습니다.", HttpStatus.BAD_REQUEST.value()));
        }
    }


    // 게시글 삭제
    @DeleteMapping("/posts/{postid}")
    public ResponseEntity<ApiResponseDto> deletePost(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long postid) {
        try {
            postService.deletePost(postid, userDetails.getUser());
            return ResponseEntity.ok().body(new ApiResponseDto("게시글 삭제 성공", HttpStatus.OK.value()));
        } catch (RejectedExecutionException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto("작성자만 삭제 할 수 있습니다.", HttpStatus.BAD_REQUEST.value()));
        }
    }

    // 선택한 게시글 좋아요 추가
    @PostMapping("/posts/{postId}/like")
    public ResponseEntity<ApiResponseDto> postInsertLike(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 오류가 나지 않을 경우 해당 댓글 좋아요 추가
        try {
            PostResponseDto responseDto = postService.postInsertLike(postId, userDetails.getUser());
            return ResponseEntity.ok().body(responseDto);
        }
        // 게시글이 존재하지 않을 경우 오류 메시지 반환
        catch (IllegalArgumentException illegalArgumentException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponseDto(illegalArgumentException.getMessage(), HttpStatus.NOT_FOUND.value()));
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

    // 선택한 게시글 좋아요 취소
    @DeleteMapping("/posts/{postId}/like")
    public ResponseEntity<ApiResponseDto> postDeleteLike(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 오류가 나지 않을 경우 해당 댓글 좋아요 취소
        try {
            PostResponseDto responseDto = postService.postDeleteLike(postId, userDetails.getUser());
            return ResponseEntity.ok().body(responseDto);
        }
        // 게시글이 존재하지 않을 경우 오류 메시지 반환
        catch (IllegalArgumentException illegalArgumentException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponseDto(illegalArgumentException.getMessage(), HttpStatus.NOT_FOUND.value()));
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