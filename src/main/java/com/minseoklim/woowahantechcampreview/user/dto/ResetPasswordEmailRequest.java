package com.minseoklim.woowahantechcampreview.user.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.Getter;

import com.minseoklim.woowahantechcampreview.common.domain.EmailAddress;

@Getter
public class ResetPasswordEmailRequest {
    @NotBlank(message = "아이디를 입력해주세요.")
    private String loginId;

    @Email(message = EmailAddress.ERR_MSG)
    private String email;

    @Pattern(
        regexp = "^(?i)(https?)://((\\w|-)+\\.)+\\w+(:\\d+)*(/(\\w|-)+)*(/(\\w|-)+\\.\\w+)?$",
        message = "유효하지 않은 URI 입니다."
    )
    private String uriToResetPassword;
}
