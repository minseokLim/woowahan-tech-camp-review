package com.minseoklim.woowahantechcampreview.auth.dto;

import javax.validation.constraints.NotBlank;

import com.minseoklim.woowahantechcampreview.user.domain.Role;

import lombok.Getter;

@Getter
public class RoleRequest {
    @NotBlank(message = "필수 입력값입니다.")
    private String role;

    public Role toRole() {
        return Role.valueOf(role);
    }
}
