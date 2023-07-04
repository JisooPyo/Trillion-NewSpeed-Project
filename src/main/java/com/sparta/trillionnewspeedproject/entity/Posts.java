package com.sparta.trillionnewspeedproject.entity;

import com.sparta.trillionnewspeedproject.dto.PostRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "posts")
public class Posts extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private Long postid; // 게시물 고유 번호

    @Column(nullable = false)
    private String title;  // 게시글 제목
    private String username;  // 유저 아이디
    private String contents;  // 게시글 내용


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // 유저 고유 번호
    private User user;

    // 게시물 작성
    public Posts(PostRequestDto postRequestDto, User user){
        this.title = postRequestDto.getTitle();
        this.contents = postRequestDto.getContents();
        this.user = user;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setContents(String contents){
        this.contents = contents;
    }

    public void setUsername(String username){
        this.username = username;
    }

}
