package com.minseoklim.woowahantechcampreview.auth.domain;

import java.util.concurrent.TimeUnit;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@RedisHash("refreshToken")
public class RefreshToken {
    @Id
    private String userId;

    private String value;

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private Long timeout;

    public RefreshToken(final String userId, final String value, final Long timeout) {
        this.userId = userId;
        this.value = value;
        this.timeout = timeout;
    }

    public boolean hasSameValue(final String value) {
        return this.value.equals(value);
    }
}
