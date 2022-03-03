package com.minseoklim.woowahantechcampreview.common.domain;

import static org.assertj.core.api.Assertions.*;

import java.io.File;

import javax.mail.internet.MimeMessage;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

class EmailTest {
    @ParameterizedTest
    @DisplayName("유효하지 않은 이메일 주소가 입력되었을 때 예외 발생")
    @CsvSource(value = {"test@test.com:test#test.com", "www.naver.com:test@test.com"}, delimiter = ':')
    void buildByInvalidEmailAddress(final String fromAddress, final String toAddress) {
        // when, then
        assertThatIllegalArgumentException()
            .isThrownBy(() ->
                Email.builder()
                    .fromAddress(fromAddress)
                    .toAddress(toAddress)
                    .subject("제목")
                    .text("본문")
                    .build()
            ).withMessageContaining(EmailAddress.ERR_MSG);
    }

    @Test
    @DisplayName("첨부파일명과 첨부파일은 동시에 입력되거나 입력되지 않아야 한다. 둘 중 하나만 입력될 경우 예외 발생")
    void buildByInvalidAttachment() {
        // when, then
        assertThatIllegalArgumentException()
            .isThrownBy(() ->
                Email.builder()
                    .fromAddress("test@test.com")
                    .toAddress("test@test.com")
                    .subject("제목")
                    .text("본문")
                    .attachedFileName("첨부파일명")
                    .build()
            ).withMessageContaining(EmailAttachedFile.ERR_MSG);

        // when, then
        assertThatIllegalArgumentException()
            .isThrownBy(() ->
                Email.builder()
                    .fromAddress("test@test.com")
                    .toAddress("test@test.com")
                    .subject("제목")
                    .text("본문")
                    .attachedFile(new File("test.txt"))
                    .build()
            ).withMessageContaining(EmailAttachedFile.ERR_MSG);
    }

    @Test
    @DisplayName("이메일의 제목이나 본문이 없을 경우 예외 발생")
    void buildWithoutSubjectOrText() {
        // when, then
        assertThatNullPointerException()
            .isThrownBy(() ->
                Email.builder()
                    .fromAddress("test@test.com")
                    .toAddress("test@test.com")
                    .subject("제목")
                    .build()
            );

        // when, then
        assertThatNullPointerException()
            .isThrownBy(() ->
                Email.builder()
                    .fromAddress("test@test.com")
                    .toAddress("test@test.com")
                    .text("본문")
                    .build()
            );
    }

    @Test
    void toMimeMessage() {
        // given
        final JavaMailSender javaMailSender = new JavaMailSenderImpl();
        final Email email = Email.builder()
            .fromAddress("test@test.com")
            .toAddress("test@test.com")
            .subject("제목")
            .text("본문")
            .build();

        // when
        final MimeMessage mimeMessage = email.toMimeMessage(javaMailSender);

        // then
        assertThat(mimeMessage).isNotNull();
    }

    @Test
    void equalsAndHashCode() {
        // given
        final Email email1 = Email.builder()
            .fromAddress("test@test.com")
            .toAddress("test@test.com")
            .subject("제목")
            .text("본문")
            .build();

        final Email email2 = Email.builder()
            .fromAddress("test@test.com")
            .toAddress("test@test.com")
            .subject("제목")
            .text("본문")
            .build();

        // when, then
        assertThat(email1).isEqualTo(email2).hasSameHashCodeAs(email2);
    }
}
