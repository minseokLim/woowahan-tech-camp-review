package com.minseoklim.woowahantechcampreview.user.application;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.minseoklim.woowahantechcampreview.common.dto.EmailDto;
import com.minseoklim.woowahantechcampreview.common.exception.NotFoundException;
import com.minseoklim.woowahantechcampreview.common.util.EmailSender;
import com.minseoklim.woowahantechcampreview.user.domain.ResetPasswordToken;
import com.minseoklim.woowahantechcampreview.user.domain.ResetPasswordTokenRepository;
import com.minseoklim.woowahantechcampreview.user.domain.User;
import com.minseoklim.woowahantechcampreview.user.domain.UserRepository;
import com.minseoklim.woowahantechcampreview.user.dto.ResetPasswordEmailRequest;
import com.minseoklim.woowahantechcampreview.user.dto.ResetPasswordRequest;

@Service
@Transactional
public class ResetPasswordService {
    private final ResetPasswordTokenRepository resetPasswordTokenRepository;
    private final UserRepository userRepository;
    private final EmailSender emailSender;
    private final PasswordEncoder passwordEncoder;

    @Value("${custom.reset-password.token-validity-in-milliseconds}")
    private long tokenValidityInMilliseconds;
    @Value("${custom.email.default-from-address}")
    private String defaultFromAddress;
    @Value("${custom.reset-password.email-subject}")
    private String resetPasswordEmailSubject;
    @Value("${custom.reset-password.email-text-format}")
    private String resetPasswordEmailTextFormat;

    public ResetPasswordService(
        final ResetPasswordTokenRepository resetPasswordTokenRepository,
        final UserRepository userRepository,
        final EmailSender emailSender,
        final PasswordEncoder passwordEncoder
    ) {
        this.resetPasswordTokenRepository = resetPasswordTokenRepository;
        this.userRepository = userRepository;
        this.emailSender = emailSender;
        this.passwordEncoder = passwordEncoder;
    }

    public void sendEmailToResetPassword(final ResetPasswordEmailRequest emailRequest) {
        final User user =
            userRepository.findByLoginIdAndEmailAndDeleted(emailRequest.getLoginId(), emailRequest.getEmail(), false)
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        final ResetPasswordToken token = createResetPasswordToken(user.getId());
        final String uriInEmail = token.applyTokenToUriToResetPassword(emailRequest.getUriToResetPassword());

        sendEmailToResetPassword(emailRequest.getEmail(), uriInEmail);
    }

    public void resetPassword(final ResetPasswordRequest resetPasswordRequest) {
        final Long userId = getResetPasswordToken(resetPasswordRequest.getToken()).getUserId();
        final User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        user.changePassword(resetPasswordRequest.getEncodedPassword(passwordEncoder));
    }

    public ResetPasswordToken getResetPasswordToken(final String token) {
        return resetPasswordTokenRepository.findById(token)
            .orElseThrow(() -> new NotFoundException("유효한 토큰을 찾을 수 없습니다."));
    }

    private ResetPasswordToken createResetPasswordToken(final Long userId) {
        return resetPasswordTokenRepository.save(new ResetPasswordToken(userId, tokenValidityInMilliseconds));
    }

    private void sendEmailToResetPassword(final String toAddress, final String uriInEmail) {
        final EmailDto emailDto = EmailDto.builder()
            .fromAddress(defaultFromAddress)
            .toAddress(toAddress)
            .subject(resetPasswordEmailSubject)
            .text(
                String.format(
                    resetPasswordEmailTextFormat,
                    Duration.ofMillis(tokenValidityInMilliseconds).toHours(),
                    uriInEmail
                )
            )
            .build();

        emailSender.send(emailDto);
    }
}
