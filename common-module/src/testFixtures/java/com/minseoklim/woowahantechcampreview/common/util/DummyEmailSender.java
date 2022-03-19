package com.minseoklim.woowahantechcampreview.common.util;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.minseoklim.woowahantechcampreview.common.domain.Email;
import com.minseoklim.woowahantechcampreview.util.TestUtil;

@Component
public class DummyEmailSender implements EmailSender {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void send(final Email email) {
        logger.info("Sent email : {}", email);

        try {
            TestUtil.writeFile("sentEmailText.txt", email.toString());
        } catch (final IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
