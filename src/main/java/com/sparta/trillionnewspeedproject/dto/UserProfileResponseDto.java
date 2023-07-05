package com.sparta.trillionnewspeedproject.dto;

import com.sparta.trillionnewspeedproject.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserProfileResponseDto {
    String userId;
    String username;
    String email;
    String introduce;

    public UserProfileResponseDto(User user){
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.introduce = user.getIntroduce();

    }
}
