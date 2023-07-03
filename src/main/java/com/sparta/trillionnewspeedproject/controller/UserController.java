package com.sparta.trillionnewspeedproject.controller;

import com.sparta.trillionnewspeedproject.dto.*;
import com.sparta.trillionnewspeedproject.security.UserDetailsImpl;
import com.sparta.trillionnewspeedproject.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @GetMapping("/user/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/user/signup")
    public String signupPage() {
        return "signup";
    }

    @PostMapping("/user/signup")
    @ResponseBody
    public String signup(@Valid @RequestBody SignupRequestDto requestDto, BindingResult bindingResult) {
        // Validation 예외처리
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if(fieldErrors.size() > 0) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
            }
            return "redirect:/api/user/signup";
        }
        userService.signup(requestDto);
        return "redirect:/api/user/login";
    }

    // 회원 관련 정보 받기(프로필 조회)
    @GetMapping("/user/profile")
    @ResponseBody
    public UserProfileResponseDto getUserProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.getUserProfile(userDetails.getUser());

    }

    // 회원 관련 정보 변경(프로필 변경)
    @PutMapping("/user/profile")
    @ResponseBody
    public UserProfileResponseDto modifyUserProfile(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody UserProfileRequestDto requestDto) {
        return new UserProfileResponseDto(userService.modifyUserProfile(userDetails.getUser(),requestDto));
    }

    // 비밀번호 변경
    @PutMapping("/user/profile/pw")
    @ResponseBody
    public void modifyUserPassword(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody UserPasswordRequestDto requestDto) {
        userService.modifyUserPassword(userDetails.getUser(),requestDto);
    }

//    @PostMapping("/user/findid")
//    @ResponseBody
//    public FindIDResponseDto findID(@RequestBody FindIDRequestDto requestDto){
//        return userService.findID(requestDto);
//    }
//
//    @PostMapping("/user/findpw")
//    @ResponseBody
//    public FindPWResponseDto findPW(@RequestBody FindIDRequestDto requestDto){
//        return userService.findPW(requestDto);
//    }
}