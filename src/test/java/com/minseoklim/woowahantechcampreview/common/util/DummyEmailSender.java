package com.minseoklim.woowahantechcampreview.common.util;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.minseoklim.woowahantechcampreview.common.dto.EmailDto;
import com.minseoklim.woowahantechcampreview.util.TestUtil;

@Profile("test")
@Component
public class DummyEmailSender implements EmailSender {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void send(final EmailDto emailDto) {
        logger.info("Sent email : {}", emailDto);

        try {
            TestUtil.writeFile("sentEmailText.txt", emailDto.toString());
        } catch (final IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
