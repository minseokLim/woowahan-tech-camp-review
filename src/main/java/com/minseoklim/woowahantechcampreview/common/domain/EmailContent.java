package com.minseoklim.woowahantechcampreview.common.domain;

import java.util.Objects;

import javax.mail.MessagingException;

import org.springframework.mail.javamail.MimeMessageHelper;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class EmailContent {
    private final String subject;
    private final String text;
    private final boolean html;

    public EmailContent(final String subject, final String text, final boolean html) {
        this.subject = Objects.requireNonNull(subject);
        this.text = Objects.requireNonNull(text);
        this.html = html;
    }

    void apply(final MimeMessageHelper messageHelper) throws MessagingException {
        messageHelper.setSubject(subject);
        messageHelper.setText(text, html);
    }

    @Override
    public String toString() {
        return "EmailContent{" +
            "subject='" + subject + '\'' +
            ", text='" + text + '\'' +
            ", html=" + html +
            '}';
    }
}
