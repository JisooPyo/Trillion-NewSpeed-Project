package com.sparta.trillionnewspeedproject.service;

import com.sparta.trillionnewspeedproject.dto.*;
import com.sparta.trillionnewspeedproject.entity.SignupAuth;
import com.sparta.trillionnewspeedproject.entity.User;
import com.sparta.trillionnewspeedproject.entity.UserRoleEnum;
import com.sparta.trillionnewspeedproject.repository.SignupAuthRepository;
import com.sparta.trillionnewspeedproject.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Component
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final SignupAuthRepository signupAuthRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;


    // ADMIN_TOKEN
    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";


    //회원가입 메서드 - 회원가입 완료버튼을 눌렀을 때 동작할 메서드
    //회원가입을 위해 요청받은 requestBody 내 정보(userId,username,password,introduce,email)을 이용하여 계정 생성
    //userId, Email은 중복 불가능
    @Transactional
    public ApiResponseDto signup(SignupRequestDto requestDto, HttpServletResponse response) {
        String userId = requestDto.getUserId();
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());
        String introduce = requestDto.getIntroduce();
        String email = requestDto.getEmail();
        // 아이디 중복 확인
        Optional<User> checkUserId = userRepository.findByUserId(userId);
        // email 중복확인
        Optional<User> checkEmail = userRepository.findByEmail(email);
        // 회원가입 이메일 인증번호 검증여부 확인
        Optional<SignupAuth> checkSignupAuth = signupAuthRepository.findByEmail(email);

        if (checkUserId.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 ID입니다.");
        }

        if (checkEmail.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
        //이메일 인증 정보가 Table에 없거나, 상태코드가 1(OK)이 아닌경우
        if(checkSignupAuth.isEmpty()||checkSignupAuth.get().getAuthStatus()!=1){
            throw new IllegalArgumentException("이메일 인증이 수행되지 않았습니다. 이메일 인증을 완료해주세요.");
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
        response.setStatus(200);
        return new ApiResponseDto("회원가입 완료", response.getStatus());
    }

    //userId로 User 찾기
    private User findUser(String userId) {
        return userRepository.findByUserId(userId).orElseThrow(() ->
                new IllegalArgumentException("선택한 유저는 존재하지 않습니다.")
        );
    }

    //로그인한 사용자의 프로필을 얻는 메서드
    //user가 존재하면 프로필정보(이름, 이메일, 한줄소개) return
    public ApiResponseDto getUserProfile(User user, HttpServletResponse response) {
        User targetUser = findUser(user.getUserId());
        if (targetUser != null) {
            return new UserProfileResponseDto(user.getUserId(), user.getUsername(), user.getEmail(), user.getIntroduce());
        } else {
            throw new IllegalArgumentException("해당 사용자는 존재하지 않습니다.");
        }
    }

    //사용자 프로필을 변경하는 메서드
    //user가 존재할 경우, requestBody의 username, introduce 값으로 변경
    @Transactional
    public ApiResponseDto modifyUserProfile(User user, UserProfileRequestDto requestDto, HttpServletResponse response) {
        User targetUser = findUser(user.getUserId());
        if (targetUser != null) {
            targetUser.modifyProfile(requestDto);
            response.setStatus(201);
            return new ApiResponseDto("프로필 변경이 완료되었습니다.", response.getStatus());
        } else {
            throw new IllegalArgumentException("해당 아이디는 존재하지 않습니다.");
        }
    }

    //비밀번호를 변경하는 메서드
    //1. user가 존재할 경우, 존재하는 유저의 password와 requestBody에 입력된 password가 일치하는지 확인
    //2. 비밀번호 대조결과 일치하는 경우, requestBody에 읿력된 modifypassword값으로 비밀번호 변경
    @Transactional
    public ApiResponseDto modifyUserPassword(User user, UserPasswordRequestDto requestDto, HttpServletResponse response) {
        User targetUser = findUser(user.getUserId());
        if (targetUser != null) {
            if (passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
                response.setStatus(201);
                targetUser.modifyPassword(passwordEncoder.encode(requestDto.getModifyPassword()));
            } else {
                throw new IllegalArgumentException("비밀번호가 틀립니다.");
            }
        } else {
            throw new IllegalArgumentException("해당 사용자는 존재하지 않습니다.");
        }
        return new ApiResponseDto("비밀번호 변경이 완료되었습니다.", response.getStatus());
    }


    //아이디찾기 메서드
    //requestBody내 email정보를 통해 대상user를 찾고, 찾은 user의 이름이 requestBody에 입력된 username과 일치할 경우
    //아이디정보를 전달
    public ApiResponseDto findID(FindIDRequestDto requestDto, HttpServletResponse response) {
        User targetUser = userRepository.findByEmail(requestDto.getEmail()).orElse(null);
        if (targetUser != null) {
            if (targetUser.getUsername().equals(requestDto.getUsername())) {
                return new FindIDResponseDto(targetUser);
            } else {
                throw new IllegalArgumentException("사용자명이 일치하지 않습니다.");
            }
        } else {
            throw new IllegalArgumentException("해당 이메일로 가입한 사용자가 없습니다.");
        }
    }

    //비밀번호찾기(임시비밀번호발급) 메서드
    //requestBody 내 userId로 대상 user를 찾기
    //찾은 user의 정보와 requestBody 내 email, username정보가 모두 일치하면
    //비밀번호를 랜덤값으로 변경하고, 사용자 이메일에 비밀번호를 전송
    @Transactional
    public ApiResponseDto findPW(FindPWRequestDto requestDto, HttpServletResponse response) {
        User targetUser = findUser(requestDto.getUserId());
        if (targetUser != null) {
            if (targetUser.getUsername().equals(requestDto.getUsername()) && targetUser.getEmail().equals(requestDto.getEmail())) {
                String tempPW = getTempCode();//무작위 10자리 0~9, A~Z조합 반환
                targetUser.modifyPassword(passwordEncoder.encode(tempPW));//임시 비밀번호로 비밀번호 업데이트
                modifyPasswordEmailSend(requestDto, tempPW);
                response.setStatus(200);
                return new ApiResponseDto("입력하신 이메일로 임시 비밀번호를 전달했습니다.", response.getStatus());
            } else {
                throw new IllegalArgumentException("사용자명 또는 이메일이 일치하지 않습니다.");
            }
        } else {
            throw new IllegalArgumentException("해당 아이디는 존재하지 않습니다.");
        }
    }

    //회원가입을 위한 이메일 인증 메서드
    //1. 이미 회원가입된 이메일이 있는지 확인
    //2. 이전에 인증번호요청이력이 있는지 확인. 있으면 삭제
    //3.인증번호 발급,메일 보내기
    //4. signupauth 테이블에 이메일정보 & 인증키(authKey) 저장(authStatus = default 0, createdAt = 생성일시)
    //5. 메일에서 받은 인증번호 데이터를 입력했을 때 일치하는 경우, authStatus = 1로 업데이트 (인증 완료.)
    //6. 회원가입 최종 단계에서 검증 authStatus = 1 여부 확인 - (signup메서드에 적용)
    @Transactional
    public ApiResponseDto Authentication(AuthenticationRequestDto requestDto, HttpServletResponse response) {
        //0. 인증 테이블 내 인증시간이 만료된 데이터가 있으면 삭제
        signupAuthRepository.deleteExpiredSignupAuth();

        //1. 이미 회원가입된 이메일이 있는지 확인
        User targetUser = userRepository.findByEmail(requestDto.getEmail()).orElse(null);
        if (targetUser != null) {
            throw new IllegalArgumentException("이미 가입한 사용자의 이메일입니다. 다른 이메일을 입력해주세요.");
        }

        //2. 이전에 인증번호요청이력이 있는지 확인. 있으면 삭제
        SignupAuth targetSignupAuth = signupAuthRepository.findByEmail(requestDto.getEmail()).orElse(null);
        if (targetSignupAuth != null) {
            signupAuthRepository.delete(targetSignupAuth);
        }

        //3.인증번호 발급, 메일 보내기
        String authKey = getTempCode();
        authenticationEmailSend(authKey, requestDto.getEmail());

        //4.인증 DB  테이블에 이메일정보 & 인증키(authKey) 저장(status = default 0)
        SignupAuth signupAuth = new SignupAuth(requestDto.getEmail(), authKey);
        signupAuthRepository.save(signupAuth);
        return new ApiResponseDto("입력하신 이메일로 인증번호를 전송했습니다.", response.getStatus());
    }

    //회원가입 인증번호를 이메일전송하는 메서드
    private void authenticationEmailSend(String authKey, String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Trillion 회원가입 이메일 인증번호");
        message.setText("안녕하세요? Trillion에서 이메일 인증번호를 안내드립니다.\n 인증번호 :" + authKey + "\n해당 인증번호를 복사하여 회원가입 홈페이지에서 이메일 인증을 완료해주세요.");
        mailSender.send(message);
    }

    @Transactional
    public ApiResponseDto Verification(VerificationRequestDto requestDto, HttpServletResponse response) {
        //DB 저장된 회원가입 인증번호, 이메일 가져오기
        SignupAuth targetSignupAuth = signupAuthRepository.findByEmail(requestDto.getEmail()).orElse(null);
        if (targetSignupAuth == null) {
            throw new IllegalArgumentException("인증번호가 발급되지 않은 상태입니다. 인증번호 발급 버튼을 먼저 눌러주세요.");
        }

        //인증시간이 만료된 경우(생성 후 5분이 지났을 경우)   시간A.isBefore(시간 B) : A가 B보다 과거일 때 true
        if (targetSignupAuth.getExpirationTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("인증번호 발급 후 5분이 경과하였습니다. 인증번호를 다시 발급해주세요.");
        }

        //인증번호 대조 - 일치
        if (targetSignupAuth.getAuthKey().equals(requestDto.getAuthKey())) {
            System.out.println("인증번호 일치");
            targetSignupAuth.changeStatusOK(); // 인증번호 상태값 1로 변경
            return new ApiResponseDto("인증번호 확인이 완료되었습니다.", response.getStatus());
        } else{//인증번호 대조 - 불일치
            targetSignupAuth.changeStatusNO(); // 인증번호 상태값 0으로 변경
            throw new IllegalArgumentException("인증번호가 일치하지 않습니다.");
        }
    }

    //임시비밀번호 발급용이메일을 보내는 메서드
    private void modifyPasswordEmailSend(FindPWRequestDto requestDto, String tempPW) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(requestDto.getEmail());
        message.setSubject(requestDto.getUsername() + "님의 Trillion 계정 임시 비밀번호 발급");
        message.setText("안녕하세요? Trillion에서 " + requestDto.getUsername() + "님의 임시비밀번호를 안내드립니다.\n 임시 비밀번호 :" + tempPW);
        mailSender.send(message);
    }

    //랜덤함수로 랜덤값 만들기
    //A~Z,0~9 중 10개를 뽑아 임시 비밀번호 /회원가입 이메일 인증번호 생성
    public String getTempCode() {
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