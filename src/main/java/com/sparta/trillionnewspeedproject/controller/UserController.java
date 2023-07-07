package com.sparta.trillionnewspeedproject.controller;

import com.sparta.trillionnewspeedproject.dto.*;
import com.sparta.trillionnewspeedproject.security.UserDetailsImpl;
import com.sparta.trillionnewspeedproject.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    //get요청에 대해 로그인페이지 html 전달
    @GetMapping("/user/login-page")
    public String loginPage() {
        return "login";
    }

    //get요청에 대해 회원가입페이지 html 전달
    @GetMapping("/user/signup")
    public String signupPage() {
        return "signup";
    }

    @PostMapping("/user/signup")
    @ResponseBody
    public ResponseEntity<ApiResponseDto> signup(@Valid @RequestBody SignupRequestDto requestDto, BindingResult bindingResult, HttpServletResponse response) {
        // Validation 예외처리 - signupRequestDto에서 설정한 글자수, 문자규칙(a~z,A~Z,0~9)에 위배되는 경우 fieldError 리스트에 내용이 추가됨
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        StringBuilder errorMessage = new StringBuilder();

        //1건 이상 Validation 관련 에러가 발견된 경우 - 에러메시지(1개~ 여러 개)를 message응답으로 client에 전달
        if (fieldErrors.size() > 0) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
                errorMessage.append(fieldError.getDefaultMessage()).append("\n ");
            }
            response.setStatus(400);
            return ResponseEntity.status(400).body(new ApiResponseDto(errorMessage.toString(), response.getStatus()));
        }

        try {
            return ResponseEntity.status(response.getStatus()).body(userService.signup(requestDto, response));
        } catch (IllegalArgumentException ex) { //signup 메서드에서 오류발생 시, 에러메시지와 상태값 리턴
            response.setStatus(400);
            return ResponseEntity.status(400).body(new ApiResponseDto(ex.getMessage(), response.getStatus()));
        }
    }

    // 회원 관련 정보 받기(프로필 조회)
    @GetMapping("/user/profile")
    @ResponseBody
    public ResponseEntity<ApiResponseDto> getUserProfile(@AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletResponse response) {
        try {
            return ResponseEntity.status(200).body(userService.getUserProfile(userDetails.getUser(), response));
        }catch (IllegalArgumentException ex){
            response.setStatus(400);
            return ResponseEntity.status(400).body(new ApiResponseDto(ex.getMessage(), response.getStatus()));
        }

    }

    // 회원 관련 정보 변경(프로필 변경)
    @PutMapping("/user/profile")
    @ResponseBody
    public ResponseEntity<ApiResponseDto> modifyUserProfile(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody UserProfileRequestDto requestDto, HttpServletResponse response) {
        try {
            return ResponseEntity.status(201).body(userService.modifyUserProfile(userDetails.getUser(), requestDto, response));
        } catch (IllegalArgumentException ex) {
            response.setStatus(400);
            return ResponseEntity.status(400).body(new ApiResponseDto(ex.getMessage(), response.getStatus()));
        }
    }

    // 비밀번호 변경
    @PutMapping("/user/profile/pw")
    @ResponseBody
    public ResponseEntity<ApiResponseDto> modifyUserPassword(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody UserPasswordRequestDto requestDto, HttpServletResponse response) {
        try {
            return ResponseEntity.status(201).body(userService.modifyUserPassword(userDetails.getUser(), requestDto, response));
        } catch (IllegalArgumentException ex) {
            response.setStatus(400);
            return ResponseEntity.status(400).body(new ApiResponseDto(ex.getMessage(), response.getStatus()));
        }
    }

    //아이디 찾기
    @PostMapping("/user/findid")
    @ResponseBody
    public ResponseEntity<ApiResponseDto> findID(@RequestBody FindIDRequestDto requestDto, HttpServletResponse response) {
        try {
            return ResponseEntity.status(200).body(userService.findID(requestDto, response));
        } catch (IllegalArgumentException ex) {
            response.setStatus(400);
            return ResponseEntity.status(400).body(new ApiResponseDto(ex.getMessage(), response.getStatus()));
        }
    }

    //비밀번호 찾기
    @PostMapping("/user/findpw")
    @ResponseBody
    public ResponseEntity<ApiResponseDto> findPW(@RequestBody FindPWRequestDto requestDto, HttpServletResponse response) {
        try {
            return ResponseEntity.status(200).body(userService.findPW(requestDto, response));
        } catch (IllegalArgumentException ex) {
            response.setStatus(400);
            return ResponseEntity.status(400).body(new ApiResponseDto(ex.getMessage(), response.getStatus()));
        }
    }

    //입력한 이메일정보를 받아 인증번호를 발급
    @PostMapping("/user/signup/authentication")
    @ResponseBody
    public ResponseEntity<ApiResponseDto> Authentication(@RequestBody AuthenticationRequestDto requestDto, HttpServletResponse response){
        try {
            return ResponseEntity.status(200).body(userService.Authentication(requestDto, response));
        }
        catch (IllegalArgumentException ex) {
            response.setStatus(400);
            return ResponseEntity.status(400).body(new ApiResponseDto(ex.getMessage(), response.getStatus()));
        }
    }
    // 입력한 이메일과 인증번호를 받아 DB 내 인증번호와 대조 및 만료여부 검증
    @PostMapping("/user/signup/verification")
    @ResponseBody
    public ResponseEntity<ApiResponseDto> Verification(@RequestBody VerificationRequestDto requestDto, HttpServletResponse response){
        try {
            return ResponseEntity.status(200).body(userService.Verification(requestDto, response));
        }
        catch (IllegalArgumentException ex) {
            response.setStatus(400);
            return ResponseEntity.status(400).body(new ApiResponseDto(ex.getMessage(), response.getStatus()));
        }
    }
}