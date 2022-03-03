package com.minseoklim.woowahantechcampreview.common.domain;

import java.io.File;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import lombok.Builder;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Email {
    private final EmailAddress fromAddress;
    private final EmailAddress toAddress;
    private final EmailContent content;
    private EmailAttachedFile attachedFile;

    @Builder
    private Email(
        final String fromAddress,
        final String toAddress,
        final String subject,
        final String text,
        final boolean html,
        final String attachedFileName,
        final File attachedFile
    ) {
        this.fromAddress = new EmailAddress(fromAddress);
        this.toAddress = new EmailAddress(toAddress);
        this.content = new EmailContent(subject, text, html);

        if (attachedFile != null || attachedFileName != null) {
            this.attachedFile = new EmailAttachedFile(attachedFileName, attachedFile);
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
        final boolean multipart = attachedFile != null;

        final MimeMessageHelper messageHelper = new MimeMessageHelper(message, multipart);
        messageHelper.setFrom(fromAddress.getEmailAddress());
        messageHelper.setTo(toAddress.getEmailAddress());
        content.apply(messageHelper);

        if (multipart) {
            attachedFile.apply(messageHelper);
        }
    }

    @Override
    public String toString() {
        return "Email{" +
            "fromAddress=" + fromAddress +
            ", toAddress=" + toAddress +
            ", content=" + content +
            ", attachedFile=" + attachedFile +
            '}';
    }
}
