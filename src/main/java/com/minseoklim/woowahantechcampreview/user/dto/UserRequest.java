package com.minseoklim.woowahantechcampreview.user.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.Getter;

import com.minseoklim.woowahantechcampreview.user.domain.User;

@Getter
public class UserRequest {
    @Pattern(regexp = "^[a-z0-9]{5,20}$", message = "로그인 ID에는 5~20자의 영문 소문자, 숫자만 사용 가능합니다.")
    private String loginId;

    @Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z0-9]{8,20}$",
        message = "비밀번호는 8~20자, 최소 하나의 영문자 및 하나의 숫자로 구성되어야 합니다."
    )
    private String password;

    @Pattern(regexp = "^[a-zA-Z가-힇0-9]{1,10}$", message = "닉네임에는 1~10자의 영문자, 한글, 숫자만 사용 가능합니다.")
    private String nickName;

    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    public User toEntity(final PasswordEncoder passwordEncoder) {
        return new User(loginId, passwordEncoder.encode(password), nickName, email);
    }
}
