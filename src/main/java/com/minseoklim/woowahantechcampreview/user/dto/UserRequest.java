package com.minseoklim.woowahantechcampreview.user.dto;

import com.minseoklim.woowahantechcampreview.user.domain.User;
import lombok.Getter;

@Getter
public class UserRequest {
    private String loginId;

    private String password;

    private String nickName;

    private String email;

    public User toEntity() {
        return new User(loginId, password, nickName, email);
    }
}
