package com.minseoklim.woowahantechcampreview.user.application;

import java.time.Duration;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.minseoklim.woowahantechcampreview.common.domain.Email;
import com.minseoklim.woowahantechcampreview.common.exception.NotFoundException;
import com.minseoklim.woowahantechcampreview.common.util.EmailSender;
import com.minseoklim.woowahantechcampreview.user.config.property.ResetPasswordProperty;
import com.minseoklim.woowahantechcampreview.user.domain.ResetPasswordToken;
import com.minseoklim.woowahantechcampreview.user.domain.User;
import com.minseoklim.woowahantechcampreview.user.domain.repository.ResetPasswordTokenRepository;
import com.minseoklim.woowahantechcampreview.user.domain.repository.UserRepository;
import com.minseoklim.woowahantechcampreview.user.dto.ResetPasswordEmailRequest;
import com.minseoklim.woowahantechcampreview.user.dto.ResetPasswordRequest;

@Service
@Transactional
public class ResetPasswordService {
    private final ResetPasswordTokenRepository resetPasswordTokenRepository;
    private final UserRepository userRepository;
    private final EmailSender emailSender;
    private final PasswordEncoder passwordEncoder;
    private final ResetPasswordProperty resetPasswordProperty;

    public ResetPasswordService(
        final ResetPasswordTokenRepository resetPasswordTokenRepository,
        final UserRepository userRepository,
        final EmailSender emailSender,
        final PasswordEncoder passwordEncoder,
        final ResetPasswordProperty resetPasswordProperty
    ) {
        this.resetPasswordTokenRepository = resetPasswordTokenRepository;
        this.userRepository = userRepository;
        this.emailSender = emailSender;
        this.passwordEncoder = passwordEncoder;
        this.resetPasswordProperty = resetPasswordProperty;
    }

    public void sendEmailToResetPassword(final ResetPasswordEmailRequest emailRequest) {
        final User user =
            userRepository.findByLoginIdValueAndEmailValueAndDeleted(
                emailRequest.getLoginId(), emailRequest.getEmail(), false
            ).orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        final ResetPasswordToken token = createResetPasswordToken(user.getId());
        final String uriInEmail = token.applyTokenToUriToResetPassword(emailRequest.getUriToResetPassword());

        sendEmailToResetPassword(emailRequest.getEmail(), uriInEmail);
    }

    public void resetPassword(final ResetPasswordRequest resetPasswordRequest) {
        final Long userId = getResetPasswordToken(resetPasswordRequest.getToken()).getUserId();
        final User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        user.changePassword(resetPasswordRequest.getPassword(), passwordEncoder);
    }

    public ResetPasswordToken getResetPasswordToken(final String token) {
        return resetPasswordTokenRepository.findById(token)
            .orElseThrow(() -> new NotFoundException("유효한 토큰을 찾을 수 없습니다."));
    }

    private ResetPasswordToken createResetPasswordToken(final Long userId) {
        return resetPasswordTokenRepository.save(
            new ResetPasswordToken(userId, resetPasswordProperty.getTokenValidityInMilliseconds())
        );
    }

    private void sendEmailToResetPassword(final String toAddress, final String uriInEmail) {
        final Email email = Email.builder()
            .fromAddress(resetPasswordProperty.getEmailFromAddress())
            .toAddress(toAddress)
            .subject(resetPasswordProperty.getEmailSubject())
            .text(
                String.format(
                    resetPasswordProperty.getEmailTextFormat(),
                    Duration.ofMillis(resetPasswordProperty.getTokenValidityInMilliseconds()).toHours(),
                    uriInEmail
                )
            )
            .build();

        emailSender.send(email);
    }
}
