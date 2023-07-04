package com.sparta.trillionnewspeedproject.controller;

import com.sparta.trillionnewspeedproject.dto.PostRequestDto;
import com.sparta.trillionnewspeedproject.dto.PostResponseDto;
import com.sparta.trillionnewspeedproject.security.UserDetailsImpl;
import com.sparta.trillionnewspeedproject.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostsController {

    private final PostService postService;


    // 게시글 작성
//    @ResponseBody
    @PostMapping("/posts")
    public PostResponseDto createPost(@RequestBody PostRequestDto postRequest, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.createPost(postRequest, userDetails.getUser());
    }

    // 특정 게시물 조회
//    @ResponseBody
    @GetMapping("/posts/{postid}")
    public PostResponseDto getPost(@PathVariable Long postid) {
        return postService.getPost(postid);
    }


    //게시글 수정
//    @ResponseBody
    @PutMapping("/posts/{postid}")
    public PostResponseDto updatePost(@PathVariable Long postid, @RequestBody PostRequestDto requestDto) {
        return postService.updatePost(postid, requestDto);
    }



    //게시글 삭제
//    @ResponseBody
    @DeleteMapping("/posts/{postid}")
    public PostResponseDto deletePost(@PathVariable Long postid) {
        return postService.deletePost(postid);
    }

//    // 내가 쓴 글 보기
//    @GetMapping("/myposts")
//    public String getMyposts(HttpServletRequest req) {
//        System.out.println("ProductController.getProducts : 인증 완료");
//        User user = (User) req.getAttribute("user");
//        System.out.println("user.getUsername() = " + user.getUsername());
//
//        return "redirect:/";
//    }
//
//    @Secured(UserRoleEnum.Authority.ADMIN) // 블로그 관리자
//    @GetMapping("/products/secured")
//    public String getPostsByAdmin(@AuthenticationPrincipal UserDetailsImpl userDetails) {
//        System.out.println("userDetails.getUsername() = " + userDetails.getUsername());
//        for (GrantedAuthority authority : userDetails.getAuthorities()) {
//            System.out.println("authority.getAuthority() = " + authority.getAuthority());
//        }
//
//        return "redirect:/";
//    }
}
