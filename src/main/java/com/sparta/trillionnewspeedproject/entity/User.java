package com.sparta.trillionnewspeedproject.entity;

import com.sparta.trillionnewspeedproject.dto.UserPasswordRequestDto;
import com.sparta.trillionnewspeedproject.dto.UserProfileRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String userId;
    

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String introduce;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    public User(String userId, String username, String password, String email, String introduce, UserRoleEnum role) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.introduce = introduce;
        this.role = role;
    }

    public void modifyProfile(UserProfileRequestDto requestDto) {
        this.username = requestDto.getUsername();
        this.introduce = requestDto.getIntroduce();
    }
    public void modifyPassword(String modifyPassword){
        this.password = modifyPassword;
    }
}