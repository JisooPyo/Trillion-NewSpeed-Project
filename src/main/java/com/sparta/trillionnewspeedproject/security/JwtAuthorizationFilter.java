package com.sparta.trillionnewspeedproject.security;



import com.sparta.trillionnewspeedproject.jwt.JwtUtil;
import com.sparta.trillionnewspeedproject.security.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


//OncePerRequestFilter - 한 번의 요청에 대해 한번만 필터링을 수행
@Slf4j(topic = "JWT 검증 및 인가")
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    //jwtUtil과 UserDetailsServiceImpl을 의존성으로 주입
    //jwtUtil : JWT토큰 생성, 검증 등
    //UserDetailsServiceImpl : 사용자 정보 제공
    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }


    //필터링 작업을 수행하는 메서드
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {

        //JWT토큰 추출
        String tokenValue = jwtUtil.getJwtFromHeader(req);

        //토큰이 유효한지 검증
        if (StringUtils.hasText(tokenValue)) {
            if (!jwtUtil.validateToken(tokenValue)) {
                log.error("Token Error");
                return;
            }
            //토큰이 유효하면 사용자 정보를 추출
            Claims info = jwtUtil.getUserInfoFromToken(tokenValue);
            try {
                //setAuthentication을 통해 인증처리(인증 객체 생성, SecurityContext생성)
                setAuthentication(info.getSubject());
            } catch (Exception e) {
                log.error(e.getMessage());
                return;
            }
        }
        //다음필터로 요청을 전달
        filterChain.doFilter(req, res);
    }

    // 인증 처리 메서드
    public void setAuthentication(String username) {

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}