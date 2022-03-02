package com.minseoklim.woowahantechcampreview.user.domain;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.util.Base64Utils;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.Getter;

@RedisHash("resetPasswordToken")
@Getter
public class ResetPasswordToken {
    static final String QUERY_PARAM_NAME = "token";

    @Id
    private String token;

    private Long userId;

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private Long timeout;

    public ResetPasswordToken(final Long userId, final Long timeout) {
        this.token = generateRandomToken();
        this.userId = userId;
        this.timeout = timeout;
    }

    // TODO: 테스트가 어려운 코드. 밖으로 빼야하나?
    private static String generateRandomToken() {
        final byte[] randomBytes = UUID.randomUUID().toString().getBytes();
        return Base64Utils.encodeToString(randomBytes);
    }

    public String applyTokenToUriToResetPassword(final String uri) {
        return UriComponentsBuilder.fromUriString(uri)
            .queryParam(QUERY_PARAM_NAME, token)
            .build()
            .toUriString();
    }
}
