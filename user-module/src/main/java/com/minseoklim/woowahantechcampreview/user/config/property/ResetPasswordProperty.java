package com.minseoklim.woowahantechcampreview.user.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties("custom.reset-password")
@Getter
@Setter
public class ResetPasswordProperty {
    private long tokenValidityInMilliseconds;
    private String emailFromAddress;
    private String emailSubject;
    private String emailTextFormat;
}
