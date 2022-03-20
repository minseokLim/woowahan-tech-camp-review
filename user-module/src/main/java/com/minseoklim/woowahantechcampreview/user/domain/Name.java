package com.minseoklim.woowahantechcampreview.user.domain;

import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Name {
    public static final String REGEX = "^[a-zA-Z가-힇0-9]{1,10}$";
    public static final String ERR_MSG = "이름에는 1~10자의 영문자, 한글, 숫자만 사용 가능합니다.";
    private static final Pattern PATTERN = Pattern.compile(REGEX);

    @Column(nullable = false, name = "name")
    private String value;

    Name(final String name) {
        if (!PATTERN.matcher(name).matches()) {
            throw new IllegalArgumentException(ERR_MSG);
        }
        this.value = name;
    }

    @Override
    public String toString() {
        return value;
    }
}
