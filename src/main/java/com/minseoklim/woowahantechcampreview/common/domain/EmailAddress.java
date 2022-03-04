package com.minseoklim.woowahantechcampreview.common.domain;

import java.util.regex.Pattern;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@Getter
public class EmailAddress {
    public static final String ERR_MSG = "이메일 형식이 올바르지 않습니다.";
    private static final Pattern PATTERN = Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");

    private final String value;

    public EmailAddress(final String emailAddress) {
        validate(emailAddress);
        this.value = emailAddress;
    }

    public static void validate(final String emailAddress) {
        if (!PATTERN.matcher(emailAddress).matches()) {
            throw new IllegalArgumentException(ERR_MSG);
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
