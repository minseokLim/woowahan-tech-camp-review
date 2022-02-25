package com.minseoklim.woowahantechcampreview.common.dto;

import java.io.File;
import java.util.Objects;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import lombok.Builder;

public class EmailDto {
    private static final Pattern EMAIL_REGEX = Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");

    private final String fromAddress;
    private final String toAddress;
    private final String subject;
    private final String text;
    private final boolean html;
    private final String attachedFileName;
    private final File attachedFile;

    @Builder
    private EmailDto(
        final String fromAddress,
        final String toAddress,
        final String subject,
        final String text,
        final boolean html,
        final String attachedFileName,
        final File attachedFile
    ) {
        this.fromAddress = fromAddress;
        this.toAddress = toAddress;
        this.subject = Objects.requireNonNull(subject);
        this.text = Objects.requireNonNull(text);
        this.html = html;
        this.attachedFileName = attachedFileName;
        this.attachedFile = attachedFile;

        validate();
    }

    private void validate() {
        if (!EMAIL_REGEX.matcher(fromAddress).matches() || !EMAIL_REGEX.matcher(toAddress).matches()) {
            throw new IllegalArgumentException("이메일 형식이 올바르지 않습니다.");
        }
        if ((attachedFileName != null) ^ (attachedFile != null)) {
            throw new IllegalArgumentException("첨부파일명과 첨부파일은 함께 입력되어야 합니다.");
        }
    }

    public MimeMessage toMimeMessage(final JavaMailSender javaMailSender) {
        final MimeMessage message = javaMailSender.createMimeMessage();

        try {
            applyFieldsToMimeMessage(message);
        } catch (final MessagingException exception) {
            throw new RuntimeException(exception);
        }

        return message;
    }

    private void applyFieldsToMimeMessage(final MimeMessage message) throws MessagingException {
        final boolean multipart = attachedFileName != null && attachedFile != null;

        final MimeMessageHelper messageHelper = new MimeMessageHelper(message, multipart);
        messageHelper.setFrom(fromAddress);
        messageHelper.setTo(toAddress);
        messageHelper.setSubject(subject);
        messageHelper.setText(text, html);

        if (multipart) {
            messageHelper.addAttachment(attachedFileName, attachedFile);
        }
    }
}
