package com.sparta.trillionnewspeedproject.entity;

import com.sparta.trillionnewspeedproject.dto.PostRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "post")
public class Post extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long post_id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "likeCnt")
    private long likeCnt;

    @OneToMany(mappedBy = "post", orphanRemoval = true)
    private List<PostLike> postLikeList;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)  // cascade( 영속성 전이 ) : 연관 관계 같이 삭제
    private List<Comment> comments;

    public Post(PostRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void insertLikeCnt() {
        this.likeCnt++;
    }

    public void deleteLikeCnt() {
        this.likeCnt--;
    }
}
