package com.example.userservice.security;

import com.example.userservice.dto.UserDto;
import com.example.userservice.service.UserService;
import com.example.userservice.vo.RequestLogin;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.ArrayList;
@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
//    private UserService userService;
    public AuthenticationFilter(AuthenticationManager authenticationManager
                                ){
        super(authenticationManager);
//        this.userService = userService;
    }
    @Override  // 로그인 시도
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            RequestLogin cred = new ObjectMapper().readValue(request.getInputStream(), RequestLogin.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(cred.getEmail(),cred.getPassword(),new ArrayList<>())
            );
        }catch (IOException exception){
            throw new RuntimeException(exception);
        }
    }

    @Override // 로그인 성공시 토큰 만드는 작업 수행
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
//        String username = ((User)authResult.getPrincipal()).getUsername();
//        UserDto userDetails = userService.getUserByUserId(username);
        log.info(((User)authResult.getPrincipal()).getUsername());
    }
}