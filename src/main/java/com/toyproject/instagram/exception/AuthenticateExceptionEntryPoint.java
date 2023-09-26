package com.toyproject.instagram.exception;

import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// security exception발생시 예외 낚아챔
@Component
public class AuthenticateExceptionEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error", getErrorMessage(authException));

        JsonMapper jsonMapper = new JsonMapper();
        // jackson라이브러리로 맵을 제이슨문자열로 변환
        String responseJson = jsonMapper.writeValueAsString(errorMap);

        response.getWriter().println(responseJson);
    }

    private  String getErrorMessage(AuthenticationException authException) {
        if(authException.getClass() == BadCredentialsException.class) {
            return "잘못됨";
        } else if (authException.getClass() == UsernameNotFoundException.class) {
            return "잘못됨";
        } else if (authException.getClass() == AccountExpiredException.class) {
            return "사용자 정보 만료됨";
        } else if (authException.getClass() == CredentialsExpiredException.class) {
            return "인증서 만료";
        } else if (authException.getClass() == DisabledException.class) {
            return "비활성화됨";
        } else if (authException.getClass() == LockedException.class) {
            return "암호 5트 이상";
        } else {
            return "오류";
        }
    }
}
