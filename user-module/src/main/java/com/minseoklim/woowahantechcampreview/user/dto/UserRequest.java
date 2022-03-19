package com.minseoklim.woowahantechcampreview.user.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.Getter;

import com.minseoklim.woowahantechcampreview.common.domain.EmailAddress;
import com.minseoklim.woowahantechcampreview.user.domain.LoginId;
import com.minseoklim.woowahantechcampreview.user.domain.Name;
import com.minseoklim.woowahantechcampreview.user.domain.Password;
import com.minseoklim.woowahantechcampreview.user.domain.User;

@Getter
public class UserRequest {
    @Pattern(regexp = LoginId.REGEX, message = LoginId.ERR_MSG)
    private String loginId;

    @Pattern(regexp = Password.REGEX, message = Password.ERR_MSG)
    private String password;

    @Pattern(regexp = Name.REGEX, message = Name.ERR_MSG)
    private String nickName;

    @Email(message = EmailAddress.ERR_MSG)
    private String email;

    public User toEntity(final PasswordEncoder passwordEncoder) {
        return new User(loginId, password, passwordEncoder, nickName, email);
    }
}
