package com.minseoklim.woowahantechcampreview.auth.domain;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.minseoklim.woowahantechcampreview.auth.domain.repository.RefreshTokenRepository;

@ExtendWith(MockitoExtension.class)
class RefreshTokenValidatorTest {
    @Mock
    private JwtTokenParser tokenParser;
    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    @InjectMocks
    private RefreshTokenValidator refreshTokenValidator;

    @Test
    @DisplayName("사용자가 보낸 리프레시 토큰과 레디스에 저장되어 있는 값이 일치함을 검사")
    void validate() {
        // given
        when(tokenParser.validateRefreshToken(anyString()))
            .thenReturn(true);
        when(refreshTokenRepository.findById(anyString()))
            .thenReturn(Optional.of(new RefreshToken("1", "refreshTokenValue", 1L)));

        // when
        final boolean valid = refreshTokenValidator.validate("1", "refreshTokenValue");

        // then
        assertThat(valid).isTrue();
    }
}
