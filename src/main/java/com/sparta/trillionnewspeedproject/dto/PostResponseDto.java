package com.sparta.trillionnewspeedproject.dto;

import com.sparta.trillionnewspeedproject.entity.Post;
import com.sparta.trillionnewspeedproject.dto.ApiResponseDto;
import com.sparta.trillionnewspeedproject.dto.CommentResponseDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Getter
@Setter
public class PostResponseDto extends ApiResponseDto {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String username;
    private long likeCnt;
    private List<CommentResponseDto> comments;

    public PostResponseDto(Post post) {
        this.id = post.getPost_id();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
        this.username = post.getUser().getUsername();
        this.likeCnt = post.getLikeCnt();
        if (!(post.getComments() == null)) {
            this.comments = post.getComments().stream()
                    .map(CommentResponseDto::new)
                    .sorted(Comparator.comparing(CommentResponseDto::getCreatedAt).reversed()) // 작성날짜 내림차순 - reversed,
                    // getCreatedAt - 작성일자, comparing - 비교 연산자, sorted - 정렬
                    .toList();
        }
    }
}
