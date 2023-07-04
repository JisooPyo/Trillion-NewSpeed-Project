package com.sparta.trillionnewspeedproject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class PostRequestDto {

    private String title;
    private String username;
    private String contents;
    private String createdAt;
}
