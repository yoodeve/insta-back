package com.toyproject.instagram.dto;

import com.toyproject.instagram.entity.User;
import lombok.Data;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class SignupReqDto {
//    @NotBlank(message = "전화번호 또는 이메일은 공백일 수 없습니다.")
    @Pattern(regexp = "^[a-zA-Z0-9]+@[0-9a-zA-Z]+\\.[a-z]*$|^[0-9]{11}+$"
            , message = "이메일 형식 또는 전화번호 형식이 아닙니다.")
    private String phoneOrEmail;


    @Pattern(regexp = "^[가-힣]{2,6}$"
            ,message = "이름은 공백일 수 없습니다.")
    private String name;

    @Pattern(regexp = "^(?=.*[a-z])[a-z0-9_.]*$"
            ,message = "사용할 수 없는 아이디입니다. 다른 아이디를 입력하세요.")
    private String username;

    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9]).{8,25}$"
            , message = "비밀번호는 영문, 숫자로 8~25자리 입력해주세요.")
    private String password;

    public User toUserEntity(BCryptPasswordEncoder passwordEncoder) {
        return User.builder()
                .email(phoneOrEmail)
                .name(name)
                .username(username)
                .password(passwordEncoder.encode(password))
                .build();
    }
}
