package com.minseoklim.woowahantechcampreview.user.application;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
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

@Service
@Transactional
public class ResetPasswordService {
    private final ResetPasswordTokenRepository resetPasswordTokenRepository;
    private final UserRepository userRepository;
    private final EmailSender emailSender;

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
        final EmailSender emailSender
    ) {
        this.resetPasswordTokenRepository = resetPasswordTokenRepository;
        this.userRepository = userRepository;
        this.emailSender = emailSender;
    }

    public void sendEmailToResetPassword(final ResetPasswordEmailRequest emailRequest) {
        final User user =
            userRepository.findByLoginIdAndEmailAndDeleted(emailRequest.getLoginId(), emailRequest.getEmail(), false)
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        final ResetPasswordToken token = createResetPasswordToken(user.getId());
        final String uriInEmail = token.applyTokenToUriToResetPassword(emailRequest.getUriToResetPassword());

        sendEmailToResetPassword(emailRequest.getEmail(), uriInEmail);
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
