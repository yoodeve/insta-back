package com.toyproject.instagram.service;

import com.toyproject.instagram.dto.SigninReqDto;
import com.toyproject.instagram.dto.SignupReqDto;
import com.toyproject.instagram.entity.User;
import com.toyproject.instagram.exception.JwtException;
import com.toyproject.instagram.exception.SignupException;
import com.toyproject.instagram.repository.UserMapper;
import com.toyproject.instagram.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    public void signupUser(SignupReqDto signupReqDto) {
        User user = signupReqDto.toUserEntity(passwordEncoder);
        String emailPattern = "^[a-zA-Z0-9]+@[0-9a-zA-Z]+\\.[a-z]*$";
        String phonePattern = "^[0-9]{11}+$";

        Pattern emailRegex = Pattern.compile(emailPattern);
        Pattern phoneRegex = Pattern.compile(phonePattern);

        Matcher emailMatcher = emailRegex.matcher(signupReqDto.getPhoneOrEmail());
        Matcher phoneMatcher = phoneRegex.matcher(signupReqDto.getPhoneOrEmail());

        if(emailMatcher.matches()) {
            user.setEmail(signupReqDto.getPhoneOrEmail());
        }
        if(phoneMatcher.matches()) {
            user.setPhone(signupReqDto.getPhoneOrEmail());
        }
        checkDuplicated(user);

        userMapper.saveUser(user);
    }

    private void checkDuplicated (User user) {
        if(StringUtils.hasText(user.getPhone())) {
            if(userMapper.findUserByPhone(user.getPhone()) != null) {
                Map<String, String> errMap = new HashMap<>();
                errMap.put("phone", "이미 사용중인 아이디입니다.");
                throw new SignupException(errMap);
            }
        }

        if(StringUtils.hasText(user.getEmail())) {
            if (userMapper.findUserByEmail(user.getEmail()) != null) {
                Map<String, String> errMap = new HashMap<>();
                errMap.put("email", "이미 사용중인 아이디입니다.");
                throw new SignupException(errMap);
            }
        }

        if(StringUtils.hasText(user.getUsername())) {
            if(userMapper.findUserByUsername(user.getEmail()) != null) {
                Map<String, String> errMap = new HashMap<>();
                errMap.put("username", "이미 사용중인 아이디입니다.");
                throw new SignupException(errMap);
            }
        }
    }

    public String signinUser(SigninReqDto signinReqDto) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(signinReqDto.getPhoneOrEmailOrUsername(), signinReqDto.getLoginPassword());
        // jwt:안에 json타입으로 정보가 들어있는 암호화된 토큰
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        String accessToken = jwtTokenProvider.generateAccessToken(authentication);
        return accessToken;
    }

    public Boolean authenticate (String token) {
        String accessToken = jwtTokenProvider.convertToken(token);
        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new JwtException("사용자 정보가 만료되었으니 다시 로그인 바랍니다.");
        }
        return true;
    }

}
