package com.toyproject.instagram.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class User {
    private String userId;
    private String email;
    private String phone;
    private String name;
    private String username;
    private String password;
    private String provider;
    private List<Authority> authorities;
}
