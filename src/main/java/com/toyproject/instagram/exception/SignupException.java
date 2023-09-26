package com.toyproject.instagram.exception;

import lombok.Data;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

@Getter
public class SignupException extends  RuntimeException{

    private Map<String, String> errMap = new HashMap<>();

    public SignupException(Map<String, String> errMap) {
        super("회원가입 유효성검사 오류");
        this.errMap = errMap;
        errMap.forEach((k, v) -> {
            System.out.println(k+ ":" + v);
        });
    }
}
