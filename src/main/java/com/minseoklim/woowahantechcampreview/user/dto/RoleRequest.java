package com.minseoklim.woowahantechcampreview.user.dto;

import javax.validation.constraints.NotNull;

import lombok.Getter;

import com.minseoklim.woowahantechcampreview.auth.domain.Role;

@Getter
public class RoleRequest {
    @NotNull(message = "role은 필수 입력값입니다.")
    private Role role;

    public Role toRole() {
        return role;
    }
}
