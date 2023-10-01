package com.toyproject.instagram.controller;

import com.toyproject.instagram.exception.JwtException;
import com.toyproject.instagram.exception.SignupException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(SignupException.class)
    public ResponseEntity<?> signupExceptionHandle (SignupException signupException) {
        return ResponseEntity.badRequest().body(signupException.getErrMap());
    }
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<?> jwtExceptionHandle (JwtException jwtException) {
        return ResponseEntity.badRequest().body(jwtException.getMessage());
    }
}
