package com.minseoklim.woowahantechcampreview.user.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.minseoklim.woowahantechcampreview.common.domain.EmailAddress;

@Embeddable
@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Email {
    @Column(nullable = false, name = "email")
    private String value;

    public Email(final String emailAddress) {
        EmailAddress.validate(emailAddress);
        this.value = emailAddress;
    }

    @Override
    public String toString() {
        return value;
    }
}
