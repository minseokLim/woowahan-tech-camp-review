package com.minseoklim.woowahantechcampreview.user.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.Getter;

import com.minseoklim.woowahantechcampreview.user.domain.Password;

@Getter
public class ResetPasswordRequest {
    @NotBlank(message = "token은 필수 값입니다.")
    private String token;

    @Pattern(regexp = Password.REGEX, message = Password.ERR_MSG)
    private String password;
}
