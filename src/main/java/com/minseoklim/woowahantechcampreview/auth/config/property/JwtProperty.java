package com.minseoklim.woowahantechcampreview.auth.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties("custom.jwt")
@Getter
@Setter
public class JwtProperty {
    private String secretKey;
    private long accessTokenValidityInMilliseconds;
    private long refreshTokenValidityInMilliseconds;

    public JwtProperty() {
    }

    public JwtProperty(
        final String secretKey,
        final long accessTokenValidityInMilliseconds,
        final long refreshTokenValidityInMilliseconds
    ) {
        this.secretKey = secretKey;
        this.accessTokenValidityInMilliseconds = accessTokenValidityInMilliseconds;
        this.refreshTokenValidityInMilliseconds = refreshTokenValidityInMilliseconds;
    }
}
