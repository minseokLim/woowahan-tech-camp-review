package com.minseoklim.woowahantechcampreview.common.domain;

import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Embeddable
@EqualsAndHashCode
@Getter
public class EmailAddress {
    public static final String ERR_MSG = "이메일 형식이 올바르지 않습니다.";
    private static final Pattern PATTERN = Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");

    @Column(nullable = false, name = "email")
    private String value;

    public EmailAddress() {
    }

    public EmailAddress(final String emailAddress) {
        if (!PATTERN.matcher(emailAddress).matches()) {
            throw new IllegalArgumentException(ERR_MSG);
        }
        this.value = emailAddress;
    }

    @Override
    public String toString() {
        return value;
    }
}
