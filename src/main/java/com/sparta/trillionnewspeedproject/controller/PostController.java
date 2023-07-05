package com.sparta.trillionnewspeedproject.controller;

import com.sparta.trillionnewspeedproject.dto.ApiResponseDto;
import com.sparta.trillionnewspeedproject.dto.PostListResponseDto;
import com.sparta.trillionnewspeedproject.dto.PostRequestDto;
import com.sparta.trillionnewspeedproject.dto.PostResponseDto;
import com.sparta.trillionnewspeedproject.security.UserDetailsImpl;
import com.sparta.trillionnewspeedproject.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
}