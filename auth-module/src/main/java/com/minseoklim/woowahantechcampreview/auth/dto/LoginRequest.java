package com.minseoklim.woowahantechcampreview.auth.dto;

import javax.validation.constraints.NotBlank;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import lombok.Getter;

@Getter
public class LoginRequest {
    @NotBlank(message = "아이디를 입력해주세요.")
    private String loginId;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    public Authentication toAuthentication() {
        return new UsernamePasswordAuthenticationToken(loginId, password);
    }
}
