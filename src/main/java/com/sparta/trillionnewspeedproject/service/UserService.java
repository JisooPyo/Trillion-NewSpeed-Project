package com.sparta.trillionnewspeedproject.service;

import com.sparta.trillionnewspeedproject.dto.*;
import com.sparta.trillionnewspeedproject.entity.User;
import com.sparta.trillionnewspeedproject.entity.UserRoleEnum;
import com.sparta.trillionnewspeedproject.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Component
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    // ADMIN_TOKEN
    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";


    public String signup(SignupRequestDto requestDto, HttpServletResponse response) {
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
            response.setStatus(400);
            return "중복된 ID입니다.";
        }

        if (checkEmail.isPresent()) {
            response.setStatus(400);
            return "중복된 Email입니다.";
        }

        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (requestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                response.setStatus(400);
                return "관리자 암호가 틀려 등록이 불가능합니다.";
            }
            role = UserRoleEnum.ADMIN;
        }
        // 사용자 등록
        User user = new User(userId, username, password, email, introduce, role);
        userRepository.save(user);
        response.setStatus(200);
        return "회원가입 완료";
    }

    private User findUser(String userId) {
        return userRepository.findByUserId(userId).orElseThrow(() ->
                new IllegalArgumentException("선택한 유저는 존재하지 않습니다.")
        );
    }

    public Object getUserProfile(User user, HttpServletResponse response) {
        User targetUser = findUser(user.getUserId());
        if (targetUser != null) {
            return new UserProfileResponseDto(user.getUserId(), user.getUsername(), user.getEmail(), user.getIntroduce());
        } else {
            response.setStatus(400);
            return new ResponseMessageDto("해당 사용자는 존재하지 않습니다.", response.getStatus());
        }
    }

    @Transactional
    public ResponseMessageDto modifyUserProfile(User user, UserProfileRequestDto requestDto, HttpServletResponse response) {
        User targetUser = findUser(user.getUserId());
        if (targetUser != null) {
            targetUser.modifyProfile(requestDto);
            response.setStatus(201);
            return new ResponseMessageDto("프로필 변경이 완료되었습니다.", response.getStatus());
        } else {
            response.setStatus(400);
            return new ResponseMessageDto("해당 아이디는 존재하지 않습니다.", response.getStatus());
        }
    }

    @Transactional
    public ResponseMessageDto modifyUserPassword(User user, UserPasswordRequestDto requestDto, HttpServletResponse response) {
        User targetUser = findUser(user.getUserId());
        if (targetUser != null) {
            if (passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
                response.setStatus(201);
                targetUser.modifyPassword(passwordEncoder.encode(requestDto.getModifyPassword()));
            } else {
                response.setStatus(400);
                return new ResponseMessageDto("비밀번호가 틀립니다.", response.getStatus());
            }
        } else {
            response.setStatus(400);
            return new ResponseMessageDto("해당 사용자는 존재하지 않습니다.", response.getStatus());
        }
        return new ResponseMessageDto("비밀번호 변경이 완료되었습니다.", response.getStatus());
    }


    public Object findID(FindIDRequestDto requestDto, HttpServletResponse response) {
        User targetUser = userRepository.findByEmail(requestDto.getEmail()).orElse(null);
        if(targetUser!=null) {
            if (targetUser.getUsername().equals(requestDto.getUsername())) {
                return new FindIDResponseDto(targetUser);
            } else {
                response.setStatus(400);
                return new ResponseMessageDto("사용자명이 일치하지 않습니다.", response.getStatus());
            }
        }else{
            response.setStatus(400);
            return new ResponseMessageDto("해당 이메일로 가입한 사용자가 없습니다.", response.getStatus());
        }
    }

    @Transactional
    public ResponseMessageDto findPW(FindPWRequestDto requestDto, HttpServletResponse response) {
        User targetUser = findUser(requestDto.getUserId());
        if (targetUser != null) {
            if (targetUser.getUsername().equals(requestDto.getUsername()) && targetUser.getEmail().equals(requestDto.getEmail())) {
                String tempPW = getTempPassword();
                targetUser.modifyPassword(passwordEncoder.encode(tempPW));//임시 비밀번호 업데이트
                mailSend(requestDto, tempPW);
                return new ResponseMessageDto("입력하신 이메일로 임시 비밀번호를 전달했습니다.", response.getStatus());
            } else {
                response.setStatus(400);
                return new ResponseMessageDto("사용자명 또는 이메일이 일치하지 않습니다.", response.getStatus());
            }
        } else {
            response.setStatus(400);
            return new ResponseMessageDto("해당 아이디는 존재하지 않습니다.", response.getStatus());
        }
    }

    private void mailSend(FindPWRequestDto requestDto, String tempPW) {
        System.out.println("메일 전송 완료. 변경된 비밀번호 :" + tempPW);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(requestDto.getEmail());
        message.setSubject(requestDto.getUsername() + "님의 Trillion 계정 임시 비밀번호 발급");
        message.setText("안녕하세요? Trillion에서 " + requestDto.getUsername() + "님의 임시비밀번호를 안내드립니다.\n 임시 비밀번호 :" + tempPW);
        mailSender.send(message);
    }

    //랜덤함수로 임시비밀번호 구문 만들기
    public String getTempPassword() {
        char[] charSet = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

        String str = "";

        // 문자 배열 길이의 값을 랜덤으로 10개를 뽑아 구문을 작성함
        int idx = 0;
        for (int i = 0; i < 10; i++) {
            idx = (int) (charSet.length * Math.random());
            str += charSet[idx];
        }
        return str;
    }
}