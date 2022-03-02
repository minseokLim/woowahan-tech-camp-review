package com.minseoklim.woowahantechcampreview.user.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.Getter;

@Getter
public class ResetPasswordRequest {
    @NotBlank(message = "token은 필수 값입니다.")
    private String token;

    @Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z0-9]{8,20}$",
        message = "비밀번호는 8~20자, 최소 하나의 영문자 및 하나의 숫자로 구성되어야 합니다."
    )
    private String password;

    public String getEncodedPassword(final PasswordEncoder passwordEncoder) {
        return passwordEncoder.encode(password);
    }
}
