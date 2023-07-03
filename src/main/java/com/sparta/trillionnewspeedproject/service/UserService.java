package com.sparta.trillionnewspeedproject.service;

import com.sparta.trillionnewspeedproject.dto.*;
import com.sparta.trillionnewspeedproject.entity.User;
import com.sparta.trillionnewspeedproject.entity.UserRoleEnum;
import com.sparta.trillionnewspeedproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
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

    private User findUser(String userId) {
        return userRepository.findByUserId(userId).orElseThrow(() ->
                new IllegalArgumentException("선택한 유저는 존재하지 않습니다.")
        );
    }

    public UserProfileResponseDto getUserProfile(User user) {
        User targetUser = findUser(user.getUserId());
        if (targetUser != null) {
            return new UserProfileResponseDto(user.getUserId(), user.getUsername(), user.getEmail(), user.getIntroduce());
        } else {
            throw new IllegalArgumentException("해당 사용자는 존재하지 않습니다.");
        }
    }

    @Transactional
    public User modifyUserProfile(User user, UserProfileRequestDto requestDto) {
        User targetUser = findUser(user.getUserId());
        if (targetUser != null) {
            targetUser.modifyProfile(requestDto);
            return targetUser;
        } else {
            throw new IllegalArgumentException("해당 사용자는 존재하지 않습니다.");
        }
    }

    @Transactional
    public void modifyUserPassword(User user, UserPasswordRequestDto requestDto) {
        User targetUser = findUser(user.getUserId());
        if (targetUser != null) {
            if (passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
                System.out.println("비밀번호 대조 결과 일치");
                targetUser.modifyPassword(passwordEncoder.encode(requestDto.getModifyPassword()));
            } else {
                System.out.println("비밀번호가 틀립니다.");
            }
        } else {
            throw new IllegalArgumentException("해당 사용자는 존재하지 않습니다.");
        }
    }

    public FindIDResponseDto findID(FindIDRequestDto requestDto) {
        User targetUser = userRepository.findByEmail(requestDto.getEmail()).orElseThrow(() ->
                new IllegalArgumentException("해당 이메일로 생성된 아이디는 존재하지 않습니다.")
        );
        if(targetUser.getUsername().equals(requestDto.getUsername())){
            return new FindIDResponseDto(targetUser);
        }
        else
            throw new IllegalArgumentException("사용자 명과 일치하지 않습니다.");
    }
//    @Transactional
//    public void findPW(FindPWRequestDto requestDto) {
//        User targetUser = findUser(requestDto.getUserId());
//        if (targetUser != null) {
//            if(targetUser.getUsername().equals(requestDto.getUsername()) && targetUser.getEmail().equals(requestDto.getEmail())){
//                String tempPW = getTempPassword();
//                targetUser.modifyPassword(passwordEncoder.encode(tempPW));//임시 비밀번호 업데이트
//                mailSend(requestDto,tempPW);
//            }    else
//                throw new IllegalArgumentException("사용자명 또는 이메일이 일치하지 않습니다.");
//
//        }
//    }

//    private void mailSend(FindPWRequestDto requestDto, String tempPW) {
//        System.out.println("메일 전송 완료. 변경된 비밀번호 :"+tempPW);
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(requestDto.getEmail());
//        message.setSubject(requestDto.getUsername()+"님의 Trillion 계정 임시 비밀번호 발급");
//        message.setText("안녕하세요? Trillion에서 "+requestDto.getUsername()+"님의 임시비밀번호를 안내드립니다.\n 임시 비밀번호 :");
//        message.setFrom("Admin@Trillion.com");
//        message.setReplyTo("Admin@Trillion.com");
//        System.out.println(message);
//        mailSender.send(message);
//    }
//
//    //랜덤함수로 임시비밀번호 구문 만들기
//    public String getTempPassword(){
//        char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
//                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
//
//        String str = "";
//
//        // 문자 배열 길이의 값을 랜덤으로 10개를 뽑아 구문을 작성함
//        int idx = 0;
//        for (int i = 0; i < 10; i++) {
//            idx = (int) (charSet.length * Math.random());
//            str += charSet[idx];
//        }
//        return str;
//    }
}