package com.sparta.trillionnewspeedproject.entity;

import com.sparta.trillionnewspeedproject.dto.CommentRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "comments")
@NoArgsConstructor
public class Comment extends Timestamped {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "postId", nullable = false)
	private Post post;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long commentId;

	@Column(name = "contents", nullable = false, length = 100)
	private String commentContents;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId", nullable = false)
	private User user;

	@Column(name = "likeCnt")
	private long likeCnt;

	@OneToMany(mappedBy = "comment", orphanRemoval = true)
	private List<CommentLike> commentLikeList;


	public Comment(Post post, CommentRequestDto requestDto, User user) {
		this.post = post;
		this.commentContents = requestDto.getCommentContents();
		this.user = user;
	}

	public void update(CommentRequestDto requestDto) {
		this.commentContents = requestDto.getCommentContents();
	}

	public void insertLikeCnt() {
		this.likeCnt++;
	}

	public void deleteLikeCnt() {
		this.likeCnt--;
	}
}
