package com.minseoklim.woowahantechcampreview.auth.dto;

import javax.validation.constraints.NotBlank;

import lombok.Getter;

import com.minseoklim.woowahantechcampreview.user.domain.Role;

@Getter
public class RoleRequest {
    @NotBlank(message = "필수 입력값입니다.")
    private String role;

    public Role toRole() {
        return Role.valueOf(role);
    }
}
