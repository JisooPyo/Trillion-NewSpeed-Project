package com.sparta.trillionnewspeedproject.dto;

import com.sparta.trillionnewspeedproject.dto.PostResponseDto;
import lombok.Getter;

import java.util.List;

@Getter
public class PostListResponseDto {
    private List<PostResponseDto> postsList;

    public PostListResponseDto(List<PostResponseDto> postList) {
        this.postsList = postList;
    }
}

