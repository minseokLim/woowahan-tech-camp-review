package com.minseoklim.woowahantechcampreview.auth.dto;

import javax.validation.constraints.NotBlank;

import lombok.Getter;

@Getter
public class TokenRequest {
    @NotBlank(message = "필수 입력값입니다.")
    private String accessToken;

    @NotBlank(message = "필수 입력값입니다.")
    private String refreshToken;
}
