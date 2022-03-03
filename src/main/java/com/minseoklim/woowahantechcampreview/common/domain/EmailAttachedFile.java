package com.minseoklim.woowahantechcampreview.common.domain;

import java.io.File;

import javax.mail.MessagingException;

import org.springframework.mail.javamail.MimeMessageHelper;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class EmailAttachedFile {
    public static final String ERR_MSG = "첨부파일명과 첨부파일은 함께 입력되어야 합니다.";

    private final String fileName;
    private final File file;

    public EmailAttachedFile(final String fileName, final File file) {
        this.fileName = fileName;
        this.file = file;

        validate();
    }

    void validate() {
        if ((fileName != null) ^ (file != null)) {
            throw new IllegalArgumentException(ERR_MSG);
        }
    }

    void apply(final MimeMessageHelper messageHelper) throws MessagingException {
        messageHelper.addAttachment(fileName, file);
    }

    @Override
    public String toString() {
        return fileName;
    }
}
