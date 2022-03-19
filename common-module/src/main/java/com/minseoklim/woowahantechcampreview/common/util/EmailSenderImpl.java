package com.minseoklim.woowahantechcampreview.common.util;

import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.minseoklim.woowahantechcampreview.common.domain.Email;

@Profile("!test")
@Component
public class EmailSenderImpl implements EmailSender {
    private final JavaMailSender javaMailSender;

    public EmailSenderImpl(final JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void send(final Email email) {
        javaMailSender.send(email.toMimeMessage(javaMailSender));
    }
}
