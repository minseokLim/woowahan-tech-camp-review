package com.minseoklim.woowahantechcampreview.user.dto;

import com.minseoklim.woowahantechcampreview.user.domain.User;

import lombok.Getter;

@Getter
public class UserResponse {
    private final Long id;

    private final String loginId;

    private final String nickName;

    private final String email;

    private final boolean deleted;

    private UserResponse(
        final Long id,
        final String loginId,
        final String nickName,
        final String email,
        final boolean deleted
    ) {
        this.id = id;
        this.loginId = loginId;
        this.nickName = nickName;
        this.email = email;
        this.deleted = deleted;
    }

    public static UserResponse of(final User user) {
        return new UserResponse(user.getId(), user.getLoginId(), user.getNickName(), user.getEmail(), user.isDeleted());
    }
}
