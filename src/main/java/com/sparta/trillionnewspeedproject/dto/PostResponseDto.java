package com.sparta.trillionnewspeedproject.dto;

import com.sparta.trillionnewspeedproject.entity.Posts;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PostResponseDto {
    private Long postid;
    private String title;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime  modifiedAt;
    private String contents;


    public PostResponseDto(Posts posts){
        this.postid = posts.getPostid();
        this.title = posts.getTitle();
        this.username = posts.getUsername();
        this.contents = posts.getContents();
        this.createdAt = posts.getCreatedAt();
        this.modifiedAt = posts.getModifiedAt();
    }

//    public PostResponseDto(Boolean success) {
//        this.success = success;
//    }
}
