package com.minseoklim.woowahantechcampreview.user.domain;

import java.util.regex.Pattern;

import javax.persistence.Embeddable;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginId {
    public static final String REGEX = "^[a-z0-9]{5,20}$";
    public static final String ERR_MSG = "로그인 ID에는 5~20자의 영문 소문자, 숫자만 사용 가능합니다.";
    private static final Pattern PATTERN = Pattern.compile(REGEX);

    private String loginId;

    public LoginId(final String loginId) {
        if (!PATTERN.matcher(loginId).matches()) {
            throw new IllegalArgumentException(ERR_MSG);
        }
        this.loginId = loginId;
    }

    @Override
    public String toString() {
        return loginId;
    }
}
