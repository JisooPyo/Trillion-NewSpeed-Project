package com.sparta.trillionnewspeedproject.dto;

import com.sparta.trillionnewspeedproject.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@AllArgsConstructor
public class FindPWResponseDto {
    String password;
    PasswordEncoder passwordEncoder;
    public FindPWResponseDto(User user){
        this.password = user.getPassword();

    }
}
