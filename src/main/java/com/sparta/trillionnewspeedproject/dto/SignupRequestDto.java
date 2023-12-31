package com.sparta.trillionnewspeedproject.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {
    @NotBlank
    @Size(min = 4, max = 10, message= "userID는 최소 4자 이상, 10자 이하여야 합니다.")
    @Pattern(regexp = "^[a-z0-9]*$", message = "userID는 알파벳 소문자(a~z), 숫자(0~9)로 구성되어야 합니다.")
    private String userId;


    @NotBlank
    private String username;

    @NotBlank
    @Size(min =8, max = 15,message= "비밀번호는 최소 8자 이상, 15자 이하여야 합니다.")
    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]*$", message = "비밀번호는 알파벳 대소문자(a~z, A~Z), 숫자(0~9), 특수문자로 구성되어야 합니다.")
    private String password;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String introduce;

    private boolean admin = false;
    private String adminToken = "";
}