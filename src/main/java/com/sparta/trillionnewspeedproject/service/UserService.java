package com.sparta.trillionnewspeedproject.service;

import com.sparta.trillionnewspeedproject.dto.*;
import com.sparta.trillionnewspeedproject.entity.User;
import com.sparta.trillionnewspeedproject.entity.UserRoleEnum;
import com.sparta.trillionnewspeedproject.exception.ErrorCode;
import com.sparta.trillionnewspeedproject.exception.GlobalExceptionHandler;
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

    private final GlobalExceptionHandler globalExceptionHandler;

    // ADMIN_TOKEN
    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";


    //회원가입 메서드
    //회원가입을 위해 요청받은 requestBody 내 정보(userId,username,password,introduce,email)을 이용하여 계정 생성
    //userId, Email은 중복 불가능
    public ResponseMessageDto signup(SignupRequestDto requestDto, HttpServletResponse response) {
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
            return globalExceptionHandler.badRequestException(ErrorCode.USERID_EXIST_ERROR);

        }

        if (checkEmail.isPresent()) {
            response.setStatus(400);
            return globalExceptionHandler.badRequestException(ErrorCode.EMAIL_EXIST_ERROR);
        }

        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (requestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                response.setStatus(400);
                return new ResponseMessageDto( " 관리자 암호가 틀려 등록이 불가능합니다.", response.getStatus());
            }
            role = UserRoleEnum.ADMIN;
        }
        // 사용자 등록
        User user = new User(userId, username, password, email, introduce, role);
        userRepository.save(user);
        response.setStatus(200);
        return new ResponseMessageDto( "회원가입 완료", response.getStatus());
    }

    //userId로 User 찾기
    private User findUser(String userId) {
        return userRepository.findByUserId(userId).orElseThrow(() ->
                new IllegalArgumentException("선택한 유저는 존재하지 않습니다.")
        );
    }

    //로그인한 사용자의 프로필을 얻는 메서드
    //user가 존재하면 프로필정보(이름, 이메일, 한줄소개) return
    public Object getUserProfile(User user, HttpServletResponse response) {
        User targetUser = findUser(user.getUserId());
        if (targetUser != null) {
            return new UserProfileResponseDto(user.getUserId(), user.getUsername(), user.getEmail(), user.getIntroduce());
        } else {
            response.setStatus(400);
            return new ResponseMessageDto("해당 사용자는 존재하지 않습니다.", response.getStatus());
        }
    }

    //사용자 프로필을 변경하는 메서드
    //user가 존재할 경우, requestBody의 username, introduce 값으로 변경
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

    //비밀번호를 변경하는 메서드
    //1. user가 존재할 경우, 존재하는 유저의 password와 requestBody에 입력된 password가 일치하는지 확인
    //2. 비밀번호 대조결과 일치하는 경우, requestBody에 읿력된 modifypassword값으로 비밀번호 변경
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


    //아이디찾기 메서드
    //requestBody내 email정보를 통해 대상user를 찾고, 찾은 user의 이름이 requestBody에 입력된 username과 일치할 경우
    //아이디정보를 전달
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

    //비밀번호찾기(임시비밀번호발급) 메서드
    //requestBody 내 userId로 대상 user를 찾기
    //찾은 user의 정보와 requestBody 내 email, username정보가 모두 일치하면
    //비밀번호를 랜덤값으로 변경하고, 사용자 이메일에 비밀번호를 전송
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

    //이메일블 보내는 메서드(임시비밀번호 발급용)
    private void mailSend(FindPWRequestDto requestDto, String tempPW) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(requestDto.getEmail());
        message.setSubject(requestDto.getUsername() + "님의 Trillion 계정 임시 비밀번호 발급");
        message.setText("안녕하세요? Trillion에서 " + requestDto.getUsername() + "님의 임시비밀번호를 안내드립니다.\n 임시 비밀번호 :" + tempPW);
        mailSender.send(message);
    }

    //랜덤함수로 임시비밀번호 만들기
    //A~Z,0~9 중 10개를 뽑아 임시 비밀번호 생성
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