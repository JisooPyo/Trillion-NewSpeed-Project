package com.sparta.trillionnewspeedproject.service;

import com.sparta.trillionnewspeedproject.dto.PostRequestDto;
import com.sparta.trillionnewspeedproject.dto.PostResponseDto;
import com.sparta.trillionnewspeedproject.entity.Posts;
import com.sparta.trillionnewspeedproject.entity.User;
import com.sparta.trillionnewspeedproject.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    // 게시물 작성
    @Transactional  //  생성,수정,삭제 무조건
    public PostResponseDto createPost(PostRequestDto postRequest, User user) {
        Posts posts = postRepository.save(new Posts(postRequest, user));
        return new PostResponseDto(posts);
//        Posts posts =new Posts(postRequest,user);
//        Posts savePosts = postRepository.save(posts);
//        PostResponseDto postResponseDto = new PostResponseDto(savePosts);
//        return postResponseDto;


//        Posts posts = new Posts(postRequest);
//
//        Posts savePost = postRepository.save(posts);
//
//        PostResponseDto postResponseDto = new PostResponseDto(savePost);
//
//        return postResponseDto;
    }

    //특정 게시글 조회
    public PostResponseDto getPost(Long postid) {
        Posts posts = findPosts(postid);
        return new PostResponseDto(posts);
    }

    // 게시글 수정
    public PostResponseDto updatePost(Long postid, PostRequestDto requestDto) {
        Posts posts = findPosts(postid);

        // 유저 id에 맞는 게시글인지 검증 후 수정 진행
        posts.setTitle(requestDto.getTitle());
        posts.setContents(requestDto.getContents());

        return new PostResponseDto(posts);
    }

    // 게시글 삭제
    public PostResponseDto deletePost(Long postid) {
        Posts posts = findPosts(postid);
        postRepository.delete(posts);

        return new PostResponseDto(posts);
    }


    // 게시글 존재 확인
    private Posts findPosts(Long postid) {
        return postRepository.findById(postid).orElseThrow(() ->
                new IllegalArgumentException("선택한 게시글은 존재하지 않습니다.")
        );

    }


}
