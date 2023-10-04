package com.toyproject.instagram.security;

import com.toyproject.instagram.entity.User;
import com.toyproject.instagram.repository.UserMapper;
import com.toyproject.instagram.service.PrincipalDetailService;
import com.toyproject.instagram.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Date;

// jwt토큰 관리 로직
@Component
public class JwtTokenProvider {
    private final Key key;
    private final PrincipalDetailService principalDetailService;

    // @autowired는 IoC컨테이너에서 객체를 자동 주입
    // @value는 어플리케이션.yml에서 변수 데이터를 자동 주입
    public JwtTokenProvider(@Value("${jwt.secret}") String secret, @Autowired PrincipalDetailService principalDetailService) {
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.principalDetailService = principalDetailService;
    }

    // jwt 토큰을 생성하는 로직
    public String generateAccessToken(Authentication authentication) {
        String accessToken = null;
        PrincipalUser principalUser = (PrincipalUser) authentication.getPrincipal();

        Date tokenExpiresDate = new Date(new Date().getTime() + (1000 * 60 * 60 * 24)); // 하루
        accessToken = Jwts.builder()
                .setSubject("AccessToken")
                .claim("username", principalUser.getUsername())
                .setExpiration(tokenExpiresDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return accessToken;
    }

    public Boolean validateToken(String token) {
        // 토큰 유효성검사
        try {
        Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public String convertToken(String bearerToken) {
        String type = "Bearer ";
        // null, blank 체크 동시에 : hasText
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(type)) {
            return bearerToken.substring(type.length());
        }
        return "";
    }

    public Authentication getAuthentication(String accessToken) {
        Authentication authentication = null;
        // jwt토큰 디코딩
        String username = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken)
                .getBody()
                .get("username")
                .toString();

        PrincipalUser user = (PrincipalUser) principalDetailService.loadUserByUsername(username);

        System.out.println("유저유저유저" + username);
//        PrincipalUser principalUser = new PrincipalUser();
        authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        return authentication;
    }
}
