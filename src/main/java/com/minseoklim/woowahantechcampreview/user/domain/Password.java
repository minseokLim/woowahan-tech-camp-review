package com.minseoklim.woowahantechcampreview.user.domain;

import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Password {
    public static final String REGEX = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z0-9]{8,20}$";
    public static final String ERR_MSG = "비밀번호는 8~20자, 최소 하나의 영문자 및 하나의 숫자로 구성되어야 합니다.";
    private static final Pattern PATTERN = Pattern.compile(REGEX);

    @Column(nullable = false, name = "password")
    private String encodedPassword;

    public Password(final String rawPassword, final PasswordEncoder passwordEncoder) {
        if (!PATTERN.matcher(rawPassword).matches()) {
            throw new IllegalArgumentException(ERR_MSG);
        }
        this.encodedPassword = passwordEncoder.encode(rawPassword);
    }
}
