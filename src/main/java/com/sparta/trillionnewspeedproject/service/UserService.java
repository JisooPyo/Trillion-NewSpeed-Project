package com.sparta.trillionnewspeedproject.service;

import com.sparta.trillionnewspeedproject.dto.SignupRequestDto;
import com.sparta.trillionnewspeedproject.entity.User;
import com.sparta.trillionnewspeedproject.entity.UserRoleEnum;
import com.sparta.trillionnewspeedproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // ADMIN_TOKEN
    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    public void signup(SignupRequestDto requestDto) {
        String userId = requestDto.getUserId();
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());
        String introduce = requestDto.getIntroduce();
        String email = requestDto.getEmail();
        // 아이디 중복 확인
        Optional<User> checkUserId = userRepository.findByUserId(userId);
        // email 중복확인
        Optional<User> checkEmail = userRepository.findByEmail(email);

        if (checkUserId.isPresent()) {
            throw new IllegalArgumentException("중복된 ID입니다.");
        }

        if (checkEmail.isPresent()) {
            throw new IllegalArgumentException("중복된 Email입니다.");
        }

        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (requestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = UserRoleEnum.ADMIN;
        }

        // 사용자 등록
        User user = new User(userId, username, password, email, introduce, role);
        userRepository.save(user);
        System.out.println("회원가입 완료");
        System.out.println(user);
    }
}