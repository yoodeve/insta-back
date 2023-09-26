package com.toyproject.instagram.security;


import com.toyproject.instagram.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class PrincipalUser implements UserDetails {

    private String phoneOremailOrUsername;
    private String password;

    public PrincipalUser(String phoneOremailOrUsername, String password) {
        this.phoneOremailOrUsername = phoneOremailOrUsername;
        this.password = password;
    }
    // 로그인 할 사용자의 권한
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    //
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return phoneOremailOrUsername;
    }
    // 계정만료 -> 사용기간이 정해진 계정
    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    // 계정 잠금 -> 비밀번호 많이 틀리면 등
    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    // 계정 자격 증명 만료 -> 공동인증서, 금융인증서, 휴대폰인증... 만료되면 false
    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    // 계정 활성화 -> 회원가입하고 본인인증까지 되지 않은 경우 false
    @Override
    public boolean isEnabled() {
        return false;
    }
}
