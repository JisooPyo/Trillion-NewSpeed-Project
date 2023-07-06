package com.sparta.trillionnewspeedproject.config;


import com.sparta.trillionnewspeedproject.security.JwtAuthenticationFilter;
import com.sparta.trillionnewspeedproject.security.JwtAuthorizationFilter;
import com.sparta.trillionnewspeedproject.jwt.JwtUtil;
import com.sparta.trillionnewspeedproject.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // Spring Security 지원을 가능하게 함
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationConfiguration authenticationConfiguration;

    //비밀번호 암호화를 수행하는 메서드
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //인증을 수행하기위한 객체인 AuthenticationManager을 빈으로 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    //JWT인증을 수행하는 JwtAuthenticationFilter를 빈으로 등록
    //jwtUtil을 주입하고 authenticationManager를 설정하여 필터가 인증을 수행할 수 있도록 함
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtUtil);
        filter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
        return filter;
    }

    //JWT 인가 처리를 수행하는 JwtAuthorizationFilter를 빈으로 등록
    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(jwtUtil, userDetailsService);
    }


    //SecurityFilterChain을 빈으로 등록
    //HttpSecurity를 매개변수로 받아 필터 체인 구성
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // CSRF 설정
        http.csrf((csrf) -> csrf.disable());//CSRF security 미사용

        // 기본 설정인 Session 방식은 사용하지 않고 JWT 방식을 사용하기 위한 설정
        http.sessionManagement((sessionManagement) ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        //HTTP요청에 대한 권한 설정 수행 (접근권한 허가경로 외에는 인증필요하도록 설정)
        http.authorizeHttpRequests((authorizeHttpRequests) ->
                authorizeHttpRequests
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() // resources 접근 허용 설정
                        .requestMatchers("/").permitAll() // 기본 페이지 요청 허가
                        .requestMatchers("/api/user/signup").permitAll() // '/api/user/signup' 요청은 모든 사용자에게 허용
                        .requestMatchers("/api/user/signup/**").permitAll() //'/api/user/signup/'로 시작하는 요청은 모든 사용자에게 허용
                        .requestMatchers("/api/user/login").permitAll() // '/api/user/login' 요청은 모든 사용자에게 허용
                        .requestMatchers("/api/user/login-page").permitAll()
                        .requestMatchers("/api/user/findid").permitAll() //아이디 찾기, 비밀번호 찾기 요청 모든 사용자에게 허용
                        .requestMatchers("/api/user/findpw").permitAll()
                        .anyRequest().authenticated() // 그 외 모든 요청 인증처리
        );

        //인증 필요한 요청을 받았을때 인증정보가 없는 경우, formLogin이용 페이지이동하도록 설정(현재 html이 없으므로 disable()해놓은 상태)
        http.formLogin((formLogin) ->
                        formLogin.disable()
//                        .loginPage("/api/user/login").permitAll()

        );

        // 필터 관리
        //인가 ->인증->usernamePassword인증필터 순 동작하도록 순서설정
        http.addFilterBefore(jwtAuthorizationFilter(), JwtAuthenticationFilter.class);
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}