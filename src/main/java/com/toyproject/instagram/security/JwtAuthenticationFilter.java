package com.toyproject.instagram.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilter {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String token = httpServletRequest.getHeader("Authorization");
        String jwtToken = jwtTokenProvider.convertToken(token);
        String uri = httpServletRequest.getRequestURI();

        if (!uri.startsWith("api/v1/auth") && jwtTokenProvider.validateToken(jwtToken)) { // Bearer 제거
            Authentication authentication = jwtTokenProvider.getAuthentication(jwtToken);
            // 시큐리티 인증 상태에 Authentication객체가 존재하면 인증된 상태임
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }
}
